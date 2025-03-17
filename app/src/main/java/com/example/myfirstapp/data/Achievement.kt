package com.example.myfirstapp.data

import android.os.Parcel
import android.os.Parcelable

data class Achievement(
    val name: String = "",
    val description: String = "",
    var unlocked: Boolean = false,
    val iconResId: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeByte(if (unlocked) 1 else 0)
        parcel.writeInt(iconResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Achievement> {
        override fun createFromParcel(parcel: Parcel): Achievement {
            return Achievement(parcel)
        }

        override fun newArray(size: Int): Array<Achievement?> {
            return arrayOfNulls(size)
        }
    }
}
