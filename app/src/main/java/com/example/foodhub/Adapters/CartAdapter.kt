package com.example.foodhub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.CartEntity
import java.util.*

class CartAdapter(val context: Context,val list:List<Object?> ):RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item_single_row ,parent,false)
        return CartAdapter.ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
    return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val food=list[position] as CartEntity?
        holder.txtDishCost.text=food?.foodCost
        holder.txtDishName.text=food?.foodName
    }

    class ItemViewHolder(view : View): RecyclerView.ViewHolder(view){
        val txtDishName: TextView =view.findViewById(R.id.txtDishName)
        val txtDishCost: TextView =view.findViewById(R.id.txtDishCost)

    }

}