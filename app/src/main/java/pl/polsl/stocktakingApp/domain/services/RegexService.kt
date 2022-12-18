package pl.polsl.stocktakingApp.domain.services

class RegexService {
    private val letterRegex = "[a-zA-Z]"
    private val digitRegex = "\\d"

    fun rewriteStringToRegex(string: String): String {
        val string = string.filterNot { it.isWhitespace() }

        val regexStringBuilder = StringBuilder()

        string.forEach {
            if (it.isLetter()) {
                regexStringBuilder.append(letterRegex)
            } else if (it.isDigit()) {
                regexStringBuilder.append(digitRegex)
            } else {
                regexStringBuilder.append(it)
            }
        }
        val strings = regexStringBuilder.toString()

        return strings
    }
}