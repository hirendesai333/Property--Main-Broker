package com.illopen.agent.model

import android.os.Parcel
import android.os.Parcelable

data class PropertyListModel(
    val name: String?,
    var jobStatus: Int? = 0,
    var isBidAdded: Int? = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(jobStatus)
        parcel.writeValue(isBidAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyListModel> {
        override fun createFromParcel(parcel: Parcel): PropertyListModel {
            return PropertyListModel(parcel)
        }

        override fun newArray(size: Int): Array<PropertyListModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class PropertyImageModel(
    var image: String
)