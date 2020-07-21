package com.example.foodhub.Fragment

import android.content.SharedPreferences
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.foodhub.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment(val name:String,val mobileNumber:String,val email:String,val address:String) : Fragment() {
    lateinit var txtUserName:TextView
    lateinit var txtUserNumber:TextView
    lateinit var txtUserEmailAddress:TextView
    lateinit var txtUserAddress:TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_profile, container, false)


        txtUserName=view.findViewById(R.id.txtUserName)
        txtUserNumber=view.findViewById(R.id.txtUserNumber)
        txtUserEmailAddress=view.findViewById(R.id.txtUserEmailAddress)
        txtUserAddress=view.findViewById(R.id.txtUserAddress)
         txtUserName.text=name
        txtUserAddress.text=address
        txtUserNumber.text=mobileNumber
        txtUserEmailAddress.text=email




        return view
    }


}
