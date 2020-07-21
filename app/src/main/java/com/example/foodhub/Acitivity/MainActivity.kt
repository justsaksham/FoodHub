package com.example.foodhub.Acitivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.foodhub.R
import com.example.foodhub.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var etUserNumber:EditText
    lateinit var etUserPassword: EditText
    lateinit var btnlogin:Button
    lateinit var txtFgtPassword:TextView
    lateinit var txtRegister:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etUserNumber=findViewById(R.id.etUserNumber)
        etUserPassword=findViewById(R.id.etUserPassword)
        btnlogin=findViewById(R.id.btnlogin)
        txtFgtPassword=findViewById(R.id.txtFgtPassword)
        txtRegister=findViewById(R.id.txtRegister)

        sharedPreferences=getSharedPreferences("FoodHub",Context.MODE_PRIVATE)

        var validator : AwesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        /// for user number
        validator.addValidation(this,R.id.etUserNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_number)

        //for passsword
        validator.addValidation(this,R.id.etUserPassword,".{4,}",R.string.invalid_password)
        // validation



        val bol=sharedPreferences.getBoolean("logedIn",false)
        if(bol){
            val intent = Intent(this@MainActivity, Welcome::class.java)
            // intent.putExtra("name",d.getString(""))
            startActivity(intent)
            finish()
        }
        btnlogin.setOnClickListener{




            if(ConnectionManager().checkConnectivity(this@MainActivity)) {

                if(validator.validate()) {
                    val queue = Volley.newRequestQueue(this@MainActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result/"
                    val jsonObject = JSONObject()
                    jsonObject.put("mobile_number", etUserNumber.text.toString())
                    jsonObject.put("password", etUserPassword.text.toString())

                    try {
                        val jsonObjectRequest = object : JsonObjectRequest(
                            Request.Method.POST, url, jsonObject, Response.Listener {

                                Toast.makeText(
                                    this@MainActivity,
                                    "saksham is here $it", Toast.LENGTH_LONG
                                ).show()
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val d = data.getJSONObject("data")

                                    sharedPreferences.edit().putBoolean("logedIn", true).apply()
                                    sharedPreferences.edit().putString("name", d.getString("name"))
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString("user_id", d.getString("user_id")).apply()
                                    sharedPreferences.edit()
                                        .putString("email", d.getString("email")).apply()
                                    sharedPreferences.edit()
                                        .putString("mobile_number", d.getString("mobile_number"))
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString("address", d.getString("address")).apply()
                                    val intent = Intent(this@MainActivity, Welcome::class.java)
                                    // intent.putExtra("name",d.getString(""))
                                    startActivity(intent)
                                    finish()
                                } else {

                                }

                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@MainActivity, "Volley Error Occured!!!!" +
                                            "$it", Toast.LENGTH_LONG
                                ).show()

                            }
                        ) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content_type"] = "application/json"
                                headers["token"] = "878041adce9bab"
                                return headers
                            }
                        }
                        queue.add(jsonObjectRequest)

                    } catch (e: Exception) {
                        if (this@MainActivity != null)
                            Toast.makeText(
                                this@MainActivity,
                                "some unexpected error occured!!!!",
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }else {
                    Toast.makeText(
                        this@MainActivity,
                        "Validation failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else{
                val dialog  = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("error")
                dialog.setMessage("internet conn is not found")
                dialog.setPositiveButton("open Settings"){
                        text,listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){
                        text,listener ->
                    ActivityCompat.finishAffinity(this@MainActivity)

                }
                dialog.create()
                dialog.show()

            }
//            else{
//                Toast.makeText(this@MainActivity,"some error occured!!!!",Toast.LENGTH_LONG).show()
//            }

        }



        txtFgtPassword.setOnClickListener {
            val intent= Intent(this@MainActivity,ForgetPassword::class.java)
            startActivity(intent)
        }
        txtRegister.setOnClickListener {
            val intent= Intent(this@MainActivity,Register::class.java)
            startActivity(intent)
        }

    }



}
