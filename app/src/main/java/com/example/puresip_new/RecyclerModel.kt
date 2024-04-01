package com.example.puresip_new

import android.os.Parcel
import android.os.Parcelable

data class RecyclerModel(
    val date: String = "",
    val alertMessage: String = "",
    val title: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(alertMessage)
        parcel.writeString(title)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecyclerModel> {
        override fun createFromParcel(parcel: Parcel): RecyclerModel {
            return RecyclerModel(parcel)
        }

        override fun newArray(size: Int): Array<RecyclerModel?> {
            return arrayOfNulls(size)
        }
    }
}
