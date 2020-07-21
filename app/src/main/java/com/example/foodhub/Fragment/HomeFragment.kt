package com.example.foodhub.Fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.Adapters.HomeRecyclerAdapter
import com.example.foodhub.Adapters.getAdapter

import com.example.foodhub.R
import com.example.foodhub.model.Restruant
import com.example.foodhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment() : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    val listRes= arrayListOf<Restruant>()

    var ratingComparator = Comparator<Restruant>{rest1,rest2 ->
        if(rest1.resRating.compareTo(rest2.resRating,true)==0){
            rest1.resName.compareTo(rest2.resName,true)
        }else{
            rest1.resRating.compareTo(rest2.resRating,true)
        }

    }
    var CostComparator = Comparator<Restruant>{rest1,rest2 ->
        if(rest1.resCost.compareTo(rest2.resCost,true)==0){
            rest1.resName.compareTo(rest2.resName,true)
        }else{
            rest1.resCost.compareTo(rest2.resCost,true)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_home, container, false)

        recyclerHome=view.findViewById(R.id.recyclerView)

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE
        layoutManager=LinearLayoutManager(activity)

        setHasOptionsMenu(true)
        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    print("response is $it")

                   try {

                       var dataOfRes=it.getJSONObject("data")
                        val success = dataOfRes.getBoolean("success")
                        if (success)
                        {
                            progressBar.visibility=View.GONE
                            progressLayout.visibility=View.GONE

                            val data = dataOfRes.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val jsonResObject = data.getJSONObject(i)
                                val restruantobj = Restruant(
                                    jsonResObject.getString("id"),
                                    jsonResObject.getString("name"),
                                    jsonResObject.getString("rating"),
                                    jsonResObject.getString("cost_for_one"),
                                    jsonResObject.getString("image_url")
                                )
                                listRes.add(restruantobj)
                                recyclerAdapter= getAdapter.getHomeRecyclerAdapter(activity as Context,listRes)
                                recyclerHome.adapter=recyclerAdapter
                                recyclerHome.layoutManager=layoutManager
//                                Toast.makeText(
//                                    activity as Context,
//                                    "hello response recieved $it",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                   add adapters
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "some error Occured!!!!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                       if(activity as Context!=null)
                       Toast.makeText(activity as Context,"some unexpected error occure $e",Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener {
                    print("response is $it")
                    Toast.makeText(activity as Context, "errors $it", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    var headers = HashMap<String, String>()
                    headers["Content_type"] = "application/json"
                    headers["token"] = "878041adce9bab"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }
        else{
            // Toast.makeText(activity as Context,"some error occurred!!!",Toast.LENGTH_SHORT).show()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.home_menu,menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id=item?.itemId
        if(id==R.id.itemRating){
            Collections.sort(listRes,ratingComparator)
            Collections.reverse(listRes)
        }
        else if(id==R.id.itemCostLowHigh){
            Collections.sort(listRes,CostComparator)
        }
        else if(id==R.id.itemCostHighLow){
            Collections.sort(listRes,CostComparator)
            Collections.reverse(listRes)
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {

        super.onDestroyView()
    }

}
