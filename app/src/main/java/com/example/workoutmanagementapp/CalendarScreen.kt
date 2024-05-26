package com.example.workoutmanagementapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutmanagementapp.ui.TrainingDetail
import com.example.workoutmanagementapp.ui.TrainingMenu
import com.example.workoutmanagementapp.ui.generateTraining
import com.example.workoutmanagementapp.ui.theme.DayTextColor
import com.example.workoutmanagementapp.ui.theme.ItemBgColor
import com.example.workoutmanagementapp.ui.theme.Page_bg_color
import com.example.workoutmanagementapp.viewmodel.MainViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showEditDialogFlg = true
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = {
            CalendarScreen()
        }
    )
}

@Composable
fun CalendarScreen() {
    val trainings = generateTraining().groupBy { it.time }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val trainingInSelectedDate = remember {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else trainings[date].orEmpty()
        }
    }
    //    StatusBarColorUpdateEffect(toolbarColor)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Page_bg_color),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
        LaunchedEffect(visibleMonth) {
            // Clear selection if we scroll to a new month.
            selection = null
        }

        // Draw light content on dark background.
        CompositionLocalProvider(LocalContentColor provides darkColors().onSurface) {
            SimpleCalendarTitle(
                modifier = Modifier
                    .background(Page_bg_color)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                },
            )

            HorizontalCalendar(
                modifier = Modifier.wrapContentWidth(),
                state = state,
                dayContent = { day ->
                    val colors = if (day.position == DayPosition.MonthDate) {
                        trainings[day.date].orEmpty().map { it.trainingInfo.color }
                    } else {
                        emptyList()
                    }
                    Day(
                        day = day,
                        isSelected = selection == day,
                        colors = colors,
                    ) { clicked ->
                        selection = clicked
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                },
            )
            //余白
            Divider(color = Page_bg_color)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(trainingInSelectedDate.value) {
                    TrainingInformation(it)
                }
            }
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // 正方形
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) Color.Blue else Color.Transparent,
            )
            .padding(1.dp)
            .background(color = ItemBgColor)
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> DayTextColor
            DayPosition.InDate, DayPosition.OutDate -> Color.Gray
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            for (color in colors) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(color),
                )
            }
        }
    }
}

//曜日の横リスト
@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = DayTextColor,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun LazyItemScope.TrainingInformation(trainingMenu: TrainingMenu) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .background(color = trainingMenu.trainingInfo.color)
                .width(10.dp)
                .fillMaxHeight()
                .aspectRatio(1f),
        )
        Column(
            modifier = Modifier
                .width(40.dp)
                .background(ItemBgColor)
                .fillMaxHeight()
                .align(Alignment.CenterVertically),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${trainingMenu.time?.monthValue.toString()}/${trainingMenu.time?.dayOfMonth.toString()}",
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                fontSize = 12.sp,
                color = DayTextColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = trainingMenu.trainingInfo.parts,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                fontSize = 12.sp,
                color = DayTextColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )
        }
        Box(
            modifier = Modifier
                .background(color = ItemBgColor)
                .weight(1f)
                .clickable {
                    //todo メニュー編集ダイアログ表示
                }
                .fillMaxHeight(),
        ) {
            bottomList(trainingMenu)
        }
    }
    Divider(color = Page_bg_color, thickness = 2.dp)
}

//カレンダー下のリスト
@Composable
fun bottomList(training: TrainingMenu) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Image(
                painter = painterResource(id = training.trainingInfo.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center)
            )
        }
        //トレーニングメニュー、セット/レップ
        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
                .background(Page_bg_color)
                .align(Alignment.CenterVertically),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(training.trainingDetailList) {
                TrainingMenuList(it)
            }
        }
//        Column(
//            modifier = Modifier
//                .weight(0.7f)
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.Center,
//        ) {
//            Text(
//                modifier = Modifier.fillMaxWidth(),
//                text = training.trainingName,
//                textAlign = TextAlign.Center,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Black,
//                color = DayTextColor
//            )
//            Text(
//                modifier = Modifier.fillMaxWidth(),
//                text = training.trainingName,
//                textAlign = TextAlign.Center,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Light,
//                color = DayTextColor
//
//            )
//        }
    }
}

@Composable
fun TrainingMenuList(trainingDetail: TrainingDetail) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(ItemBgColor)
            .fillMaxHeight()
            .padding(end = 10.dp),
        verticalArrangement = Arrangement.Center, // 縦方向に中央揃え
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth()
            ,
            text = trainingDetail.trainingName,
            textAlign = TextAlign.Center,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            color = DayTextColor
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth()
            ,
            text = "${trainingDetail.set}セット",
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = DayTextColor
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth()
            ,
            text = "${trainingDetail.rep}レップ",
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = DayTextColor
        )
    }
}

@Composable
@Preview
fun Test() {
}