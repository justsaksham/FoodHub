package com.example.foodhub.Acitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import android.widget.Toolbar

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.Adapters.ItemsRecyclerAdapter
import com.example.foodhub.Adapters.getAdapter
import com.example.foodhub.DBAsync.DBAsyncTaskFoodList
import com.example.foodhub.DBAsync.DBAsyncTaskRestList
import com.example.foodhub.DBAsync.GetDBA
import com.example.foodhub.Fragment.FavouriteFragment
import com.example.foodhub.Fragment.HomeFragment
import com.example.foodhub.R
import com.example.foodhub.database.RestEntity
import com.example.foodhub.model.Food
import com.example.foodhub.model.Restruant
import java.io.Serializable
import java.lang.Exception

class FoodItemList : AppCompatActivity() {
    val listOfCartItem= arrayListOf<Food>()//ye last mei add kiya h
    val listOfItemId= arrayListOf<String>()////ye last mei add kiya h
    val itemList= arrayListOf<Food>()
    lateinit var toolbar: Toolbar
    lateinit var recyclerItems: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var itemsRecyclerAdapter: ItemsRecyclerAdapter
    lateinit var favNotRes1:TextView
    lateinit var favNotRes:TextView
    lateinit var btnProceedCart:Button//ye last mei add kiya h
    lateinit var ActvityName:String
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_item_list)

        val h=GetDBA.getDBAsyncTaskFoodList(this@FoodItemList,null,4).execute().get()

        toolbar=findViewById(R.id.toolbar)
        layoutManager=LinearLayoutManager(this@FoodItemList)
        recyclerItems=findViewById(R.id.recyclerView)
        btnProceedCart=findViewById(R.id.btnProceedCart)
        favNotRes=findViewById(R.id.favNotRes)
        favNotRes1=findViewById(R.id.favNotRes1)

        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)

        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE





        val b:View=findViewById(R.id.btnProceedCart)
            b.visibility=View.GONE
        val id=intent.getStringExtra("id")
        val nameOfRes:String?=intent.getStringExtra("nameOfRes")

        val rest=intent.getParcelableExtra<RestEntity>("obj")



//        ActvityName=intent.getStringExtra("ActvityName")




        Toast.makeText(this@FoodItemList,"saksham is $rest",Toast.LENGTH_LONG).show()
        setupToolbar()
        supportActionBar?.title=nameOfRes

        favNotRes1.visibility=View.GONE


        val async= GetDBA.getDBAsyncTaskRestList(this@FoodItemList,rest,1).execute().get()

        if(async) {
            favNotRes1.visibility = View.VISIBLE
        }
        else{
            favNotRes1.visibility=View.GONE
        }


        favNotRes.setOnClickListener{
            if(!GetDBA.getDBAsyncTaskRestList(this@FoodItemList,rest,1).execute().get()){
                val insert=GetDBA.getDBAsyncTaskRestList(this@FoodItemList,rest,2).execute().get()
                if(insert){
                    favNotRes1.visibility = View.VISIBLE
                }
                else{
                    Toast.makeText(this@FoodItemList,"some error occured",Toast.LENGTH_LONG).show()
                }

            }
            else{
                val delete=GetDBA.getDBAsyncTaskRestList(this@FoodItemList,rest,3).execute().get()
                if(delete){
                    favNotRes1.visibility = View.GONE
                }
                else{
                    Toast.makeText(this@FoodItemList,"some error occured",Toast.LENGTH_LONG).show()
                }
            }
        }

        //     Toast.makeText(this@FoodItemList,"$id",Toast.LENGTH_LONG).show()
        val queue= Volley.newRequestQueue(this@FoodItemList)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/$id"
        try {
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null, Response.Listener {
                    val itemsOfRes = it.getJSONObject("data")
                    val success = itemsOfRes.getBoolean("success")
                    if (success) {


                        progressBar.visibility=View.GONE
                        progressLayout.visibility=View.GONE


                        val data = itemsOfRes.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val items = data.getJSONObject(i)
                            val food = Food(
                                items.getString("id"),
                                items.getString("name"),
                                items.getString("cost_for_one"),
                                items.getString("restaurant_id")
                            )
                            itemList.add(food)

                            //add adapters
                            itemsRecyclerAdapter = getAdapter.getItemsRecyclerAdapter(
                                this@FoodItemList,
                                itemList,
                                b
                                )
                            recyclerItems.adapter = itemsRecyclerAdapter
                            recyclerItems.layoutManager = layoutManager
                           // Toast.makeText(this@FoodItemList, "$food", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(
                            this@FoodItemList,
                            "some unexpected error occured",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@FoodItemList,
                        "Volley error occured!!! $it",
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
        }
        catch(e:Exception){
            if(this@FoodItemList!=null){
                Toast.makeText(this@FoodItemList,"some error occured",Toast.LENGTH_LONG).show()
            }
            //Toast.makeText(this@FoodItemList,"error ",Toast.LENGTH_LONG).show()
        }

        btnProceedCart.setOnClickListener {
            Toast.makeText(this@FoodItemList,"cart ",Toast.LENGTH_LONG).show()
            val intent=Intent(this@FoodItemList,Cart::class.java)
            intent.putExtra("objectRestEntity",rest)
            startActivity(intent)
        }



    }
    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        //this will enable the home buttton make it active
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean{
        val id=item.itemId
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//       // val h=ItemsRecyclerAdapter.DBAsyncTask(this@FoodItemList,null,4).execute().get()
//
//    }

    override fun onBackPressed() {
        val h=GetDBA.getDBAsyncTaskFoodList(this@FoodItemList,null,4).execute().get()
        finish()
        super.onBackPressed()
    }
}
