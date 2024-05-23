package com.example.workoutmanagementapp.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutmanagementapp.R
import com.example.workoutmanagementapp.viewmodel.MainViewModel

//val buiOptions =
//    listOf("胸", "二頭筋", "三頭筋", "肩", "背中", "腹筋", "足")
val partList = listOf(
    TrainingInfo.Chest.parts,
    TrainingInfo.Biceps.parts,
    TrainingInfo.Triceps.parts,
    TrainingInfo.Shoulder.parts,
    TrainingInfo.Back.parts,
    TrainingInfo.Abdominal.parts,
    TrainingInfo.Leg.parts,
)

val workoutMenuList = listOf(
    TrainingInfo.Chest.workoutMenu,
    TrainingInfo.Biceps.workoutMenu,
    TrainingInfo.Triceps.workoutMenu,
    TrainingInfo.Shoulder.workoutMenu,
    TrainingInfo.Back.workoutMenu,
    TrainingInfo.Abdominal.workoutMenu,
    TrainingInfo.Leg.workoutMenu,

    )

fun getMonthList(): List<String> {
    return (1..12).map { it.toString() }
}

fun getDayList(): List<String> {
    return (1..31).map { it.toString() }
}

fun getRepList(): List<String> {
    return (1..50).map { it.toString() }
}

fun getSetList(): List<String> {
    return (1..10).map { it.toString() }
}

@SuppressLint("StringFormatInvalid")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEditDialog(
    context: Context,
    viewModel: MainViewModel = hiltViewModel()
) {
    val selectedParts = remember { mutableStateOf("選択してください") }

    if (viewModel.showEditDialogFlg) {
        AlertDialog(
            modifier = Modifier
                .padding(horizontal = 10.dp),
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
                    var i by remember { mutableStateOf(0) }

                    LazyColumn {
                        item {
                            //日付
                            Text("日付")
                            TwoPullDown(Type.Day)
                            Spacer(modifier = Modifier.height(10.dp))

                            //部位
                            Text(text = context.getString(R.string.body_part))
                            PartsDropdown(partList, selectedParts)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(i) {
                            //種目、レップ、セット
                            AddTrainingMenu(context = context, selectedParts)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            //種目の追加ボタン
                            TextButton(
                                onClick = {
                                    i++
                                }
                            ) {
                                Text("種目の追加")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            //種目の追加ボタン
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
                                    value = viewModel.weight,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() } && newValue.length <= maxLength) {
                                            viewModel.weight = newValue
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
                        when {
//                            viewModel.userId.isEmpty() -> {
//                                Toast.makeText(
//                                    context,
//                                    context.getString(R.string.name_empty_error),
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }

                            else -> {
                                //登録処理
//                                viewModel.updateTask(task)
                                viewModel.showEditDialogFlg = false
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.save_notion),
                                    Toast.LENGTH_SHORT
                                ).show()
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

@Composable
fun AddTrainingMenu(context: Context, selectedParts: MutableState<String>) {
    if (selectedParts.value != "選択してください") {
        Text(text = context.getString(R.string.training_event))

        //When分で部位によって分岐
        WorkoutMenuDropdown(WorkoutMenu.abdominalMenu)

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = context.getString(R.string.rep_set_value))
        TwoPullDown(Type.Rep)
        Spacer(modifier = Modifier.height(10.dp))
    }
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
fun WorkoutMenuDropdown(workoutMenu: List<String>) {
    val selectedWorkout = remember { mutableStateOf("選択してください") }

    var expanded by remember { mutableStateOf(false) }
    Column {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            workoutMenu.forEach { workout ->
                DropdownMenuItem(onClick = {
                    selectedWorkout.value = workout
                    expanded = false
                }) {
                    Text(text = workout)
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

enum class Type {
    Day, Rep
}

@Composable
fun TwoPullDown(type: Type) {
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    var selectedOption1 by remember { mutableStateOf("0") }
    var selectedOption2 by remember { mutableStateOf("0") }

    Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            TextButton(
                onClick = { expanded1 = true },
                modifier = Modifier.background(Color.White)
            ) {
                Text(selectedOption1)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier.wrapContentWidth(Alignment.End)//右寄せ
                )
            }

            DropdownMenu(
                expanded = expanded1,
                onDismissRequest = { expanded1 = false }
            ) {
                if (type == Type.Day) {
                    getMonthList().forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption1 = option
                            expanded1 = false
                        }) {
                            Text(option)
                        }
                    }
                } else {
                    getRepList().forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption1 = option
                            expanded1 = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }
        }
        Text(
            text = if (type == Type.Day) "月" else "レップ",
            modifier = Modifier
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Center,
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextButton(
                onClick = { expanded2 = true },
                modifier = Modifier
                    .background(Color.White),

                ) {
                Text(selectedOption2)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "",
                    modifier = Modifier.wrapContentWidth(Alignment.End)//右寄せ
                )
            }
            DropdownMenu(
                expanded = expanded2,
                onDismissRequest = { expanded2 = false },
            ) {
                if (type == Type.Day) {
                    getDayList().forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption2 = option
                            expanded2 = false
                        }) {
                            Text(option)
                        }
                    }
                } else {
                    getSetList().forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption2 = option
                            expanded2 = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }
        }
        Text(
            text = if (type == Type.Day) "日" else "セット",
            modifier = Modifier
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Test() {
    Row {
        TextField(value = "aaa", onValueChange = {})
//           modifier = Modifier
//               .padding(10.dp))
        Text("KG", modifier = Modifier.align(Alignment.Bottom))
        Text("KG")

    }
}