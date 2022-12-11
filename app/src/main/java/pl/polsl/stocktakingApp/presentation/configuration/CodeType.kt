package pl.polsl.stocktakingApp.presentation.configuration

import androidx.annotation.StringRes
import pl.polsl.stocktakingApp.R

enum class CodeType(@StringRes val stringId: Int) {
    Barcode(R.string.barcodeLabel),
    QR(R.string.qrLabel)
}
