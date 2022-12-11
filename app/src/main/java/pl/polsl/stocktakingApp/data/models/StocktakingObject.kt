package pl.polsl.stocktakingApp.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StocktakingObject(
    @PrimaryKey
    val id: Int? = null,
    var name: String = "",
    var description: String = "",
    var amount: Int = 1,
    var barcode: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(amount)
        parcel.writeString(barcode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StocktakingObject> {
        override fun createFromParcel(parcel: Parcel): StocktakingObject {
            return StocktakingObject(parcel)
        }

        override fun newArray(size: Int): Array<StocktakingObject?> {
            return arrayOfNulls(size)
        }

        //val default = StocktakingObject()
    }

    //constructor(i: Int, s: String, s1: String, s2: String, i1: Int, s3: String) : this(i,s,s1,s2,i1,s3)
}
