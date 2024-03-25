package com.example.my_client_for_linux

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.Socket
import java.net.URL

class terminalActivity:AppCompatActivity()
{
    //

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
            Log.d("check_my_state_terminal",Read(readStream))
            //
            Write(writeStream, "check")
            val std = Read(readStream)
            Log.d("terminal_answer_47line",std)
            var request:String = ""
            //////////////////crash
           ClientObj()
        }
        catch (ex: Exception)
        {
            Log.d("terminal_error_ffffff",ex.message.toString())
            Thread.sleep(1000)
            connect()
        }
    }

    private fun ClientObj() {

        Log.d("start_clienobj","true")
         val writestream = client.getOutputStream()
         val readstream = client.getInputStream()
         Thread(Runnable {
             Write(writestream,"connect_terminal")
             Log.d("connect_terminal",Read(readstream))
             //
             Write(writestream,"$client_name,$login,$password")
             Log.d("client_name,login,password",Read(readstream))
         }).start()
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
    lateinit var login:String
    lateinit var password:String
    lateinit var client_name:String

    //
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal)
        text = findViewById(R.id.consoletext)
        edit = findViewById(R.id.editcommand)
        //connect_terminal
        login = intent.getStringExtra("login").toString()
        password = intent.getStringExtra("password").toString()
        client_name = intent.getStringExtra("client").toString()
        connectClient()


    }
    lateinit var client: Socket
    lateinit var text:TextView
    lateinit var edit:EditText
    public fun on_click_send_message(view:View)
    {
        val writestream = client.getOutputStream()
        val readstream = client.getInputStream()
        val command = edit.text;
        var std:String
        edit.setText("");
        Thread(Runnable {
            Write(writestream, command.toString())
            std = Read(readstream)
            Log.d("check_read_terminal",std)
            text.setText(text.text.toString() + "$command\n$std\n");
        }).start()
    }
}