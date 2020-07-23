package com.example.foodhub.Acitivity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
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
import org.jetbrains.annotations.NotNull
import org.json.JSONObject
import java.lang.Exception

class Register : AppCompatActivity() {
    lateinit var etUserName:EditText
    lateinit var etUserEmail:EditText
    lateinit var etUserNumber:EditText
    lateinit var etUserAddress:EditText
    lateinit var etUserPassword:EditText
    lateinit var etUserConfirmPass:EditText
    lateinit var btnRegister: Button
    lateinit var toolbar:Toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUserName = findViewById(R.id.etUserName)
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserNumber = findViewById(R.id.etUserNumber)
        etUserAddress = findViewById(R.id.etUserAddress)
        etUserPassword = findViewById(R.id.etUserPassword)
        etUserConfirmPass = findViewById(R.id.etUserConfirmPass)
        btnRegister = findViewById(R.id.btnRegister)
        toolbar=findViewById(R.id.toolbar)

        var validator :AwesomeValidation=AwesomeValidation(ValidationStyle.BASIC)

        //for name
        validator.addValidation(this,R.id.etUserName,RegexTemplate.NOT_EMPTY,R.string.invalidName)
        /// for user number
        validator.addValidation(this,R.id.etUserNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_number)


        // for Email
        validator.addValidation(this,R.id.etUserEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_Email)

        //for passsword
        validator.addValidation(this,R.id.etUserPassword,".{4,}",R.string.invalid_password)
        // validation

        validator.addValidation(this,R.id.etUserConfirmPass,R.id.etUserPassword,R.string.invalid_confirm_password)











        setupToolbar()
        val sharedPreferences=getSharedPreferences("FoodHub", Context.MODE_PRIVATE)
        btnRegister.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@Register)) {
                if(validator.validate()) {
                    val queue = Volley.newRequestQueue(this@Register)
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    try {
                        val jsonObject = JSONObject()
                        jsonObject.put("name", etUserName.text.toString())
                        jsonObject.put("mobile_number", etUserNumber.text.toString())
                        jsonObject.put("password", etUserPassword.text.toString())
                        jsonObject.put("address", etUserAddress.text.toString())
                        jsonObject.put("email", etUserEmail.text.toString())
                        val jsonObjectRequest = object :
                            JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonObject,
                                Response.Listener {
                                    val res = it.getJSONObject("data")
                                    val success = res.getBoolean("success")
                                    if (success) {
                                        val d = res.getJSONObject("data")
                                        sharedPreferences.edit().putBoolean("logedIn", true).apply()
                                        sharedPreferences.edit()
                                            .putString("name", d.getString("name")).apply()
                                        sharedPreferences.edit()
                                            .putString("user_id", d.getString("user_id")).apply()
                                        sharedPreferences.edit()
                                            .putString("email", d.getString("email")).apply()
                                        sharedPreferences.edit().putString(
                                            "mobile_number",
                                            d.getString("mobile_number")
                                        ).apply()
                                        sharedPreferences.edit()
                                            .putString("address", d.getString("address")).apply()
                                        Toast.makeText(this@Register,"Registered Successfully",Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@Register, Welcome::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val msg = res.getString("errorMessage")
                                        Toast.makeText(this@Register, "$msg", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                },
                                Response.ErrorListener {
                                    if (this@Register != null)
                                    Toast.makeText(this@Register, "Server Error Occured", Toast.LENGTH_LONG)
                                        .show()

                                }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                var headers = HashMap<String, String>()
                                headers["Content_type"] = "application/json"
                                headers["token"] = "878041adce9bab"
                                return headers
                            }
                        }
                        queue.add(jsonObjectRequest)
                    } catch (e: Exception) {

                            Toast.makeText(this@Register, "Some unexpected error ", Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(this@Register, "validation failed", Toast.LENGTH_LONG).show()

                }
            }else{
                //dialog lagana
                val dialog  = AlertDialog.Builder(this@Register)
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
                    ActivityCompat.finishAffinity(this@Register)

                }
                dialog.create()
                dialog.show()

            }
        }
    }
    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        //this will enable the home buttton make it active
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
