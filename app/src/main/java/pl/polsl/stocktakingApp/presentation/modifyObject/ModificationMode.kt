package pl.polsl.stocktakingApp.presentation.modifyObject

import androidx.annotation.StringRes
import pl.polsl.stocktakingApp.R

sealed class ModificationMode(@StringRes val pageTitleId: Int) {
    object EditMode : ModificationMode(R.string.editObject)
    object AddMode : ModificationMode(R.string.addObject)
}
