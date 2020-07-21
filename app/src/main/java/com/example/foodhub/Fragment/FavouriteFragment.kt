package com.example.foodhub.Fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.Acitivity.Welcome
import com.example.foodhub.Adapters.HomeRecyclerAdapter
import com.example.foodhub.Adapters.getAdapter
import com.example.foodhub.DBAsync.GetDBA
import com.example.foodhub.DBAsync.Retreive
import com.example.foodhub.DBAsync.RetreiveRestList

import com.example.foodhub.R
import com.example.foodhub.database.RestEntity
import com.example.foodhub.model.Restruant

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment() : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var listRes:List<RestEntity>
    lateinit var listRes2:ArrayList<Restruant>
    lateinit var gone:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerView=view.findViewById(R.id.recyclerView)
        layoutManager= LinearLayoutManager(activity)
        gone=view.findViewById(R.id.gone)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE
        listRes2= arrayListOf()



        //getting values from db
        val async=GetDBA.getRetreiveRestList(activity as Context,"carts_db").execute()
        listRes=async.get()
        if(listRes.isEmpty()){
            gone.visibility=View.VISIBLE
        }
        if(listRes !=null){

            for(i in listRes) {
                gone.visibility=View.GONE
                val rest=Restruant(
                    i.rest_id.toString(),
                    i.restName,
                    i.restRating,
                    i.restCost,
                    i.image
                )
                listRes2.add(rest)
               }
            progressBar.visibility=View.GONE
            progressLayout.visibility=View.GONE

            recyclerAdapter = getAdapter.getHomeRecyclerAdapter(activity as Context, listRes2)
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = layoutManager

//
        }
        else{
            Toast.makeText(activity as Context,"error occured", Toast.LENGTH_LONG).show()
        }

        return view
    }

}
