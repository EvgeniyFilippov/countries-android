package com.example.course_android

import android.os.Parcel
import android.os.Parcelable

class BaseParcelable() : Parcelable {

    lateinit var value: Any

    constructor(value: Any) : this() {
        this.value = value
    }

    constructor(parcel: Parcel) : this() {
        this.value = Any()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseParcelable> {
        override fun createFromParcel(parcel: Parcel): BaseParcelable {
            return BaseParcelable(parcel)
        }

        override fun newArray(size: Int): Array<BaseParcelable?> {
            return arrayOfNulls(size)
        }
    }
}