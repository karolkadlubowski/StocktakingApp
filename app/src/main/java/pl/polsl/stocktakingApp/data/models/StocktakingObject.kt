package pl.polsl.stocktakingApp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StocktakingObject(
    @PrimaryKey
    val id: Int?,
    val name: String,
    val description: String,
    val amount: Int,
    val barcode: String
)
