package tasklist

const val headTable = """
+----+------------+-------+---+---+--------------------------------------------+
| N  |    Date    | Time  | P | D |                   Task                     |
+----+------------+-------+---+---+--------------------------------------------+
"""

const val taskText = "|    |            |       |   |   |"

const val endTaskText = "+----+------------+-------+---+---+--------------------------------------------+"

class TableColor(val taskList: MutableList<Task>) {

    fun printTable() {

        print(headTable)

        var space = "  "
        var numbers = 1
        var resultString = ""

        val myColor = "\u001B[101m \u001B[0m"

        for (task in taskList) {

            if (numbers > 9) space = " "
            resultString = "| $numbers$space| ${task.date} | ${task.time} | ${task.importance.colorLevel} | ${task.relevance.colorRelevance} |"

            for (i in task.noteText.indices) {
                if (i > 0) resultString += taskText
                if (task.noteText[i].length < 44) {
                    resultString += printLittleString(task.noteText[i]) + "|\n"
                } else {
                    resultString += printBigString(task.noteText[i]) + "|\n"
                }
            }
            resultString += endTaskText + "\n"
            print(resultString)
            numbers++
        }
    }

    fun printLittleString(miniTask: String): String {
        val countSpace = 44 - miniTask.length
        var resultString = miniTask
        repeat(countSpace) {
            resultString += " "
        }
        return resultString
    }

    fun printBigString(miniTask: String): String {
        val count = miniTask.length / 44
        var index = 0
        var resultString = ""


        for (i in 1..count) {
            if (miniTask.length % 44 == 0 && i == count) {
                resultString += miniTask.substring(index)
            } else {
                resultString += miniTask.substring(index, index + 44) + "|\n" + taskText
            }
            index += 44
        }

        if (miniTask.length % 44 != 0) {
            resultString += printLittleString(miniTask.substring(index))
        }

        return resultString
    }
}