package com.example.foodhub.DBAsync

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.foodhub.database.CartDatabase
import com.example.foodhub.database.CartEntity
import java.util.*

class DBAsyncTaskFoodList(val context: Context,val cartEntity: CartEntity?,val mode:Int ) :AsyncTask<Void,Void,Boolean>(){
    val db= Room.databaseBuilder(context,CartDatabase::class.java,"carts-db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        when(mode){
            1->{
                val food:CartEntity=db.cartDao().getFoodById(cartEntity?.food_id.toString())
                db.close()
                if(food!=null)
                    return true
                else false
            }
            2->{
                db.cartDao().insertFood(cartEntity)
                db.close()
                return true
            }
            3->{
                db.cartDao().deleteFood(cartEntity)
                db.close()
                return true
            }
            4 ->{
                db.cartDao().deleteAll()
                db.close()
                return true
            }
        }
        return false
    }
}