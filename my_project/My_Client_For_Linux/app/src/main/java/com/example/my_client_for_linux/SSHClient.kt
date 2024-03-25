package com.example.my_client_for_linux

class SSHClient {
    private lateinit var _login:String
    private  lateinit var _password:String
    public constructor(login:String, password:String)
    {
        _login = login
        _password = password
    }
    public fun GetLogin():String
    {
        return _login
    }
    public fun GetPassword():String
    {
        return _password
    }

}