using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using WPCordovaClassLib.Cordova;
using WPCordovaClassLib.Cordova.Commands;
using WPCordovaClassLib.Cordova.JSON;

namespace Cordova.Extension.Commands
{

    [DataContract]
    class Pdf417ScannerOptions
    {            
        [DataMember(IsRequired = false)]
        public bool beep = true;
        
        [DataMember(IsRequired = false)]
        public bool noDialog = false;
        
        [DataMember(IsRequired = false)]
        public bool removeOverlay = false;
         
        [DataMember(IsRequired = false)]
        public bool uncertain = false;
         
        [DataMember(IsRequired = false)]
        public bool quietZone = false;
         
        [DataMember(IsRequired = false)]
        public bool highRes = true;
         
        [DataMember(IsRequired = false)]
        public bool frontFace = false;
        
        [DataMember(IsRequired = false)]
        public bool customUI = false;
        
        [DataMember(IsRequired = false)]
        public bool inverseScanning = false;        
    }

    class Pdf417Scanner : BaseCommand
    {

        public void scan(string parameters) {
            // extract plugin parameters
            string[] paramList = null;
            string[] types;
            Pdf417ScannerOptions options;
            string licenseKey;            
            try {
                paramList = JsonHelper.Deserialize<string[]>(parameters);
                types = JsonHelper.Deserialize<string[]>(paramList[0]);
                options = JsonHelper.Deserialize<Pdf417ScannerOptions>(paramList[1]);
                licenseKey = paramList[4];
            } catch (Exception) {
                //ignored
            }
            if (paramList == null) {
                // there was an error deserializing JSON params. return error to caller
                DispatchCommandResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            } else {

            }

        }

    }
}
