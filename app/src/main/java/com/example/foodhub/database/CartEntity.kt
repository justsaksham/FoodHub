package com.example.foodhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Carts")
data  class CartEntity(
    @NotNull @PrimaryKey val food_id:Int,
    @ColumnInfo(name="food_name" )val foodName:String,
    @ColumnInfo(name="food_cost" )val foodCost:String,
    @ColumnInfo(name="rest_id" )val restId:String
)
