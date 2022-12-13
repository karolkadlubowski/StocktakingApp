package pl.polsl.stocktakingApp.domain.usecase

import pl.polsl.printer.BluetoothService
import pl.polsl.printer.LabelLineDividerService
import pl.polsl.printer.Result
import pl.polsl.stocktakingApp.data.models.StocktakingObject
import pl.polsl.stocktakingApp.domain.UseCase
import pl.polsl.stocktakingApp.presentation.configuration.CodeType

interface PrintLabel : UseCase<PrintLabel.Params, Result> {
    data class Params(
        val deviceAddress: String,
        val stocktakingObject: StocktakingObject,
        val codeType: CodeType
    )
}

internal class PrintLabelImpl(
    private val bluetoothService: BluetoothService,
    private val labelLineDividerService: LabelLineDividerService
) : PrintLabel {
    override fun invoke(input: PrintLabel.Params): Result {
        val data = input.stocktakingObject
        val name = data.name.trim()
        val code = data.barcode

        val label: String
        if (name.length <= 30) {
            val codeType =
                if (data.barcode.length < 19 && input.codeType == CodeType.Barcode) "^BY3,3,240^FO80,160^BC^FD$code^FS"
                else "^BY3,3,5^FO300,160^BQN,2,10^FDQA,$code^FS"

            label = buildString {
                append("^XA\n")
                append("^CI28\n")
                append("^CF0,35\n")
                append("^FO80,70^FDNazwa: ^FS\n")
                append("^CF0,35\n")
                append("^FO200,70^FD$name^FS\n")
                append("^CF0,25\n")
                append("^FO80,120^FDKod:^FS\n")
                append("^FO200,120^FD$code^FS\n")
                append("^FO80,150^GB680,3,3^FS\n")
                append(codeType)
                append("^XZ")
            }
        } else {
            val dividedName = labelLineDividerService.divideString(name)
            val nameFirstPart = dividedName[0]
            val nameSecondPart = dividedName[1]

            val codeType =
                if (data.barcode.length < 19 && input.codeType == CodeType.Barcode) "^BY3,3,240^FO80,200^BC^FD$code^FS"
                else "^BY3,3,5^FO300,200^BQN,2,10^FDQA,$code^FS"

            label = buildString {
                append("^XA\n")
                append("^CI28\n")
                append("^CF0,35\n")
                append("^FO80,70^FDNazwa: ^FS\n")
                append("^CF0,35\n")
                append("^FO200,70^FD$nameFirstPart^FS\n")
                append("^FO200,110^FD$nameSecondPart^FS\n")
                append("^CF0,25\n")
                append("^FO80,160^FDKod:^FS\n")
                append("^FO200,160^FD$code^FS\n")
                append("^FO80,190^GB680,3,3^FS\n")
                append(codeType)
                append("^XZ\n")
            }
        }
        return bluetoothService.print(input.deviceAddress, label)
    }
}