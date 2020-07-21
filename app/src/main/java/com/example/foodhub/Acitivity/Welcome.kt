package com.example.foodhub.Acitivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
//import android.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.foodhub.Fragment.*
import com.example.foodhub.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.drawer.view.*

//to set toolbaar go in style
class Welcome: AppCompatActivity() {
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var welcomeNavigation: NavigationView
    lateinit var toolbar:Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var txtUserName: TextView
    lateinit var txtUserNumber: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        frameLayout=findViewById(R.id.frameLayout)
        welcomeNavigation=findViewById(R.id.welcomeNavigation)
        drawerLayout=findViewById(R.id.drawerLayout)
        toolbar=findViewById(R.id.toolbar)
        sharedPreferences=getSharedPreferences("FoodHub",Context.MODE_PRIVATE)

        setupToolbar()
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@Welcome,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //its is tooogle
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        var previousMenuItem:MenuItem? = null
        actionBarDrawerToggle.syncState()
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        val drawer=welcomeNavigation.getHeaderView(0)
        txtUserName = drawer.findViewById<TextView>(R.id.txtUserName)
        txtUserNumber=drawer.findViewById<TextView>(R.id.txtUserNumber)
        txtUserName.text=sharedPreferences.getString("name","saksham")
        txtUserNumber.text=sharedPreferences.getString("mobile_number","99979")

        openFragment(HomeFragment(),"Home")
       welcomeNavigation.setCheckedItem(R.id.itemHome)
        welcomeNavigation.setNavigationItemSelectedListener {

            //
            //txtUserName.text=


            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isChecked=true
            it.isCheckable=true
//         //   why to use
            previousMenuItem=it
            when(it.itemId){
                R.id.itemHome -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout
//                    , HomeFragment()).commit()
                    openFragment(HomeFragment(),"Home")

                    drawerLayout.closeDrawers()
                    welcomeNavigation.setCheckedItem(R.id.itemHome)

                }
                R.id.itemFav ->{
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
//                        FavouriteFragment()
//                    ).commit()
                    openFragment(FavouriteFragment(),"Favourites")
                    drawerLayout.closeDrawers()
                }
               R.id.itemFAQ ->{
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
//                        FAQFragment()
//                        ).commit()
                   openFragment(FAQFragment(),"FAQ's")
                    drawerLayout.closeDrawers()
                }
                R.id.itemOrderHistroy ->{
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
//                        HistoryFragment()).commit()
                    val id=sharedPreferences.getString("user_id","kk")
                   // val bundle= bundleOf("user_id" to id)
                    openFragment(HistoryFragment(id!!.toString()),"History")
                    drawerLayout.closeDrawers()
                }
                R.id.itemMyProfile ->{
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
//                        ProfileFragment()).commit()

                    sharedPreferences=getSharedPreferences("FoodHub",Context.MODE_PRIVATE)
                    val name=sharedPreferences.getString("name","kk")
                    val mobileNumber=sharedPreferences.getString("mobile_number","kk")
                    val email=sharedPreferences.getString("email","k")
                    val address=sharedPreferences.getString("address","kk")
                    openFragment(ProfileFragment(name!!,mobileNumber!!,email!!,address!!),"My Profile")
                    drawerLayout.closeDrawers()
                }
                R.id.itemLogout ->{
//                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
//                       LogoutFragment() ).commit()
                   // openFragment(LogoutFragment(),"Logout")
                 //   drawerLayout.closeDrawers()

                    val dialog=AlertDialog.Builder(this@Welcome)

                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to logout?")
                    dialog.setPositiveButton("Yes"){text,listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@Welcome,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                   dialog.setNegativeButton("NO"){text,listener ->
//                       val intent = Intent(this@Welcome,Welcome::class.java)
//                       startActivity(intent)
//                       finish()

                   }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun openFragment(fragment: Fragment, title: CharSequence?) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()
        supportActionBar?.title=title

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
        if(id== android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        var frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is HomeFragment ->{
            openFragment(HomeFragment(),"Home")
                welcomeNavigation.setCheckedItem(R.id.itemHome)
        }
            else -> super.onBackPressed()
        }

    }
    override fun onRestart() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
//        //onStart()
        super.onRestart()
    }
}
