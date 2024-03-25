using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Sre
{
    class ItemListSpion
    {
        private int _key = 0;
        private TcpClient _client = null;
        public ItemListSpion(TcpClient client, int key) 
        {
            this._client = client;
            this._key = key;
        }
        public int GetKey() 
        {
            return this._key;
        }
        public TcpClient GetClient() 
        {
            return this._client;
        }
    }
}
