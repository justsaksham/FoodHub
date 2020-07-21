package com.example.foodhub.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
@Parcelize
@Entity(tableName = "Rest")
data class RestEntity (
    @NotNull @PrimaryKey val rest_id:Int,
    @ColumnInfo(name = "rest_name") val restName:String,
    @ColumnInfo(name = "rest_cost") val restCost:String,
    @ColumnInfo(name = "rest_rating") val restRating:String,
    @ColumnInfo(name = "rest_image") val image:String

): Parcelable