package com.example.foodhub.Adapters

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodhub.Acitivity.Cart
import com.example.foodhub.DBAsync.DBAsyncTaskFoodList
import com.example.foodhub.R
import com.example.foodhub.database.CartDatabase
import com.example.foodhub.database.CartEntity
import com.example.foodhub.model.Food

class ItemsRecyclerAdapter(val context:Context,val ItemList:ArrayList<Food>,val b:View,val listOfCartItem:ArrayList<Food>,

                           val listOfItemId:ArrayList<String>):RecyclerView.Adapter<ItemsRecyclerAdapter.ItemViewHolder>() {
  // val listOfCartItem= arrayListOf<Food>()
    //val listOfItemId= arrayListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_food_item_single_row,parent,false)
        return ItemsRecyclerAdapter.ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ItemList.size
        }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val list = arrayListOf<Food>()

        val food = ItemList[position]
        holder.txtDishName.text = food.foodName
        holder.txtDishId.text = food.foodId
        holder.txtDishCost.text = food.foodCost
        val value: String = food.foodId
        val cartEntity=CartEntity(
            food.foodId.toInt(),
            food.foodName.toString()
            ,food.foodCost.toString()
            ,food.RestId.toString()
        )
        val async=DBAsyncTaskFoodList(context,cartEntity,1).execute()
        val success2=async.get()
        if(success2){
            val favColor = ContextCompat.getColor(context, R.color.btnchanged)
            holder.btnAdd.setBackgroundColor(favColor)
            holder.btnAdd.text = "Added"
        }
        else{
            val favColor = ContextCompat.getColor(context, R.color.appColor)
            holder.btnAdd.setBackgroundColor(favColor)
            holder.btnAdd.text = "Add"
        }
        holder.btnAdd.setOnClickListener {

            //val b:View=context.findViewById(R.id.btnProceedCart)

            val async=DBAsyncTaskFoodList(context,cartEntity,1).execute()
            val success=async.get()
            Toast.makeText(context, "clicked $success", Toast.LENGTH_LONG).show()
            if (!success) {
                listOfCartItem.add(food)
                listOfItemId.add(value)
                val async=DBAsyncTaskFoodList(context,cartEntity,2).execute()
                val success1=async.get()
                if(success1) {
                    val favColor = ContextCompat.getColor(context, R.color.btnchanged)
                    holder.btnAdd.setBackgroundColor(favColor)
                    holder.btnAdd.text = "Added"
                   // Toast.makeText(context, "clicked $listOfCartItem", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                }
            } else {
                listOfCartItem.remove(food)
                listOfItemId.remove(value)
                val async=DBAsyncTaskFoodList(context,cartEntity,3).execute()
                val success1=async.get()
                if(success1) {
                    val favColor = ContextCompat.getColor(context, R.color.appColor)
                    holder.btnAdd.setBackgroundColor(favColor)
                    holder.btnAdd.text = "Add"
                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                }
            }
            if (!listOfCartItem.isEmpty())
                b.visibility = View.VISIBLE
            else {
                b.visibility = View.GONE
            }
        }

    }

    class ItemViewHolder(view : View):RecyclerView.ViewHolder(view){
        val txtDishId:TextView=view.findViewById(R.id.txtDishId)
        val txtDishName:TextView=view.findViewById(R.id.txtDishName)
        val txtDishCost:TextView=view.findViewById(R.id.txtDishCost)
        val btnAdd:Button=view.findViewById(R.id.btnAdd)
    }
     fun getter():ArrayList<Food>{
        return listOfCartItem
    }
//    class DBAsyncTask(val context:Context, val CartEntity: CartEntity?,val mode:Int):
//        AsyncTask<Void, Void, Boolean>(){
//        /*
//        mode 1 -> check DB if the book is favourites or not
//        mode 2 -> Save the book into DB as favourites
//        mode 3 -> Remove the Favourite the book
//         */
//        val db= Room.databaseBuilder(context, CartDatabase::class.java,"carts-db").build()
//        override fun doInBackground(vararg params: Void?): Boolean {
//            when(mode){
//                1 ->{
//                    // check DB if the book is favourites or not
//                    val book: CartEntity?= db.cartDao().getFoodById(CartEntity?.food_id.toString())
//                    db.close()
//                    return book!=null
//
//                }
//                2 ->{
//                    //Save the book into DB as favourites
//                    db.cartDao().insertFood(CartEntity)
//                    db.close()
//                    return true
//                }
//                3 ->{
//                    ///Remove the Favourite the book\
//                    db.cartDao().deleteFood(CartEntity)
//                    db.close()
//                    return true
//                }
//                4 ->{
//                    db.cartDao().deleteAll()
//                    db.close()
//                    return true
//              }
//            }
//            return false;
//        }
//
//    }
}