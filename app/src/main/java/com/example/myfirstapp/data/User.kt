package com.example.myfirstapp.data

import android.os.Parcel
import android.os.Parcelable

data class User(
    var uid: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var description: String = "",
    var interest: String = "",
    //avatar
    var avatarId: Int = 0,
    var games: List<Game> = arrayListOf(),
    val achievements: List<Achievement> = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        //avatar
        parcel.readInt(),
        arrayListOf<Game>().apply {
            parcel.readTypedList(this, Game.CREATOR)
        },
        arrayListOf<Achievement>().apply {
            parcel.readTypedList(this, Achievement.CREATOR)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(description)
        parcel.writeString(interest)
        parcel.writeInt(avatarId)
        parcel.writeTypedList(games)
        parcel.writeTypedList(achievements)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
