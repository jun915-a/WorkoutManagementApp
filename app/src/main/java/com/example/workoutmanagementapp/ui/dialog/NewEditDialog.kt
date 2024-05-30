package com.example.workoutmanagementapp.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutmanagementapp.R
import com.example.workoutmanagementapp.room.Task
import com.example.workoutmanagementapp.addOrReplace
import com.example.workoutmanagementapp.getDayList
import com.example.workoutmanagementapp.getMonthList
import com.example.workoutmanagementapp.getNowDate
import com.example.workoutmanagementapp.getRepList
import com.example.workoutmanagementapp.getSetList
import com.example.workoutmanagementapp.getWeightList
import com.example.workoutmanagementapp.getYearList
import com.example.workoutmanagementapp.partList
import com.example.workoutmanagementapp.ui.dataclass.TrainingDetail
import com.example.workoutmanagementapp.ui.dataclass.TrainingInfo
import com.example.workoutmanagementapp.ui.dataclass.TrainingMenuDatabase
import com.example.workoutmanagementapp.viewmodel.MainViewModel


@SuppressLint("StringFormatInvalid")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowNewEditDialog(
    context: Context,
    viewModel: MainViewModel = hiltViewModel()
) {
    if (viewModel.showEditDialogFlg) {
        //現在時刻取得
        val nowDate = getNowDate()
        var localYear = nowDate?.year.toString()
        var localMonth = nowDate?.monthValue.toString()
        var localDay = nowDate?.dayOfMonth.toString()

        //日付未洗濯でダイアログ表示
        if (viewModel.day.isNotEmpty()) {
            localYear = viewModel.day.substring(0, 4)
            localMonth = viewModel.day.substring(5, 7)
            localDay = viewModel.day.substring(8, 10)
        }
        //年
        val selectedYear = remember {
            mutableStateOf(
                localYear
            )
        }

        //月
        val selectedMonth = remember {
            mutableStateOf(
                localMonth
            )
        }

        //日
        val selectedDay = remember {
            mutableStateOf(
                localDay
            )
        }

        //部位
        val selectedParts = remember { mutableStateOf("選択してください") }


        AlertDialog(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            onDismissRequest = {
                viewModel.showEditDialogFlg = false
            },
            title = {
                Text(
                    text = context.getString(R.string.edit_dialog_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    var count by remember { mutableStateOf(0) }

                    LazyColumn {
                        item {
                            //日付
                            Text("日付")

                            DayPullDown(selectedYear = selectedYear, selectedMonth, selectedDay)
                            Spacer(modifier = Modifier.height(10.dp))

                            //部位
                            Text(text = context.getString(R.string.body_part))
                            PartsDropdown(partList, selectedParts)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(count) {
                            //種目、レップ、セット
                            AddTrainingMenu(
                                context = context,
                                selectedParts,
                                count
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            if (selectedParts.value != "選択してください") {
                                //種目の追加ボタン
                                TextButton(
                                    onClick = {
                                        count++
                                    }
                                ) {
                                    Text("種目の追加")
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = context.getString(R.string.memo_title))
                            val maxLength = 3
                            TextField(
                                modifier = Modifier
                                    .background(Color.White),
                                singleLine = true,
                                value = viewModel.memo,
                                onValueChange = {
                                    viewModel.memo = it
                                },
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            //体重
                            Text(text = context.getString(R.string.weight_title))

                            Row {
                                TextField(
                                    modifier = Modifier
                                        .width(80.dp),
                                    singleLine = true,
                                    value = viewModel.bodyWeight,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() } && newValue.length <= maxLength) {
                                            viewModel.bodyWeight = newValue
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                                )
                                Text(
                                    "kg",
                                    modifier = Modifier.align(Alignment.Bottom)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        var canSaveFlg = true
                        for (training in viewModel.trainingName) {
                            if (training == "選択してください" || training == "部位を選択してください") {
                                canSaveFlg = false
                            }
                        }
                        if (selectedParts.value == "選択してください" || viewModel.trainingName.size == 0) {
                            canSaveFlg = false
                        }


                        if (canSaveFlg) {
                            viewModel.dataBaseDay =
                                selectedYear.value + "-" + selectedMonth.value + "-" + selectedDay.value

                            viewModel.parts = selectedParts.value
                            println("test_log 日付：${viewModel.day} パーツ：${selectedParts.value} トレーニング：${viewModel.trainingName} レップ：${viewModel.rep} セット：${viewModel.set}")
                            newSaveTask(viewModel)

                            viewModel.showEditDialogFlg = false
                            Toast.makeText(
                                context,
                                context.getString(R.string.save_notion),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.empty_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        when {
                            //TODO 未入力項目でエラートースト表示
//                            viewModel.userId.isEmpty() -> {
//                                Toast.makeText(
//                                    context,
//                                    context.getString(R.string.name_empty_error),
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }

                            else -> {

                            }
                        }
                    }
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        //TODO:キャンセル確認ダイヤログ
                        viewModel.showEditDialogFlg = false
                        Toast.makeText(
                            context,
                            context.getString(R.string.edit_cancel_notion),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text("キャンセル")
                }
            },
        )
    }
}

/*
 * 登録処理
 */
fun newSaveTask(viewModel: MainViewModel) {
    println("test_log!A ${viewModel.currentMaxId}")
    val trainingDetail = mutableListOf<TrainingDetail>()
    for (i in viewModel.trainingName.indices) {
        trainingDetail.add(
            TrainingDetail(
                viewModel.trainingName[i],
                viewModel.weight[i],
                viewModel.set[i],
                viewModel.rep[i]
            )
        )
    }
    val obj = TrainingMenuDatabase(
        viewModel.currentMaxId + 1,
        viewModel.dataBaseDay,
        viewModel.parts,
        trainingDetail,
        viewModel.memo,
        viewModel.bodyWeight
    )
    val jsonStr = viewModel.toJson(obj)
    viewModel.insertTask(Task(viewModel.currentMaxId + 1, jsonStr))
}

@Composable
fun AddTrainingMenu(
    context: Context,
    selectedParts: MutableState<String>,
    count: Int,
) {
    Text(text = context.getString(R.string.training_event))

    //When分で部位によって分岐
    NewWorkoutMenuDropdown(selectedParts, count)

    Spacer(modifier = Modifier.height(10.dp))
    Text(text = context.getString(R.string.weight_rep_set_text))
    AddNewRepSetPullDown(count)
    Spacer(modifier = Modifier.height(10.dp))
}


@Composable
fun PartsDropdown(partsList: List<String>, selectedParts: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            partsList.forEach { parts ->
                DropdownMenuItem(onClick = {
                    selectedParts.value = parts
                    expanded = false
                }) {
                    Text(text = parts)
                }
            }
        }
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            Text(
                text = selectedParts.value,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "",
                modifier = Modifier.wrapContentWidth(Alignment.End)//右寄せ
            )
        }
    }
}

//種目
@Composable
fun NewWorkoutMenuDropdown(
    selectedParts: MutableState<String>,
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    //種目
    val selectedWorkout =
        if (selectedParts.value == "選択してください") remember { mutableStateOf("部位を選択してください") }
        else remember { mutableStateOf("選択してください") }
    var expanded by remember { mutableStateOf(false) }
    addOrReplace(
        viewModel.trainingName,
        count - 1,
        selectedWorkout.value
    )
    val workoutMenu: List<String> = when (selectedParts.value) {
        TrainingInfo.Chest.parts -> {
            TrainingInfo.Chest.workoutMenu
        }

        TrainingInfo.Biceps.parts -> {
            TrainingInfo.Biceps.workoutMenu
        }

        TrainingInfo.Triceps.parts -> {
            TrainingInfo.Triceps.workoutMenu
        }

        TrainingInfo.Shoulder.parts -> {
            TrainingInfo.Shoulder.workoutMenu
        }

        TrainingInfo.Back.parts -> {
            TrainingInfo.Back.workoutMenu
        }

        TrainingInfo.Abdominal.parts -> {
            TrainingInfo.Abdominal.workoutMenu
        }

        TrainingInfo.Leg.parts -> {
            TrainingInfo.Leg.workoutMenu
        }

        else -> {
            listOf()
        }
    }

    Column {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (selectedParts.value != "選択してください") {
                workoutMenu.forEach { workout ->
                    DropdownMenuItem(onClick = {
                        selectedWorkout.value = workout
                        expanded = false
                    }) {
                        Text(text = workout)
                    }
                }
            }
        }
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
        ) {
            Text(
                text = selectedWorkout.value,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "",
                modifier = Modifier.wrapContentWidth(Alignment.End)//右寄せ
            )
        }
    }
}

@Composable
fun DayPullDown(
    selectedYear: MutableState<String>,
    selectedMonth: MutableState<String>,
    selectedDay: MutableState<String>,
//    viewModel: MainViewModel = hiltViewModel()
) {
    var expandedYear by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedDay by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        //年
        Column {
            TextButton(
                onClick = { expandedYear = true },
                modifier = Modifier
                    .background(Color.White)
            ) {
                Text(selectedYear.value)
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            DropdownMenu(
                expanded = expandedYear,
                onDismissRequest = { expandedYear = false }
            ) {
                getYearList().forEach { selectYear ->
                    DropdownMenuItem(onClick = {
                        selectedYear.value = selectYear
                        expandedYear = false
                    }) {
                        Text(selectYear)
                    }
                }
            }
        }
        Text(
            text = "年",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(0.1f))

        //月
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            TextButton(
                onClick = { expandedMonth = true },
                modifier = Modifier.background(Color.White)
            ) {
                Text(selectedMonth.value)
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            DropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false }
            ) {
                getMonthList().forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedMonth.value = option
                        expandedMonth = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
        Text(
            text = "月",
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(0.1f))

        //日・セット
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextButton(
                onClick = { expandedDay = true },
                modifier = Modifier
                    .background(Color.White),

                ) {
                Text(selectedDay.value)
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            DropdownMenu(
                expanded = expandedDay,
                onDismissRequest = { expandedDay = false },
            ) {
                getDayList().forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedDay.value = option
                        expandedDay = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
        Text(
            text = "日",
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Composable
fun AddNewRepSetPullDown(
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    //weight
    val selectedWeight = remember { mutableStateOf("-") }

    //rep
    val selectedRep = remember { mutableStateOf("0") }

    //set
    val selectedSet = remember { mutableStateOf("0") }

    var expandedWeight by remember { mutableStateOf(false) }
    var expandedRep by remember { mutableStateOf(false) }
    var expandedSet by remember { mutableStateOf(false) }

    addOrReplace(
        viewModel.weight,
        count,
        selectedWeight.value
    )
    addOrReplace(
        viewModel.rep,
        count,
        selectedRep.value
    )
    addOrReplace(
        viewModel.set,
        count,
        selectedSet.value
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        //重量
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            TextButton(
                onClick = { expandedWeight = true },
                modifier = Modifier.background(Color.White)
            ) {
                Text(selectedWeight.value)
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }

            DropdownMenu(
                expanded = expandedWeight,
                onDismissRequest = { expandedWeight = false }
            ) {
                getWeightList().forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedWeight.value = option
                        expandedWeight = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
        Text(text = "kg", textAlign = TextAlign.Center, fontSize = 12.sp)
        Spacer(modifier = Modifier.weight(0.1f))
        //レップ
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            TextButton(
                onClick = { expandedRep = true },
                modifier = Modifier.background(Color.White)
            ) {
                Text(selectedRep.value)
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }

            DropdownMenu(
                expanded = expandedRep,
                onDismissRequest = { expandedRep = false }
            ) {
                getRepList().forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedRep.value = option
                        expandedRep = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
        Text(text = "レップ", textAlign = TextAlign.Center, fontSize = 12.sp)
        Spacer(modifier = Modifier.weight(0.1f))

        //日・セット
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextButton(
                onClick = { expandedSet = true },
                modifier = Modifier
                    .background(Color.White),

                ) {
                Text(selectedSet.value)
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            DropdownMenu(
                expanded = expandedSet,
                onDismissRequest = { expandedSet = false },
            ) {
                getSetList().forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedSet.value = option
                        expandedSet = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
        Text(
            text = "セット",
            textAlign = TextAlign.End,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.weight(0.1f))
    }
}
