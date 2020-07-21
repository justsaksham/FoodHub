package com.example.foodhub.DBAsync

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.foodhub.database.CartDatabase

class Retreive(val context: Context,val nameOfDatabase:String): AsyncTask<Void, Void, List<Object?>>(){
    override fun doInBackground(vararg params: Void?): List<Object?> {
        val db= Room.databaseBuilder(context, CartDatabase::class.java,nameOfDatabase).build()
        return db.cartDao().display() as List<Object?>
    }

}