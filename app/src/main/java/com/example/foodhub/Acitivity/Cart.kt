package com.example.foodhub.Acitivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.Adapters.CartAdapter
import com.example.foodhub.Adapters.ItemsRecyclerAdapter
import com.example.foodhub.DBAsync.Retreive
import com.example.foodhub.R
import com.example.foodhub.database.CartDatabase
import com.example.foodhub.database.CartEntity
import com.example.foodhub.database.RestEntity
import com.example.foodhub.model.Food
import com.example.foodhub.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Cart : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var recyclerItems: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var cartAdapter: CartAdapter
    lateinit var btnOrder:Button
    lateinit var txtRestName:TextView
    lateinit var list:List<Object?>
  //  lateinit var btnProceedCart: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
      toolbar=findViewById(R.id.toolbar)
      layoutManager= LinearLayoutManager(this@Cart)
      recyclerItems=findViewById(R.id.recyclerView)
      btnOrder=findViewById(R.id.btnOrder)
      txtRestName=findViewById(R.id.txtRestName)


      val rest=intent.getParcelableExtra<RestEntity>("objectRestEntity")

      txtRestName.text="${txtRestName.text} ${rest.restName}"

      setupToolbar()
      //for retrieving the data
      val async= Retreive(this@Cart,"carts-db").execute()
      var list=async.get()
      val cartAdapter=CartAdapter(this@Cart,list)
      recyclerItems.adapter=cartAdapter
      recyclerItems.layoutManager=layoutManager










      btnOrder.setOnClickListener {
            if(ConnectionManager().checkConnectivity(this@Cart)) {
            var sum:Int = 0
            val json=JSONObject()
            val jsonArray=JSONArray()
            var j=0
            var rest_id:String="ss"
            for(i in list){
                val items=i as CartEntity?
                val jsonObject=JSONObject()
                jsonObject.put("food_item_id",items?.food_id)
                jsonArray.put(j,jsonObject)
                j++;
                rest_id=items!!.restId
                val s:String?=(items?.foodCost)
                sum=sum + s!!.toInt()
            }
            val sharedPreferences=getSharedPreferences("FoodHub", Context.MODE_PRIVATE)
            val u_id=sharedPreferences.getString("user_id","sak")
            json.put("user_id",u_id)
            json.put("restaurant_id",rest_id)
            json.put("total_cost",sum.toString())
            json.put("food",jsonArray)
            btnOrder.text="total value($sum)"







            val queue = Volley.newRequestQueue(this@Cart)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            try {
                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.POST, url, json, Response.Listener {

                        val data=it.getJSONObject("data")
                        val success=data.getBoolean("success")
                        if(success){
                            Toast.makeText(this@Cart,"$it",Toast.LENGTH_LONG).show()
                            val intent = Intent(this@Cart, OrderConfirm::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this@Cart,"some error occured $it",Toast.LENGTH_LONG).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@Cart,
                            "some unExcepted Error occured!!! $it",
                            Toast.LENGTH_LONG
                        ).show()
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


            } catch (e: Exception) {
                    if(this@Cart!=null){
                        Toast.makeText(this@Cart,"some error occured",Toast.LENGTH_LONG).show()
                    }
            }
        }else{
            //dailog lagana h
            val dialog  = AlertDialog.Builder(this@Cart)
            dialog.setTitle("error")
            dialog.setMessage("Internet Connection  is not found")
            dialog.setPositiveButton("Open Settings"){
                    text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){
                    text,listener ->
                ActivityCompat.finishAffinity(this@Cart)

            }
            dialog.create()
            dialog.show()

        }
      }
    }
    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        //this will enable the home buttton make it active
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
