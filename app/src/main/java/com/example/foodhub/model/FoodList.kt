package com.example.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodList(
    val foodId:String,
    val foodName:String,
    val foodCost:String
): Parcelable
