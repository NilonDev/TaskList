package tasklist

import kotlinx.datetime.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class Task {
    var noteText: MutableList<String>
    var importance: Priority
    var date: String
    var time: String
    var relevance: Relevance

    init {
        noteText = mutableListOf()
        importance = Priority("L")
        date = ""
        time = ""
        relevance = Relevance("O")
    }

    fun setText(text: MutableList<String>) {
        if (text.isNotEmpty()) {
            noteText.clear()
            noteText.addAll(text)
        }
    }

    fun setPriority(userPriority: String) {
        val validPriority = listOf<String>("C", "H", "N", "L", "c", "h", "n", "l")

        if (validPriority.contains(userPriority)) {
            importance = Priority(userPriority.uppercase())
        } else {
            throw IllegalArgumentException("ERROR")
        }
    }

    fun checkingRelevance() {

        val (years, month, day) = date.split("-").map { it.toInt() }
        val taskDate = LocalDate(years, month, day)

        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        val numberOfDays = currentDate.daysUntil(taskDate)

        /*
        val taskDate = date.toInstant()
        val currentDate = Clock.System.now()
        val numberOfDays = currentDate.until(taskDate, DateTimeUnit.DAY, TimeZone.UTC).toInt()

         */

        when {
            numberOfDays == 0 -> relevance = Relevance("T")
            numberOfDays > 0 ->  relevance = Relevance("I")
            else -> relevance = Relevance("O")
        }
    }

    fun setDateTask(userDate: String) {
        val (years, month, day) = userDate.split("-").map { it.toInt() }
        val newDate = LocalDate(years, month, day)
        date = newDate.toString()
        checkingRelevance()
        //println(newDate.toString())
    }

    fun setTimeTask(date: String) {
        val (hours, minutes) = date.split(":").map { it.toInt() }
        val newTime: kotlinx.datetime.LocalTime = kotlinx.datetime.LocalTime(hours, minutes)
        time = newTime.toString()
        //println(newDate.toString())
    }
}

class Priority(var level: String) {
    var colorLevel: String = setNewColor()

    fun setNewColor(): String {
        val myColor = when(level) {
            "C" -> "\u001B[101m \u001B[0m"
            "H" -> "\u001B[103m \u001B[0m"
            "N" -> "\u001B[102m \u001B[0m"
            "L" -> "\u001B[104m \u001B[0m"
            else -> "\u001B[105m \u001B[0m" // ???
        }
        return  myColor
    }
}

class Relevance(var relevance: String) {
    var colorRelevance: String = setNewColor()

    fun setNewColor(): String {
        val myColor = when(relevance) {
            "I" -> "\u001B[102m \u001B[0m"
            "T" -> "\u001B[103m \u001B[0m"
            "O" -> "\u001B[101m \u001B[0m"
            else -> "\u001B[105m \u001B[0m" // ???
        }
        return  myColor
    }
}