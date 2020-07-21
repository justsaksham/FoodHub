package com.example.foodhub.Acitivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodhub.R

class OrderConfirm : AppCompatActivity() {
    lateinit var btnConfirm: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)
        btnConfirm=findViewById(R.id.btnConfirm)
        btnConfirm.setOnClickListener{
            val intent=Intent(this@OrderConfirm,Welcome::class.java)
            startActivity(intent)
            finishAffinity()
            //delete all paused activity clears the backstack
          //  finish()

        }
    }

    override fun onBackPressed() {
        val intent=Intent(this@OrderConfirm,Welcome::class.java)
        startActivity(intent)
        finishAffinity()
        super.onBackPressed()
    }
}
