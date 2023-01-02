package pl.polsl.stocktakingApp.domain.services

class RegexService {
    private val letterRegex = "[a-zA-Z]"
    private val digitRegex = "\\d"

    fun returnRegexStringFromString(regexString: String?, foundString: String): String {
        return if (regexString != null) {
            val regex = rewriteStringToRegex(regexString)
            var foundPattern = regex.find(foundString.uppercase())?.value

            if (foundPattern == null) {
                foundPattern = switchSigns(foundString, regex)
            }

            foundPattern ?: foundString
        } else {
            foundString
        }
    }

    fun returnRegexStringOrNullFromString(regexString: String?, foundString: String): String? {
        return if (regexString != null) {
            val regex = rewriteStringToRegex(regexString)
            var foundPattern = regex.find(foundString.uppercase())?.value

            if (foundPattern == null) {
                foundPattern = switchSigns(foundString, regex)
            }

            foundPattern
        } else {
            null
        }
    }

    private fun rewriteStringToRegex(text: String): Regex {
        val string = text.filterNot { it.isWhitespace() }

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

        return regexStringBuilder.toString().toRegex()
    }

    private fun switchSigns(scannedString: String, regex: Regex): String? {
        val dReplacedForZero = scannedString.replace('D', '0')
        val oReplacedForZero = dReplacedForZero.replace('O', '0')
        return regex.find(oReplacedForZero)?.value
    }
}