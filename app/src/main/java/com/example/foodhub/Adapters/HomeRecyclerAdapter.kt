package com.example.foodhub.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.Acitivity.FoodItemList
import com.example.foodhub.Acitivity.MainActivity
import com.example.foodhub.Acitivity.Welcome
import com.example.foodhub.DBAsync.DBAsyncTaskRestList
import com.example.foodhub.Fragment.FavouriteFragment
import com.example.foodhub.Fragment.HomeFragment
import com.example.foodhub.R
import com.example.foodhub.database.RestEntity
import com.example.foodhub.model.Restruant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_single_row.view.*

class HomeRecyclerAdapter(val context:Context,val ResList:ArrayList<Restruant>) :RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ResList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val rest=ResList[position]
        holder.txtResName.text=rest.resName
        holder.txtPriceRes.text=rest.resCost
        holder.txtRatingRes.text=rest.resRating
        Picasso.get().load(rest.resImage).error(R.drawable.logo).into(holder.imgRes)
        val restName=RestEntity(
            rest.resId.toInt(),
            rest.resName,
            rest.resCost,
            rest.resRating
            ,rest.resImage
        )
        holder.llRest.setOnClickListener {
         //   notifyItemChanged(position,holder.favNotRes1)
            Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(context, FoodItemList::class.java)
            intent.putExtra("id", rest.resId)
            intent.putExtra("nameOfRes", rest.resName)
            intent.putExtra("obj", restName)

//            val j=activityyyy.componentName.className
//            intent.putExtra("ActvityName",j)
           context.startActivity(intent)
//            activityyyy.finish()



        }
//          notifyDataSetChanged()


        holder.favNotRes1.visibility=View.GONE

     val async=DBAsyncTaskRestList(context,restName,1).execute().get()

        if(async) {
            holder.favNotRes1.visibility = View.VISIBLE
        }
        else{
            holder.favNotRes1.visibility=View.GONE
        }

        holder.favNotRes.setOnClickListener{


//            var k=(act as Welcome).supportFragmentManager.findFragmentById(R.id.frameLayout)
//            Toast.makeText(context,"vghchgcgc $k",Toast.LENGTH_LONG).show()
//            (act as Welcome).supportFragmentManager.beginTransaction().detach(k!!).attach(k!!)
//                .commit()

//            when(k) {
//                is HomeFragment -> {
//                    Toast.makeText(context,"hhh $k",Toast.LENGTH_LONG).show()
//                    (act as Welcome).supportFragmentManager.beginTransaction().detach(k).attach(k)
//                        .commit()
//                }
//
//                else -> {
//                    Toast.makeText(context,"juuun $k",Toast.LENGTH_LONG).show()
//                    (act as Welcome).supportFragmentManager.beginTransaction().detach(k).attach(k)
//                        .commit()
//
//                }
//            }





            if(!DBAsyncTaskRestList(context,restName,1).execute().get()){
               val insert=DBAsyncTaskRestList(context,restName,2).execute().get()
               if(insert){
               holder.favNotRes1.visibility = View.VISIBLE
               }
               else{
                   Toast.makeText(context,"some error occured",Toast.LENGTH_LONG).show()
               }

           }
            else{
               val delete=DBAsyncTaskRestList(context,restName,3).execute().get()
               if(delete){
                   holder.favNotRes1.visibility = View.GONE
               }
               else{
                   Toast.makeText(context,"some error occured",Toast.LENGTH_LONG).show()
               }
           }
        }
    }

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val favNotRes:TextView=view.findViewById(R.id.favNotRes)
        val favNotRes1:TextView=view.findViewById(R.id.favNotRes1)
        val llRest:LinearLayout=view.findViewById(R.id.llRest)
        val imgRes:ImageView=view.findViewById(R.id.imgRes)
        val txtResName:TextView=view.findViewById(R.id.txtResName)
        val txtPriceRes:TextView=view.findViewById(R.id.txtPriceRes)
        val txtRatingRes:TextView=view.findViewById(R.id.txtRatingRes)
    }
}