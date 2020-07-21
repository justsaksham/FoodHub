package com.example.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restruant(val resId:String,
                     val resName:String,
                     val resRating:String,
                     val resCost:String,
                     val resImage:String
                     ): Parcelable