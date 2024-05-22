package com.example.workoutmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.workoutmanagementapp.ui.CalendarDisplay
import com.example.workoutmanagementapp.ui.ShowEditDialog
import com.example.workoutmanagementapp.ui.theme.WorkoutManagementAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutManagementAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                    ShowEditDialog(context = this)
                }
            }
        }
    }
}

@Preview
@Composable
fun Previewa(){
    MainScreen()
}

