package tasklist

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import kotlin.system.exitProcess

const val ADD = "add"
const val PRINT = "print"
const val EDIT = "edit"
const val DELETE = "delete"
const val END = "end"

class Tasklist() {
    private val taskList = mutableListOf<Task>()

    private fun addTask() {
        val task = Task()
        setPriorityOfTask(task)
        setDateOfTask(task)
        setTimeOfTask(task)
        setTextOfTask(task)
        if (task.noteText.isNotEmpty()) taskList.add(task)
    }

    private fun setPriorityOfTask(task: Task) {
        while (true) {
            try {
                println("Input the task priority (C, H, N, L):")
                val priority = readln()
                task.setPriority(priority)
                break
            } catch (e: IllegalArgumentException) {
                continue
            }
        }
    }

    private fun setDateOfTask(task: Task) {
        while (true) {
            try {
                println("Input the date (yyyy-mm-dd):")
                val date = readln()
                task.setDateTask(date)
                break
            } catch (e: IllegalArgumentException) {
                println("The input date is invalid")
                continue
            }
        }
    }

    private fun setTimeOfTask(task: Task) {
        while (true) {
            try {
                println("Input the time (hh:mm):")
                val time = readln()
                task.setTimeTask(time)
                break
            } catch (e: IllegalArgumentException) {
                println("The input time is invalid")
                continue
            }
        }
    }

    private fun setTextOfTask(task: Task) {
        println("Input a new task (enter a blank line to end):")
        val text = readText()
        if (text.isNotEmpty()) task.setText(text) else println("The task is blank")
    }

    private fun readText(): MutableList<String> {
        val finalTask = mutableListOf<String>()
        var bufferTask = ""

        while (true) {
            bufferTask = readln().trim()
            if (bufferTask.isEmpty()) break else finalTask.add(bufferTask)
        }

        //return finalTask.joinToString("\n")

        return finalTask
    }

    private fun printTableTask() {
        if (taskList.isEmpty()) {
            println("No tasks have been input")
            return
        }

        val TableColor = TableColor(taskList)
        TableColor.printTable()
    }

    private fun printTask() {
        /*
        var numeration = 1

        if (taskList.isEmpty()) {
            println("No tasks have been input")
            return
        }

        for (task in taskList) {
            var tempTask = ""
            when {
                numeration in 1..9 -> {
                    tempTask = task.noteText.replace("\n", "\n   ")
                    println("$numeration  ${task.date} ${task.time} ${task.importance} ${task.relevance}")
                    println("   $tempTask")
                }
                numeration in 10..99 -> {
                    tempTask = task.noteText.replace("\n", "\n   ")
                    println("$numeration ${task.date} ${task.time} ${task.importance} ${task.relevance}")
                    println("   $tempTask")
                }
            }
            println()
            numeration++
        }
        */
    }

    private fun exitProgram() {
        saveTaskList()
        println("Tasklist exiting!")
        exitProcess(0)
    }

    private fun deleteTask() {
        if (taskList.isEmpty()) {
            println("No tasks have been input")
        } else {
            printTableTask()
            var deleteNumber: Int

            error@while (true) {
                try {
                    println("Input the task number (1-${taskList.size}):")
                    deleteNumber = readln().toInt()
                    if (deleteNumber in 1..taskList.size) break
                    println("Invalid task number")
                } catch(e: NumberFormatException) {
                    println("Invalid task number")
                    continue@error
                }
            }

            taskList.removeAt(deleteNumber - 1)
            println("The task is deleted")
        }
    }

    private fun editTask() {
        val validOption = listOf("priority", "date", "time", "task")
        if (taskList.isEmpty()) {
            println("No tasks have been input")
        } else {
            printTableTask()
            var editNumber: Int

            error@while (true) {
                try {
                    println("Input the task number (1-${taskList.size}):")
                    editNumber = readln().toInt()
                    if (editNumber in 1..taskList.size) break
                    println("Invalid task number")
                } catch(e: NumberFormatException) {
                    println("Invalid task number")
                    continue@error
                }
            }

            val task = taskList[editNumber - 1]

            println("Input a field to edit (priority, date, time, task):")
            var editOption = readln()

            while (editOption !in validOption) {
                println("Invalid field")
                println("Input a field to edit (priority, date, time, task):")
                editOption = readln()
            }

            when (editOption) {
                "priority" -> setPriorityOfTask(task)
                "date" -> setDateOfTask(task)
                "time" -> setTimeOfTask(task)
                "task" -> setTextOfTask(task)
            }
            println("The task is changed")
        }

    }

    public fun menu() {
        loadTaskList()
        println("Input an action (add, print, edit, delete, end):")
        var actionСhoice = readln().trim()

        while (true) {
            when (actionСhoice) {
                ADD -> addTask()
                PRINT -> printTableTask()
                EDIT -> editTask()
                DELETE -> deleteTask()
                END -> exitProgram()
                else -> println("The input action is invalid")
            }
            println("Input an action (add, print, edit, delete, end):")
            actionСhoice = readln().trim()
        }
    }

    private fun saveTaskList() {
        if (taskList.isEmpty()) return

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val type = Types.newParameterizedType(List::class.java, Task::class.java,
            Priority::class.java, Relevance::class.java)
        val taskListAdapter = moshi.adapter<List<Task?>>(type)

        val jsonFile = File("tasklist.json")
        jsonFile.writeText(taskListAdapter.toJson(taskList))

    }

    private fun loadTaskList() {
        val jsonFile = File("tasklist.json")
        if (!jsonFile.exists()) return

        val jsonData = jsonFile.readText()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val type = Types.newParameterizedType(List::class.java, Task::class.java,
            Priority::class.java, Relevance::class.java)
        val taskListAdapter = moshi.adapter<List<Task?>>(type)

        val tempTaskList = (taskListAdapter.fromJson(jsonData))
        tempTaskList?.forEach { if (it != null) taskList.add(it)}
    }
}