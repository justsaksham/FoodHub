package com.example.foodhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface RestDao {
    @Insert
    fun insertRest(restEntity:RestEntity)

    @Delete
    fun deleteRest(resttEntity: RestEntity)

    @Query("SELECT * FROM Rest")
    fun display():List<RestEntity>


    @Query("DELETE From Rest")
    fun deleteAll()


//    @Query("Drop TABLE carts")
//    fun drop():Unit

    @Query("SELECT * FROM Rest WHERE rest_id= :RestId")
    fun getFoodById(RestId:String):RestEntity


}