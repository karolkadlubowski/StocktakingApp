package pl.polsl.stocktakingApp.presentation.addObject

import androidx.annotation.StringRes
import pl.polsl.stocktakingApp.R

sealed class ObjectModificationMode(@StringRes val pageTitle: Int) {
    object EditMode : ObjectModificationMode(R.string.editObject)
    object AddMode : ObjectModificationMode(R.string.addObject)
}
