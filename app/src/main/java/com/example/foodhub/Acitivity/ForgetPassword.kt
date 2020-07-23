package com.example.foodhub.Acitivity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.foodhub.Adapters.ItemsRecyclerAdapter
import com.example.foodhub.R
import com.example.foodhub.model.Food
import com.example.foodhub.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class ForgetPassword : AppCompatActivity() {
    lateinit var etUserNumber:EditText
    lateinit var etUserEmail:EditText
    lateinit var btnNext:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        etUserNumber=findViewById(R.id.etUserNumber)
        etUserEmail=findViewById(R.id.etUserEmail)
        btnNext=findViewById(R.id.btnNext)


        var validator : AwesomeValidation = AwesomeValidation(ValidationStyle.BASIC)


        /// for user number
        validator.addValidation(this,R.id.etUserNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_number)


        // for Email
        validator.addValidation(this,R.id.etUserEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_Email)

        btnNext.setOnClickListener {

            val jsonObject = JSONObject()
            jsonObject.put("mobile_number", etUserNumber.text.toString())
            jsonObject.put("email", etUserEmail.text.toString())
            val queue = Volley.newRequestQueue(this@ForgetPassword)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            if (ConnectionManager().checkConnectivity(this@ForgetPassword)) {
                if(validator.validate()) {
                    try {
                        val jsonObjectRequest = object : JsonObjectRequest(
                            Request.Method.POST, url, jsonObject, Response.Listener {
                                val itemsOfRes = it.getJSONObject("data")
                                val success = itemsOfRes.getBoolean("success")
                                if (success) {
                                    val first = itemsOfRes.getBoolean("first_try")
                                    if(first){
                                        val dialog = AlertDialog.Builder(this@ForgetPassword)
                                        dialog.setTitle("Information")
                                        dialog.setMessage("Kindly refer to the email for the OTP")
                                        dialog.setPositiveButton("OK")
                                        { text, listener ->
                                            val intent = Intent(this@ForgetPassword, Otp::class.java)
                                            intent.putExtra("number", etUserNumber.text.toString())
                                            startActivity(intent)
                                            finish()
                                        }
                                        dialog.create()
                                        dialog.show()
                                      //  Toast.makeText(this@ForgetPassword,"please check mail for otp",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        val dialog = AlertDialog.Builder(this@ForgetPassword)
                                        dialog.setTitle("Information")
                                        dialog.setMessage("Kindly refer to the previous email for the OTP")
                                        dialog.setPositiveButton("OK")
                                        { text, listener ->
                                            val intent = Intent(this@ForgetPassword, Otp::class.java)
                                            intent.putExtra("number", etUserNumber.text.toString())
                                            startActivity(intent)
                                            finish()
                                        }
                                        dialog.create()
                                        dialog.show()
                                       // Toast.makeText(this@ForgetPassword,"please check previous mail for otp",Toast.LENGTH_SHORT).show()
                                    }
                                    }else{
                                    val errorMessage=itemsOfRes.getString("errorMessage")
                                    Toast.makeText(
                                        this@ForgetPassword,
                                        "$errorMessage",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }

                            }, Response.ErrorListener {
                                if (this@ForgetPassword != null)
                                Toast.makeText(
                                    this@ForgetPassword,
                                    "server error occured!!!",
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

                            Toast.makeText(this@ForgetPassword, "some error occured ", Toast.LENGTH_LONG).show()
                    }

                }else{
                    Toast.makeText(this@ForgetPassword, "Validation failed ", Toast.LENGTH_LONG).show()

                }
            }else{
                val dialog  = AlertDialog.Builder(this@ForgetPassword)
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
                    ActivityCompat.finishAffinity(this@ForgetPassword)

                }
                dialog.create()
                dialog.show()

            }
        }
    }
}
