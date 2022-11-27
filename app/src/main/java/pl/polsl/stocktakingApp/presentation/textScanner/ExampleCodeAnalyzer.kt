package pl.polsl.stocktakingApp.presentation.textScanner

class ExampleCodeAnalyzer {
    private var _exampleCode: String? = null

    fun setExampleCode(code: String) {
        _exampleCode = code
    }

    fun analyzeCode(code: String): String? {
        if (_exampleCode == null) {
            return null
        }

        if (code.length < _exampleCode!!.length) {
            return null
        }
        return null


//        code.forEach {
////            if(!it.isLetter() || !it.isDigit()){
////
////            }
//            if()
//        }
    }
}