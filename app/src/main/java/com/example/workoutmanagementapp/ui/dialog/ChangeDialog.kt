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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.workoutmanagementapp.addOrReplace
import com.example.workoutmanagementapp.getRepList
import com.example.workoutmanagementapp.getSetList
import com.example.workoutmanagementapp.getWeightList
import com.example.workoutmanagementapp.partList
import com.example.workoutmanagementapp.room.Task
import com.example.workoutmanagementapp.ui.dataclass.TrainingDetail
import com.example.workoutmanagementapp.ui.dataclass.TrainingInfo
import com.example.workoutmanagementapp.ui.dataclass.TrainingMenuDatabase
import com.example.workoutmanagementapp.viewmodel.MainViewModel


@SuppressLint("StringFormatInvalid")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowChangeDialog(
    context: Context,
    viewModel: MainViewModel = hiltViewModel()
) {
    println("test!!!A ${viewModel.trainingMenu}")

    if (viewModel.showChangeDialogFlg && viewModel.trainingMenu != null) {
        //年
        val selectedYear = remember {
            mutableStateOf(
                viewModel.trainingMenu?.time?.year.toString()
            )
        }

        //月
        val selectedMonth = remember {
            mutableStateOf(
                viewModel.trainingMenu?.time?.monthValue.toString()
            )
        }

        //日
        val selectedDay = remember {
            mutableStateOf(
                viewModel.trainingMenu?.time?.dayOfMonth.toString()
            )
        }

        //部位
        val selectedParts = remember { mutableStateOf(viewModel.trainingMenu?.trainingInfo?.parts) }


        AlertDialog(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            onDismissRequest = {
                viewModel.showChangeDialogFlg = false
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
                    var count by remember { mutableStateOf(viewModel.trainingMenu?.trainingDetailList?.size) }
                    LazyColumn {
                        item {
                            //日付
                            Text("日付")

                            DayPullDown(selectedYear = selectedYear, selectedMonth, selectedDay)
                            Spacer(modifier = Modifier.height(10.dp))

                            //部位
                            Text(text = context.getString(R.string.body_part))
                            ChangePartsDropdown(partList, selectedParts)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        count?.let {
                            items(it) { itemCount ->
                                //種目、レップ、セット既存データ取得
                                if (viewModel.trainingMenu?.trainingDetailList?.size!! > itemCount) {
                                    ExChangeAddTrainingMenu(
                                        context = context,
                                        selectedParts = selectedParts,
                                        count = itemCount
                                    )
                                }


                                if (viewModel.trainingMenu?.trainingDetailList?.size!! <= itemCount) {
                                    //種目、レップ、セット新規ボタンタップ時
                                    AddChangeAddTrainingMenu(
                                        context = context,
                                        selectedParts,
                                        itemCount
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        item {
                            if (selectedParts.value != "選択してください") {
                                //種目の追加ボタン
                                TextButton(
                                    onClick = {
                                        println("test_AAAZC${count}")
                                        count = count!! + 1
                                    }
                                ) {
                                    Text("種目の追加")
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = context.getString(R.string.memo_title))
                            val maxLength = 3
                            viewModel.memo = viewModel.trainingMenu?.memo.toString()
                            viewModel.bodyWeight = viewModel.trainingMenu?.bodyWeight.toString()
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
                        for (training in viewModel.trainingName.indices) {
                            println("testAAA!! ${viewModel.trainingName[training]} ${training}")
                            if (viewModel.trainingName[training] == "選択してください" || viewModel.trainingName[training] == "部位を選択してください") {
                                canSaveFlg = false
                            }
                        }
                        if (selectedParts.value == "選択してください") {
                            canSaveFlg = false
                        }

                        if (canSaveFlg) {
                            viewModel.dataBaseDay =
                                selectedYear.value + "-" + selectedMonth.value + "-" + selectedDay.value

                            viewModel.parts = selectedParts.value.toString()
                            println("test_log 日付：${viewModel.day} パーツ：${selectedParts.value} トレーニング：${viewModel.trainingName} レップ：${viewModel.rep} セット：${viewModel.set}")
                            updateTask(viewModel)

                            viewModel.showChangeDialogFlg = false
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
                    }
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        //TODO:キャンセル確認ダイヤログ
                        viewModel.showChangeDialogFlg = false
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
fun updateTask(viewModel: MainViewModel) {
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
    viewModel.trainingMenu?.id?.let {
        val obj = TrainingMenuDatabase(
            it,
            viewModel.dataBaseDay,
            viewModel.parts,
            trainingDetail,
            viewModel.memo,
            viewModel.bodyWeight
        )
        val jsonStr = viewModel.toJson(obj)
        viewModel.insertTask(Task(it, jsonStr))
    }
}

@Composable
fun ExChangeAddTrainingMenu(
    context: Context,
    selectedParts: MutableState<String?>,
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    val trainingDetail = viewModel.trainingMenu?.trainingDetailList?.get(count)
    Text(text = context.getString(R.string.training_event))

    ExChangeWorkoutMenuDropdown(trainingDetail, selectedParts, count)

    Spacer(modifier = Modifier.height(10.dp))
    Text(text = context.getString(R.string.weight_rep_set_text))
    ExNewRepSetPullDown(trainingDetail, count)
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun AddChangeAddTrainingMenu(
    context: Context,
    selectedParts: MutableState<String?>,
    count: Int,
) {
    Text(text = context.getString(R.string.training_event))

    //When分で部位によって分岐
    AddChangeWorkoutMenuDropdown(selectedParts, count)

    Spacer(modifier = Modifier.height(10.dp))
    Text(text = context.getString(R.string.weight_rep_set_text))
    AddNewRepSetPullDown(count)
    Spacer(modifier = Modifier.height(10.dp))
}


@Composable
fun ChangePartsDropdown(partsList: List<String>, selectedParts: MutableState<String?>) {
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
                text = selectedParts.value.toString(),
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

/*
 *種目 新規追加
 */
@Composable
fun AddChangeWorkoutMenuDropdown(
    selectedParts: MutableState<String?>,
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    println("test_AAAZ ${count}")
    //種目
    val selectedWorkout =
        if (selectedParts.value == "選択してください") remember { mutableStateOf("部位を選択してください") }
        else remember { mutableStateOf("選択してください") }
    var expanded by remember { mutableStateOf(false) }
    addOrReplace(
        viewModel.trainingName,
        count,
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

/*
 *種目 新規追加
 */
@Composable
fun ExChangeWorkoutMenuDropdown(
    trainingDetail: TrainingDetail?,
    selectedParts: MutableState<String?>,
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    println("test_AAAZA ${count}")

    //種目
    val selectedWorkout = remember { mutableStateOf(trainingDetail?.trainingName) }
    var expanded by remember { mutableStateOf(false) }
    selectedWorkout.value?.let {
        addOrReplace(
            viewModel.trainingName,
            count,
            it
        )
    }
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
                text = selectedWorkout.value.toString(),
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
fun ExNewRepSetPullDown(
    trainingDetail: TrainingDetail?,
    count: Int,
    viewModel: MainViewModel = hiltViewModel()
) {
    //weight
    val selectedWeight = remember { mutableStateOf(trainingDetail?.weight) }

    //rep
    val selectedRep = remember { mutableStateOf(trainingDetail?.rep) }

    //set
    val selectedSet = remember { mutableStateOf(trainingDetail?.set) }

    var expandedWeight by remember { mutableStateOf(false) }
    var expandedRep by remember { mutableStateOf(false) }
    var expandedSet by remember { mutableStateOf(false) }

    addOrReplace(
        viewModel.weight,
        count,
        selectedWeight.value.toString()
    )
    addOrReplace(
        viewModel.rep,
        count,
        selectedRep.value.toString()
    )
    addOrReplace(
        viewModel.set,
        count,
        selectedSet.value.toString()
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
                Text(selectedWeight.value.toString())
                Spacer(modifier = Modifier.weight(0.1f))
                androidx.compose.material3.Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }

            androidx.compose.material.DropdownMenu(
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
                Text(selectedRep.value.toString())
                Spacer(modifier = Modifier.weight(0.1f))
                androidx.compose.material3.Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }

            androidx.compose.material.DropdownMenu(
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
                Text(selectedSet.value.toString())
                Spacer(modifier = Modifier.weight(0.1f))
                androidx.compose.material3.Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier
                        .size(6.dp)
                        .wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            androidx.compose.material.DropdownMenu(
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
