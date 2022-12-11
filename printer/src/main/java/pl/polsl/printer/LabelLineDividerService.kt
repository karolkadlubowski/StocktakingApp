package pl.polsl.printer

class LabelLineDividerService {
    companion object {
        const val MAX_LINE_LENGTH = 30
        const val MAX_STRING_LENGTH = 60
    }

    fun divideString(stringToDivide: String): Array<String> {
        var string = stringToDivide
        if (string.length > MAX_STRING_LENGTH) {
            string = string.substring(0, 60)
        }

        val wordList = string.split(" ").toTypedArray()

        val dividePoint: Int = if (wordList.size % 2 == 0) {
            wordList.size / 2
        } else {
            (wordList.size / 2) + 1
        }

        if (wordList.size == 1 || wordList[0].length > MAX_LINE_LENGTH) {
            return divideStringEqually(string)
        }

        var result: Array<String> = divideStringToTwoSubstrings(wordList, dividePoint)

        if (result[0].length > MAX_LINE_LENGTH) {
            result = shortenFirstLine(result, wordList, dividePoint)
        } else if (result[0].length < MAX_LINE_LENGTH) {
            result = extendFirstLine(result, wordList, dividePoint)
        }

        if (result[0].length > MAX_LINE_LENGTH) {
            return divideStringEqually(string)
        }

        if (result[1].length > MAX_LINE_LENGTH) {
            result[1] = result[1].substring(0, MAX_LINE_LENGTH)
        }

        return result
    }

    private fun divideStringEqually(string: String): Array<String> = arrayOf(
        string.substring(startIndex = 0, endIndex = MAX_LINE_LENGTH),
        string.substring(startIndex = MAX_LINE_LENGTH)
    )

    private fun divideStringToTwoSubstrings(
        wordList: Array<String>,
        dividePoint: Int
    ): Array<String> {
        val result: Array<String> = arrayOf("", "")

        result[0] = buildString {
            for (i in 0 until dividePoint - 1) {
                append(wordList[i] + " ")
            }
            append(wordList[dividePoint - 1])
        }

        result[1] = buildString {
            for (i in dividePoint until wordList.size - 1) {
                append(wordList[i] + " ")
            }
            append(wordList[wordList.size - 1])
        }

        return result
    }


    private fun shortenFirstLine(
        firstDivisionResult: Array<String>,
        wordList: Array<String>,
        twoLinesDividerPoint: Int
    ): Array<String> {
        var dividePoint = twoLinesDividerPoint
        var result: Array<String>
        do {
            dividePoint--
            result = divideStringToTwoSubstrings(wordList, dividePoint)
        } while ((firstDivisionResult[0].dropLast(wordList[dividePoint - 1].length + 1)).length > MAX_LINE_LENGTH)
        return result
    }

    private fun extendFirstLine(
        firstDivisionResult: Array<String>,
        wordList: Array<String>,
        twoLinesDividerPoint: Int
    ): Array<String> {
        var dividePoint = twoLinesDividerPoint
        var result = firstDivisionResult
        while ((firstDivisionResult[0] + " " + wordList[dividePoint]).length <= MAX_LINE_LENGTH) {
            dividePoint++
            result = divideStringToTwoSubstrings(wordList, dividePoint)
        }
        return result
    }
}