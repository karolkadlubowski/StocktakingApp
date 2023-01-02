package pl.polsl.stocktakingApp.presentation.common

import android.content.Context
import androidx.annotation.StringRes
import pl.polsl.stocktakingApp.R

open class Event {
    open class Message(@StringRes private val textId: Int) : Event() {
        fun text(context: Context): String = context.getString(textId)
    }

    object NoSelectedPrinter : Event()
    object BluetoothError : Message(R.string.bluetoothEnablingError)
}

