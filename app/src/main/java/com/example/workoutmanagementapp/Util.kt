package com.example.workoutmanagementapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.example.workoutmanagementapp.ui.TrainingInfo
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
fun addOrReplace(list: MutableList<String>, index: Int, element: String) {
    if (index < list.size) {
        list[index] = element // 既存のインデックスに上書き
    } else {
        list.add(element) // インデックスが範囲外の場合は追加
    }
}
fun addOrReplaceT(list: MutableList<TrainingInfo>, index: Int, element: TrainingInfo) {
    if (index < list.size) {
        list[index] = element // 既存のインデックスに上書き
    } else {
        list.add(element) // インデックスが範囲外の場合は追加
    }
}
fun getNowDate(): LocalDateTime? {
    return LocalDateTime.now()
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

/**
 * Alternative way to find the first fully visible month in the layout.
 *
 * @see [rememberFirstVisibleMonthAfterScroll]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    // Only take non-null values as null will be produced when the
    // list is mid-scroll as no index will be completely visible.
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }

//fun Context.findActivity(): Activity {
//    var context = this
//    while (context is ContextWrapper) {
//        if (context is Activity) return context
//        context = context.baseContext
//    }
//    throw IllegalStateException("no activity")
//}
//
//fun getWeekPageTitle(week: Week): String {
//    val firstDate = week.days.first().date
//    val lastDate = week.days.last().date
//    return when {
//        firstDate.yearMonth == lastDate.yearMonth -> {
//            firstDate.yearMonth.displayText()
//        }
//        firstDate.year == lastDate.year -> {
//            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
//        }
//        else -> {
//            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
//        }
//    }
//}
