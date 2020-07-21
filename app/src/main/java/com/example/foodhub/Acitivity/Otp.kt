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
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.foodhub.R
import com.example.foodhub.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class Otp : AppCompatActivity() {
        lateinit var etUserotp:EditText
        lateinit var etUserNewPassword:EditText
        lateinit var etUserConfirmPass:EditText
        lateinit var btnSubmit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        etUserotp=findViewById(R.id.etUserotp)
        etUserNewPassword=findViewById(R.id.etUserNewPassword)
        etUserConfirmPass=findViewById(R.id.etUserConfirmPass)
        btnSubmit=findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener{
           //  fun CharSequence?.isValidEmail()=!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(etUserConfirmPass.text).matches()

            val jsonObject=JSONObject()


            var validator : AwesomeValidation = AwesomeValidation(ValidationStyle.BASIC)


                validator.addValidation(this,R.id.etUserotp,".{4}",R.string.invalid_otp)
            //for passsword
            validator.addValidation(this,R.id.etUserPassword,".{4,}",R.string.invalid_password)
            // validation

            validator.addValidation(this,R.id.etUserConfirmPass,R.id.etUserPassword,R.string.invalid_confirm_password)








            val number=intent.getStringExtra("number")











            jsonObject.put("mobile_number",number.toString())
            jsonObject.put("password",etUserNewPassword.text.toString())
            jsonObject.put("otp",etUserotp.text.toString())
            if(ConnectionManager().checkConnectivity(this@Otp)) {
                if(validator.validate()) {
                    val queue = Volley.newRequestQueue(this@Otp)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    try {

                        val jsonObjectRequest = object :
                            JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonObject,
                                Response.Listener {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")
                                    val message = data.getString("successMessage")
                                    if (success) {

                                        Toast.makeText(this@Otp, "$message", Toast.LENGTH_LONG)
                                            .show()
                                        val intent = Intent(this@Otp, MainActivity::class.java)
                                        startActivity(intent)
                                        finishAffinity()

                                    } else {
                                        Toast.makeText(this@Otp, "$message", Toast.LENGTH_LONG)
                                            .show()
                                    }
                                },
                                Response.ErrorListener {
                                    Toast.makeText(this@Otp, "$it", Toast.LENGTH_LONG).show()
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
                        if (this@Otp != null)
                            Toast.makeText(this@Otp, "error ", Toast.LENGTH_LONG).show()

                    }
                }else{

                    Toast.makeText(this@Otp, "Validation failed ", Toast.LENGTH_LONG).show()

                }
            }else{
                val dialog  = AlertDialog.Builder(this@Otp)
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
                    ActivityCompat.finishAffinity(this@Otp)

                }
                dialog.create()
                dialog.show()

            }

        }
    }
}
