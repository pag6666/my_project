using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Sre
{
    enum ErrorState
    {
       None,ErrorStream,ErrorTcpClientClientObj,ErrorTcpListener,ErrorTcpClientShpionObj
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
}
