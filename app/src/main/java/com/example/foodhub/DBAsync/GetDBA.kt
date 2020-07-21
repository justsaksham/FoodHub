package com.example.foodhub.DBAsync

import android.content.Context
import com.example.foodhub.database.CartEntity
import com.example.foodhub.database.RestEntity
//for apply loosely coupling concept
// we can easily change the functionality and
//provide new implementation object
//
class GetDBA {
    companion object{
        fun getDBAsyncTaskFoodList(context: Context,cartEntity: CartEntity?,mode:Int):DBAsyncTaskFoodList{
            return DBAsyncTaskFoodList(context,cartEntity,mode)
        }
        fun getDBAsyncTaskRestList(context: Context, restEntity: RestEntity, mode:Int):DBAsyncTaskRestList{
            return DBAsyncTaskRestList(context,restEntity,mode)
        }
        fun getRetreive(context: Context,DbName:String):Retreive{
            return Retreive(context,DbName)
        }
        fun getRetreiveRestList (context: Context,DbName:String):RetreiveRestList
        {
            return RetreiveRestList(context,DbName)
        }
    }
}