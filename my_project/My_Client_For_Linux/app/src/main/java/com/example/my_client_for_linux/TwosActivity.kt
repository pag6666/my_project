package com.example.my_client_for_linux
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.IpSecManager.ResourceUnavailableException
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.net.SocketAddress
import java.net.URL
import java.text.Format
import java.util.Base64.Encoder
import kotlin.concurrent.thread
import kotlin.math.log

class TwosActivity : AppCompatActivity(){
    lateinit var main_two:LinearLayout
    lateinit var text:TextView
    lateinit var image: ImageView
    lateinit var progressbar:ProgressBar
    lateinit var ipEndPoint:TextView
    private fun Init()
    {
        ipEndPoint= findViewById(R.id.EndPoint)
        main_two = findViewById(R.id.main_two)
        progressbar = findViewById(R.id.prog)
        text = findViewById(R.id.stateconnect)
        image = findViewById(R.id.imagestateconnect)
    }
    private var i = 0
    private val handler = Handler()
    private fun startpb()
    {
        progressbar.visibility = View.VISIBLE

        i = progressbar.progress

        Thread(Runnable {
            // this loop will run until the value of i becomes 99
            while (i < 100) {

                // Update the progress bar and display the current value
                handler.post(Runnable {
                    progressbar.progress = i
                    // setting current progress to the textview

                })
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // setting the visibility of the progressbar to invisible
            // or you can use View.GONE instead of invisible
            // View.GONE will remove the progressbar
            progressbar.visibility = View.INVISIBLE

        }).start()
    }
    private var host:String = ""
    private var port:Int = 0
    private fun GetIp()
    {
        val url = URL("https://raw.githubusercontent.com/pag6666/ngrok_file/main/ngrok_for_mobila")
        val std =  url.readText()
        val hostport = std.split(":")
        host = hostport[0].trim()
        port = hostport[1].trim().toInt()
        Log.d("ip_host",host)
        Log.d("ip_port",port.toString())
        ipEndPoint.setText("ip = "+host.toString()+":"+port.toString())

    }
      lateinit var client:Socket
    private fun connect() {
        try {
            GetIp()
            client = Socket(host.toString(), port)
            val writeStream = client.getOutputStream()
            val readStream = client.getInputStream()
            //my state
            Write(writeStream, "0")
            Log.d("check_my_state",Read(readStream))

            Write(writeStream, "check")
            val std = Read(readStream)
            Log.d("stddddddddddddd",std)
            var request:String = ""
            when(std.trim())
            {
                "success_check"-> {
                    request = "success"
                    i=100

                    successConnected()
                }
                else-> {
                    request = "command_not_found"
                }
            }
            Log.d("request",request)
        }
        catch (ex:Exception)
        {
            Log.d("error",ex.message.toString())
            i++
            if(i > 99)
            {
                i = 0
            }
            Thread.sleep(100)
            connect()
        }
    }
    private fun Write(stream: OutputStream, text:String)
    {
        val buffer = text.toByteArray()
        stream.write(buffer, 0, buffer.size)
        stream.flush()
    }

    private fun Read(stream: InputStream):String
    {

        val buffer = ByteArray(128)
        val size = stream.read(buffer, 0, buffer.size);
        return String(buffer,0,size)
        //buffer.toString(Charsets.UTF_8).trim()
    }
    private fun connectClient()
    {
        Thread(Runnable {
            connect()
        }).start()

    }

    private fun successConnected()
    {
        main_two.setBackgroundResource(R.drawable.sucess_gradient_background)
        progressbar.visibility = View.INVISIBLE
        text.setText("sucess connected")
        image.setImageResource(R.drawable.l)
        Thread(Runnable {
            Thread.sleep(3000)
            val listActivity = Intent(this, LinerActivity::class.java)
            val login = intent.getStringExtra("login")
            val password = intent.getStringExtra("password")
            client.close()
            listActivity.putExtra("login",login)
            listActivity.putExtra("password",password)
            startActivity(listActivity)
        }).start()


    }
    private  fun faillConnected()
    {
        main_two.setBackgroundResource(R.drawable.fail_gradient_background)
        progressbar.visibility = View.VISIBLE
        text.setText("connected not success")
        image.setImageResource(R.drawable.x)
    }
    var isconnected:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tworst)
        Init()
        startpb()
        faillConnected()
        connectClient()

    }
}


