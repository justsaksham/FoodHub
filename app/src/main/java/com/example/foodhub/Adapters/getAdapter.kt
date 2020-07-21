package com.example.foodhub.Adapters

import android.content.Context
import android.view.View
import com.example.foodhub.model.Food
import com.example.foodhub.model.FoodList
import com.example.foodhub.model.Histroy
import com.example.foodhub.model.Restruant
//for apply loosely coupling concept
// we can easily change the functionality and
//provide new implementation object
//
class getAdapter {
  companion object {
      fun getCartAdapter(context: Context, list: List<Object?>): CartAdapter {
          return CartAdapter(context, list)
      }

      fun getHistroyAdapter(context: Context, list: ArrayList<Histroy>): HistroyAdapter {
          return HistroyAdapter(context, list)
      }

      fun getHistroyListAdapter(context: Context, list: ArrayList<FoodList>): HistroyListAdapter {
          return HistroyListAdapter(context, list)
      }

      fun getHomeRecyclerAdapter(
          context: Context,
          list: ArrayList<Restruant>
      ): HomeRecyclerAdapter {
          return HomeRecyclerAdapter(context, list)
      }
      fun getItemsRecyclerAdapter(context:Context,list:ArrayList<Food>,view: View):ItemsRecyclerAdapter{
          return ItemsRecyclerAdapter(context,list,view)
      }
  }
}