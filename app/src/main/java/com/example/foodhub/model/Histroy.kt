package com.example.foodhub.model

data class Histroy(
    val orderId:String
    ,val restaurantName:String,
    val totalCost:String,
    val orderPlacedAt:String,
    val foodList:ArrayList<FoodList>
)