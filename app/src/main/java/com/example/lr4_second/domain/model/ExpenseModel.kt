package com.example.lr4_second.domain.model

import android.os.Parcel
import android.os.Parcelable

class ExpenseModel(var expid: Int?, var expName: String, var expValue: String): Parcelable {
    var id: Int? = expid
    var name: String = expName
    var expenseValue: String = expValue

    fun isEmpty(): Boolean
    {
        if (expName.isBlank() || expValue.isBlank()) return true
        else return false
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(expName)
        parcel.writeString(expValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpenseModel> {
        override fun createFromParcel(parcel: Parcel): ExpenseModel {
            return ExpenseModel(parcel)
        }

        override fun newArray(size: Int): Array<ExpenseModel?> {
            return arrayOfNulls(size)
        }
    }
}