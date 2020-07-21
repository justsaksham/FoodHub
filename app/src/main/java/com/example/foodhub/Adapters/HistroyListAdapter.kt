package com.example.foodhub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.CartEntity
import com.example.foodhub.model.Food
import com.example.foodhub.model.FoodList

class HistroyListAdapter(val context: Context, val list:ArrayList<FoodList>): RecyclerView.Adapter<HistroyListAdapter.HistroyListViewHolder>(){
    var i:Int=list.size
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistroyListAdapter.HistroyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item_single_row,parent,false)
        return HistroyListAdapter.HistroyListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistroyListAdapter.HistroyListViewHolder, position: Int) {
        val food=list[position] as FoodList
        holder.txtDishCost.text=food.foodCost
        holder.txtDishName.text=food.foodName
    }
        class HistroyListViewHolder(view: View):RecyclerView.ViewHolder(view){

            val txtDishName: TextView =view.findViewById(R.id.txtDishName)
            val txtDishCost: TextView =view.findViewById(R.id.txtDishCost)

        }
}