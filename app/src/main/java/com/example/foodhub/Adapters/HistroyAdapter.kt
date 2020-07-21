package com.example.foodhub.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.model.Histroy
import java.util.*
import kotlin.concurrent.fixedRateTimer

class HistroyAdapter(val context:Context,val list:ArrayList<Histroy>):RecyclerView.Adapter<HistroyAdapter.HistroyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistroyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.histroy_single_row,parent,false)
        return HistroyAdapter.HistroyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistroyViewHolder, position: Int) {
        val obj=list[position]

        holder.txtRestName.text=obj.restaurantName
        holder.txtDate.text=obj.orderPlacedAt
        val adapter=getAdapter.getHistroyListAdapter(context,obj.foodList )
        holder.recyclerView.adapter=adapter
        holder.recyclerView.layoutManager=LinearLayoutManager(context)

    }


    class HistroyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtRestName:TextView=view.findViewById(R.id.txtRestName)
        val txtDate:TextView=view.findViewById(R.id.txtDate)
        val recyclerView:RecyclerView= view.findViewById(R.id.recyclerView)

    }
}