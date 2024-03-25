package com.example.my_client_for_linux

import android.content.Intent
import android.media.audiofx.DynamicsProcessing.BandBase
import android.os.Bundle
import android.os.PersistableBundle
import android.text.style.UpdateLayout
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.net.URL
import kotlin.math.log


class LinerActivity: AppCompatActivity() {
    private var host:String = ""
    private var port:Int = 0
    private fun GetIp(): Array<String>
    {
        val ip = Array<String>(2){""}
        val url = URL("https://raw.githubusercontent.com/pag6666/ngrok_file/main/ngrok_for_mobila")
        val std =  url.readText()
        val hostport = std.split(":")
        ip[0] = hostport[0]
        ip[1] = hostport[1]
        return ip
    }
    lateinit var client: Socket
    private fun connect() {
        try {
            val ip = GetIp()
            host = ip[0]
            port = ip[1].trim().toInt()
            client = Socket(host.toString(), port)
            val writeStream = client.getOutputStream()
            val readStream = client.getInputStream()
            //my state
            Write(writeStream, "0")
            Log.d("check_my_state",Read(readStream))

            Write(writeStream, "check")
            val std = Read(readStream)
            var request:String = ""
            when(std)
            {
                "success_check"-> {
                    request = "success"
                    ClientObj()
                }
                else-> {
                    request = "command_not_found"
                }
            }
        }
        catch (ex: Exception)
        {
            Log.d("fffffffffffffffff",ex.message.toString())
            Thread.sleep(1000)

            connect()

        }
    }


    private fun Write(stream: OutputStream, text:String)
    {
        var buffer:ByteArray= "null".toByteArray()
        if(text.length < 128) {
           buffer = text.toByteArray()
        }
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
    lateinit var users:List<String>
    private fun find_client_for_name(name:String)
    {
        val writestream = client.getOutputStream()
        val readstream = client.getInputStream()
        Thread(Runnable {
            var request:String = ""
            Write(writestream,"connect");
            val answer = Read(readstream)
            if(answer == "success_connected")
            {
                Write(writestream, name)
                Log.d("answer_name_server",Read(readstream))
                val login = intent.getStringExtra("login")
                val password = intent.getStringExtra("password")
                val intent = Intent(this, terminalActivity::class.java)
                intent.putExtra("client", name)
                intent.putExtra("login", login)
                intent.putExtra("password", password)
                startActivity(intent)
                client.close()
            }
            else
            {
                Toast.makeText(this,"client disconnected",Toast.LENGTH_LONG).show()
            }
        }).start()
    }
    public fun button_click_update(view:View)
    {
        val writestream = client.getOutputStream()
        val readstream = client.getInputStream()
        Thread(Runnable {
            clientname.clear()
            Write(writestream,"check")
            Log.d("server_read",Read(readstream))
            Write(writestream,"get_client_user");
            val users_str:String = Read(readstream)
            Log.d("array_get_server",users_str)
            if(users_str!="null") {
                users = users_str.split(",")
                clientname.addAll(users)
                for (i in clientname) {
                    Log.d("server_item", i.toString())
                }
            }
            else
            {
                clientname.add("null")
            }
        }).start()
        Thread.sleep(2000)
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, clientname)
        listView.adapter = adapter
        Log.d("clientname index = 0",clientname[0]);
        if(clientname[0]!="null") {
            listView.setOnItemClickListener { parent, view, position, id ->
                Thread.sleep(100)
                find_client_for_name(clientname.get(position))
            }
        }
    }
    private fun ClientObj()
    {
        val writestream = client.getOutputStream()
        val readstream = client.getInputStream()

        Thread(Runnable {
            clientname.clear()
            clientname.clear()
            Write(writestream,"check")
            Log.d("server_read",Read(readstream))
            Write(writestream,"get_client_user");
            val users_str:String = Read(readstream)
            Log.d("array_get_server",users_str)
            if(users_str!="null") {
                users = users_str.split(",")
                clientname.addAll(users)
                for (i in clientname) {
                    Log.d("server_item", i.toString())
                }
            }
            else
            {
                clientname.add("client not found")
            }
        }).start()

       /* */

    }
    var clientname = ArrayList<String>()
    lateinit var adapter:ArrayAdapter<String>

    private lateinit var listView:ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liner)
        listView = findViewById(R.id.list_client)
        connectClient()

    }
}