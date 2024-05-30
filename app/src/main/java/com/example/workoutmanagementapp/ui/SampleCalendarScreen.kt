package com.example.workoutmanagementapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

//@Composable
//fun CalendarDisplay() {
//    val nowDate = LocalDate.now().toString().substring(8, 10)
//    // 現在の年月
//    val currentMonth = remember {
//        YearMonth.now()
//    }
//    // 現在より前の年月
//    val startMonth = remember { currentMonth.minusMonths(100) }
//    // 現在より後の年月
//    val endMonth = remember { currentMonth.plusMonths(100) }
//    // 曜日
//    val daysOfWeek = remember { daysOfWeek() }
//
//    // カレンダーの状態を持つ
//    val state = rememberCalendarState(
//        startMonth = startMonth,
//        endMonth = endMonth,
//        firstVisibleMonth = currentMonth,
//        firstDayOfWeek = daysOfWeek.first(),
//        outDateStyle = OutDateStyle.EndOfGrid
//    )
//
//    // 横スクロールのカレンダーを作成するためのComposable関数
//    // 縦スクロールのVerticalなどもある
//    HorizontalCalendar(
//        state = state,
//        // 日付を表示する部分
//        dayContent = {
//            val isNowDate =
//                it.date.dayOfMonth.toString() == nowDate && it.date.monthValue == currentMonth.monthValue
//            Day(it, isNowDate)
//        },
//        // カレンダーのヘッダー
//        monthHeader = { DaysOfWeekTitle(daysOfWeek = daysOfWeek) }
//    )
//
//}
//
//@Composable
//fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        for (dayOfWeek in daysOfWeek) {
//            Text(
//                modifier = Modifier.weight(1f),
//                textAlign = TextAlign.Center,
//                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
//                // 土日だけそれぞれ色を変えたいので対応したカラーコードを返している
//                color = Color.Red //getDayOfWeekTextColor(index)
//            )
//        }
//    }
//}
//
//@Composable
//fun Day(day: CalendarDay, isNowDate: Boolean) {
//    Box(
//        modifier = Modifier
//            .aspectRatio(1f),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = day.date.dayOfMonth.toString(),
//            // ここで今月でないものの日付をグレーアウトさせている
//            color = if (isNowDate) {
//                Color.Green
//            } else if (day.position == DayPosition.MonthDate) {
//                Color.Black
//            } else {
//                Color.Gray
//            },
//            modifier = Modifier.clickable {
////                Log.d("test_log", "test ${day.date.dayOfMonth.toString()}")
//            }
//        )
//    }
//}
//
