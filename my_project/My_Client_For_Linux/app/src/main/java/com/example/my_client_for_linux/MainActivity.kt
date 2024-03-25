package com.example.my_client_for_linux

import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var loginEdit:EditText
    private lateinit var passwordEdit:EditText

    private fun Init()
    {
        loginEdit = findViewById(R.id.login)
        passwordEdit = findViewById(R.id.password)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.hide()
        Init()
    }
    private fun CheckStringOnNull(text:String):Boolean
    {
        var request:Boolean = false
        if(text.toString().trim().length > 0)
        {
            if(text !=  String().orEmpty()) {
                request = true
            }
        }
        return request
    }
    public fun OnClickConnect(view:View)
    {
        val login = loginEdit.text
        val paswword = passwordEdit.text
        if(CheckStringOnNull(login.toString()) && CheckStringOnNull(paswword.toString())){
            val ssg = SSHClient(login.toString(), paswword.toString())
            Log.d("login", ssg.GetLogin())
            Log.d("password", ssg.GetPassword())
            val inti = Intent(this,TwosActivity::class.java)
            inti.putExtra("login", ssg.GetLogin())
            inti.putExtra("password", ssg.GetPassword())
            startActivity(inti)

        }
        else
        {
            Toast.makeText(this,"Input Login or Password",Toast.LENGTH_LONG).show();
            Log.d("login and password","Input Login or Password")
        }
    }
}