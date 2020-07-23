package com.example.foodhub.Fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.Adapters.HistroyAdapter
import com.example.foodhub.Adapters.getAdapter

import com.example.foodhub.R
import com.example.foodhub.model.FoodList
import com.example.foodhub.model.Histroy
import com.example.foodhub.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment(val id:String) : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var histroyAdapter:HistroyAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var gone: TextView
    lateinit var list:ArrayList<Histroy>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        list= arrayListOf()
        gone=view.findViewById(R.id.gone)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE



        if(ConnectionManager().checkConnectivity(activity as Context)){
            try{
                val queue=Volley.newRequestQueue(activity as Context)
                val url="http://13.235.250.119/v2/orders/fetch_result/$id"

                val jsonObjectRequest=object:JsonObjectRequest(
                    Request.Method.GET,url,null, Response.Listener {

                        val data=it.getJSONObject("data")


                        val success=data.getBoolean("success")


                        if(success){
                            val dataArray=data.getJSONArray("data")
                            progressBar.visibility=View.GONE
                            progressLayout.visibility=View.GONE

                            for(i in 0  until dataArray.length()){

                                gone.visibility=View.GONE
                                val obj=dataArray.getJSONObject(i)
                                val food=obj.getJSONArray("food_items")
                                val foodList:ArrayList<FoodList> = arrayListOf()


                               for(j in 0 until food.length()){
                                   var fooditem=food.getJSONObject(j)


                                   var foodvalue=FoodList(
                                       fooditem.getString("food_item_id"),
                                       fooditem.getString("name"),
                                       fooditem.getString("cost")
                                   )
                                   foodList.add(foodvalue)
                               }
                                val histroy=Histroy(
                                    obj.getString("order_id"),
                                    obj.getString("restaurant_name"),
                                    obj.getString("total_cost"),
                                    obj.getString("order_placed_at"),
                                    foodList


                                )
                                list.add(histroy)
                                layoutManager =LinearLayoutManager(activity)
                                histroyAdapter=getAdapter.getHistroyAdapter(activity as Context,list)
//                               val dec=recyclerView.addItemDecoration(DividerItemDecoration
//                                   (
//                                   recyclerView.context,
//                                   (layoutManager as LinearLayoutManager)
//                                       .orientation
//                               )
//                               )

                                recyclerView.adapter=histroyAdapter
                                recyclerView.layoutManager=LinearLayoutManager(activity)

                            }
                        }
                        else{
                            Toast.makeText(context,"some unexpected error occured",Toast.LENGTH_LONG).show()
                        }
                    },Response.ErrorListener {
                        if(activity as Context!=null)
                        Toast.makeText(context,"server error occured",Toast.LENGTH_LONG).show()
                    }
                ){

                    override fun getHeaders(): MutableMap<String, String> {
                        var headers = HashMap<String, String>()
                        headers["Content_type"] = "application/json"
                        headers["token"] = "878041adce9bab"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }catch(e:Exception){

                    Toast.makeText(activity as Context, " some unexpected error ", Toast.LENGTH_LONG).show()

            }

        }
        else{
            //dialog box
            val dialog  = AlertDialog.Builder(activity as Context)
            dialog.setTitle("error")
            dialog.setMessage("internet conn is not found")
            dialog.setPositiveButton("open Settings"){
                    text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){
                    text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()

        }

        return view
    }


}
