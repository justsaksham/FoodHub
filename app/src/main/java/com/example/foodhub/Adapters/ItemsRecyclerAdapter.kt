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
import com.example.foodhub.DBAsync.GetDBA
import com.example.foodhub.R
import com.example.foodhub.database.CartDatabase
import com.example.foodhub.database.CartEntity
import com.example.foodhub.model.Food

class ItemsRecyclerAdapter(val context:Context,val ItemList:ArrayList<Food>,val b:View):RecyclerView.Adapter<ItemsRecyclerAdapter.ItemViewHolder>() {
   val listOfCartItem= arrayListOf<Food>()
    val listOfItemId= arrayListOf<String>()


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
        holder.txtDishCost.text ="Rs. ${food.foodCost}"
        val value: String = food.foodId
        val cartEntity=CartEntity(
            food.foodId.toInt(),
            food.foodName.toString()
            ,food.foodCost.toString()
            ,food.RestId.toString()
        )


        //checking they are added in cart or not
        val async=GetDBA.getDBAsyncTaskFoodList(context,cartEntity,1).execute()
        val success2=async.get()
        if(success2){
            val favColor = ContextCompat.getColor(context, R.color.btnchanged)
            holder.btnAdd.setBackgroundColor(favColor)
            holder.btnAdd.text = "Remove"
        }
        else{
            val favColor = ContextCompat.getColor(context, R.color.appColor)
            holder.btnAdd.setBackgroundColor(favColor)
            holder.btnAdd.text = "Add"
        }
        holder.btnAdd.setOnClickListener {

            //val b:View=context.findViewById(R.id.btnProceedCart)

            val async=GetDBA.getDBAsyncTaskFoodList(context,cartEntity,1).execute()
            val success=async.get()
           // Toast.makeText(context, "clicked $success", Toast.LENGTH_LONG).show()
            if (!success) {
                listOfCartItem.add(food)
                listOfItemId.add(value)
                val async=GetDBA.getDBAsyncTaskFoodList(context,cartEntity,2).execute()
                val success1=async.get()
                if(success1) {
                    val favColor = ContextCompat.getColor(context, R.color.btnchanged)
                    holder.btnAdd.setBackgroundColor(favColor)
                    holder.btnAdd.text = "Remove"
                  //  Toast.makeText(context, " Added in cart", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context, " some unexpected error", Toast.LENGTH_SHORT).show()
                }
            } else {
                listOfCartItem.remove(food)
                listOfItemId.remove(value)
                val async=GetDBA.getDBAsyncTaskFoodList(context,cartEntity,3).execute()
                val success1=async.get()
                if(success1) {
                    val favColor = ContextCompat.getColor(context, R.color.appColor)
                    holder.btnAdd.setBackgroundColor(favColor)
                    Toast.makeText(context, " Removed From cart", Toast.LENGTH_SHORT).show()
                    holder.btnAdd.text = "Add"
                }else{
                    Toast.makeText(context, "Some unexpected error", Toast.LENGTH_LONG).show()
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
}