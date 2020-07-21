package com.example.foodhub.DBAsync

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.foodhub.database.RestDatabase
import com.example.foodhub.database.RestEntity

class DBAsyncTaskRestList(context: Context,val restEntity:RestEntity, val mode:Int):AsyncTask<Void,Void,Boolean>() {
    val db= Room.databaseBuilder(context,RestDatabase::class.java,"carts_db").build()
    override fun doInBackground(vararg params: Void?): Boolean {
        when(mode){
            1->{
                val rest:RestEntity=db.restDao().getFoodById(restEntity.rest_id.toString())
                db.close()
                return rest!=null
            }
            2->{
                db.restDao().insertRest(restEntity)
                db.close()
                return true
            }
            3->{
                db.restDao().deleteRest(restEntity)
                db.close()
                return true
            }
            4 ->{
                db.restDao().deleteAll()
                db.close()
                return true
            }
        }
        return false
    }
}