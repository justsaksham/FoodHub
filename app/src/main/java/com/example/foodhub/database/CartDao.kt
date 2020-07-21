package com.example.foodhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {

    @Insert
    fun insertFood(cartEntity:CartEntity?)

    @Delete
    fun deleteFood(cartEntity: CartEntity?)

    @Query("SELECT * FROM Carts")
    fun display():List<CartEntity?>


    @Query("DELETE From Carts")
    fun deleteAll()


//    @Query("Drop TABLE carts")
//    fun drop():Unit

    @Query("SELECT * FROM Carts WHERE food_id= :FoodId")
    fun getFoodById(FoodId:String):CartEntity

}