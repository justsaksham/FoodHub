package com.example.foodhub.DBAsync

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.foodhub.database.CartDatabase
import com.example.foodhub.database.RestDatabase
import com.example.foodhub.database.RestEntity
import com.example.foodhub.model.Restruant

class RetreiveRestList(val context: Context,val nameOfDatabase:String): AsyncTask<Void, Void, List<RestEntity>>(){
    override fun doInBackground(vararg params: Void?): List<RestEntity> {
        val db= Room.databaseBuilder(context, RestDatabase::class.java,nameOfDatabase).build()
        return db.restDao().display() as ArrayList<RestEntity>
    }

}