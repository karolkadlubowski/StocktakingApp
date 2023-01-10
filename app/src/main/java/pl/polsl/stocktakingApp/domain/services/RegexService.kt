package pl.polsl.stocktakingApp.domain.services

class RegexService {

    fun getStocktakingNumberFromText(regexString: String?, text: String): String {
        return if (regexString != null) {
            val regex = _rewriteStringToRegex(regexString)
            var foundPattern = regex.find(text.uppercase())?.value

            if (foundPattern == null) {
                foundPattern = _switchSigns(text, regex)
            }

            foundPattern ?: text
        } else {
            text
        }
    }

    fun getStocktakingNumberOrNullFromText(regexString: String?, foundText: String): String? {
        return if (regexString != null) {
            val regex = _rewriteStringToRegex(regexString)
            var foundPattern = regex.find(foundText.uppercase())?.value

            if (foundPattern == null) {
                foundPattern = _switchSigns(foundText, regex)
            }

            foundPattern
        } else {
            null
        }
    }

    private fun _rewriteStringToRegex(text: String): Regex {
        val string = text.filterNot { it.isWhitespace() }

        val regexStringBuilder = StringBuilder()

        string.forEach {
            if (it.isLetter()) {
                regexStringBuilder.append(LETTER_REGEX)
            } else if (it.isDigit()) {
                regexStringBuilder.append(DIGIT_REGEX)
            } else {
                regexStringBuilder.append(it)
            }
        }

        return regexStringBuilder.toString().toRegex()
    }

    private fun _switchSigns(text: String, regex: Regex): String? {
        val dReplacedForZero = text.replace('D', '0')
        val oReplacedForZero = dReplacedForZero.replace('O', '0')
        return regex.find(oReplacedForZero)?.value
    }

    companion object {
        private const val LETTER_REGEX = "[a-zA-Z]"
        private const val DIGIT_REGEX = "\\d"
    }
}