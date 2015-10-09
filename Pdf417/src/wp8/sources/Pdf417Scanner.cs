using Cordova.Extension.Commands;
using Microblink;
using Microsoft.Phone.Controls;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Navigation;
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

    [DataContract]
    class Pdf417ScannerResult
    {
        [DataMember]
        public bool cancelled;

        [DataMember]
        public IList<Pdf417ScannerBaseResult> resultList;

        public static PluginResult CancelResult() {
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            Pdf417ScannerResult resultMsg = new Pdf417ScannerResult();
            resultMsg.cancelled = true;
            result.Message = JsonHelper.Serialize(resultMsg);
            return result;
        }
        public string GetJSON() {
            StringBuilder b = new StringBuilder();
            b.Append("{");
            b.Append("\"cancelled\":");
            b.Append(cancelled ? "true" : "false");
            b.Append(",");
            b.Append("\"resultList\":");
            b.Append("[");
            if (resultList != null) {                
                bool first = true;
                foreach (var res in resultList) {
                    if (!first) {
                        b.Append(",");
                    }
                    b.Append(res.GetJSON());
                    first = false;
                }                
            }
            b.Append("]");
            b.Append("}");
            return b.ToString();
        }
    }

    [DataContract]
    abstract class Pdf417ScannerBaseResult
    {
        [DataMember]
        public string resultType;

        public abstract string GetJSON();

        protected string EscapeJSONString(string s) {
            if (s == null || s.Length == 0) {
                return "";
            }

            char c = '\0';
            int i;
            int len = s.Length;
            StringBuilder sb = new StringBuilder(len + 4);
            String t;

            for (i = 0; i < len; i += 1) {
                c = s[i];
                switch (c) {
                    case '\\':
                    case '"':
                        sb.Append('\\');
                        sb.Append(c);
                        break;
                    case '/':
                        sb.Append('\\');
                        sb.Append(c);
                        break;
                    case '\b':
                        sb.Append("\\b");
                        break;
                    case '\t':
                        sb.Append("\\t");
                        break;
                    case '\n':
                        sb.Append("\\n");
                        break;
                    case '\f':
                        sb.Append("\\f");
                        break;
                    case '\r':
                        sb.Append("\\r");
                        break;
                    default:
                        if (c < ' ') {
                            //t = "000" + String.Format("X", c);
                            t = String.Format("\\u{0:X4}", (int)c);
                            //sb.Append("\\u" + t.Substring(t.Length - 4));
                            sb.Append(t);
                        } else {
                            sb.Append(c);
                        }
                        break;
                }
            }
            return sb.ToString();
        }
    }

    [DataContract]
    class Pdf417ScannerBarcodeResult : Pdf417ScannerBaseResult
    {
        public Pdf417ScannerBarcodeResult() {
            resultType = "Barcode result";
        }        

        [DataMember]
        public string type;

        [DataMember]
        public string data;

        [DataMember(IsRequired = false, EmitDefaultValue = false)]
        public string raw;

        public override string GetJSON() {
            StringBuilder b = new StringBuilder();
            b.Append("{");
            b.Append("\"resultType\":");
            b.Append("\"");
            b.Append(resultType);
            b.Append("\"");
            b.Append(",");
            b.Append("\"type\":");
            b.Append("\"");
            b.Append(type);
            b.Append("\"");
            b.Append(",");
            b.Append("\"data\":");
            b.Append("\"");
            b.Append(EscapeJSONString(data));
            b.Append("\"");
            b.Append(",");
            b.Append("\"raw\":");
            if (raw == null) {
                b.Append("null");
            } else {
                b.Append("\"");
                b.Append(raw);
                b.Append("\"");
            }
            b.Append("}");
            return b.ToString();
        }
    }    


    [DataContract]
    class Pdf417ScannerUSDLResult : Pdf417ScannerBaseResult
    {
        public Pdf417ScannerUSDLResult() {
            resultType = "USDL result";
        }

        [DataMember]
        //public IDictionary<string, string> fields;
        public IDictionary<string, string> fields;

        public override string GetJSON() {
            StringBuilder b = new StringBuilder();
            b.Append("{");
            b.Append("\"resultType\":");
            b.Append("\"");
            b.Append(resultType);
            b.Append("\"");
            b.Append(",");            
            b.Append("\"fields\":");
            if (fields == null) {
                b.Append("{}");
            } else {
                b.Append("{");
                bool first = true;
                foreach (string key in fields.Keys) {
                    if (!first) {
                        b.Append(",");
                    }
                    b.Append("\"");
                    b.Append(key);
                    b.Append("\":\"");
                    b.Append(EscapeJSONString(fields[key]));
                    b.Append("\"");
                    first = false;
                }
                b.Append("}");
            }
            b.Append("}");
            return b.ToString();
        }
    }

    class Pdf417Scanner : BaseCommand
    {

        public void scan(string parameters) {
            // extract plugin parameters
            string[] paramList = null;
            string[] types = null;
            Pdf417ScannerOptions options = null;
            string licenseKey = null;
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
                foreach (string type in types) {
                    if ("PDF417".Equals(type)) {
                        Pdf417ScannerPage.Scan_PDF417 = true;
                    } else if ("USDL".Equals(type)) {
                        Pdf417ScannerPage.Scan_USDL = true;
                    } else if ("QR Code".Equals(type)) {
                        Pdf417ScannerPage.Scan_QR_Code = true;
                    } else if ("Code 128".Equals(type)) {
                        Pdf417ScannerPage.Scan_Code_128 = true;
                    } else if ("Code 39".Equals(type)) {
                        Pdf417ScannerPage.Scan_Code_39 = true;
                    } else if ("EAN 13".Equals(type)) {
                        Pdf417ScannerPage.Scan_EAN_13 = true;
                    } else if ("EAN 8".Equals(type)) {
                        Pdf417ScannerPage.Scan_EAN_8 = true;
                    } else if ("ITF".Equals(type)) {
                        Pdf417ScannerPage.Scan_ITF = true;
                    } else if ("UPCA".Equals(type)) {
                        Pdf417ScannerPage.Scan_UPCA = true;
                    } else if ("UPCE".Equals(type)) {
                        Pdf417ScannerPage.Scan_UPCE = true;
                    } else if ("Aztec".Equals(type)) {
                        Pdf417ScannerPage.Scan_Aztec = true;
                    } else if ("Data Matrix".Equals(type)) {
                        Pdf417ScannerPage.Scan_Data_Matrix = true;
                    }
                }
                Pdf417ScannerPage.Option_Beep = options.beep;
                Pdf417ScannerPage.Option_CustomUI = options.customUI;
                Pdf417ScannerPage.Option_FrontFace = options.frontFace;
                Pdf417ScannerPage.Option_HighRes = options.highRes;
                Pdf417ScannerPage.Option_InverseScanning = options.inverseScanning;
                Pdf417ScannerPage.Option_NoDialog = options.noDialog;
                Pdf417ScannerPage.Option_QuietZone = options.quietZone;
                Pdf417ScannerPage.Option_RemoveOverlay = options.removeOverlay;
                Pdf417ScannerPage.Option_Uncertain = options.uncertain;

                Pdf417ScannerPage.LicenseKey = licenseKey;

                Deployment.Current.Dispatcher.BeginInvoke(() => {
                    var root = Application.Current.RootVisual as PhoneApplicationFrame;

                    if (root == null) {
                        return;
                    }

                    root.Navigated += this.OnNavigated;
                    root.Navigate(new Uri("/Plugins/mobi.pdf417.Pdf417Scanner/Pdf417ScannerPage.xaml", UriKind.Relative));
                });
            }
        }

        private void OnNavigated(object sender, NavigationEventArgs e) {
            if (!(e.Content is Pdf417ScannerPage)) {
                return;
            }

            var phoneApplicationFrame = Application.Current.RootVisual as PhoneApplicationFrame;
            if (phoneApplicationFrame != null) {
                phoneApplicationFrame.Navigated -= this.OnNavigated;
            }

            var pdf417Scanner = (Pdf417ScannerPage)e.Content;

            if (pdf417Scanner != null) {
                pdf417Scanner.OnComplete += pdf417Scanner_OnComplete;
                pdf417Scanner.OnFailure += pdf417Scanner_OnFailure;
                pdf417Scanner.OnCancel += pdf417Scanner_OnCancel;
            } else {
                DispatchCommandResult(Pdf417ScannerResult.CancelResult());
            }           
        }

        void pdf417Scanner_OnCancel() {
            DispatchCommandResult(Pdf417ScannerResult.CancelResult());
        }

        void pdf417Scanner_OnFailure(string message) {
            DispatchCommandResult(new PluginResult(PluginResult.Status.ERROR, message));
        }

        void pdf417Scanner_OnComplete(IList<IRecognitionResult> resultList) {
            Pdf417ScannerResult pluginResult = new Pdf417ScannerResult();
            pluginResult.cancelled = false;
            pluginResult.resultList = new List<Pdf417ScannerBaseResult>();
            foreach (var recognitionResult in resultList) {
                if (recognitionResult.Valid && !recognitionResult.Empty) { 
                    if (recognitionResult is PDF417RecognitionResult) {
                        PDF417RecognitionResult pdf417Result = (PDF417RecognitionResult)recognitionResult;
                        Pdf417ScannerBarcodeResult barcodeResult = new Pdf417ScannerBarcodeResult();
                        barcodeResult.type = "PDF417";
                        barcodeResult.data = pdf417Result.StringData;
                        byte[] rawData = pdf417Result.RawData.GetAllData();                        
                        barcodeResult.raw = ByteArrayToHex(rawData);
                        pluginResult.resultList.Add(barcodeResult);
                    } else if (recognitionResult is ZXingRecognitionResult) {
                        ZXingRecognitionResult zxingResult = (ZXingRecognitionResult)recognitionResult;
                        Pdf417ScannerBarcodeResult barcodeResult = new Pdf417ScannerBarcodeResult();
                        barcodeResult.type = zxingResult.BarcodeTypeString;
                        barcodeResult.data = zxingResult.StringData;                        
                        pluginResult.resultList.Add(barcodeResult);
                    } else if (recognitionResult is BarDecoderRecognitionResult) {
                        BarDecoderRecognitionResult bcodeResult = (BarDecoderRecognitionResult)recognitionResult;
                        Pdf417ScannerBarcodeResult barcodeResult = new Pdf417ScannerBarcodeResult();
                        barcodeResult.type = bcodeResult.BarcodeTypeString;
                        barcodeResult.data = bcodeResult.StringData;                        
                        pluginResult.resultList.Add(barcodeResult);
                    } else if (recognitionResult is USDLRecognitionResult) {
                        USDLRecognitionResult uResult = (USDLRecognitionResult)recognitionResult;
                        Pdf417ScannerUSDLResult usdlResult = new Pdf417ScannerUSDLResult();
                        usdlResult.fields = new Dictionary<string,string>();
                        foreach (string key in uResult.Elements.Keys) {
                            if (uResult.Elements[key] is string) {
                                usdlResult.fields.Add(key, (string)uResult.Elements[key]);
                            }
                        }
                        pluginResult.resultList.Add(usdlResult);
                    }
                }
            }
            // dispatct JSON result
            string json = pluginResult.GetJSON();            
            DispatchCommandResult(new PluginResult(PluginResult.Status.OK, json));
        }

        private string ByteArrayToHex(byte[] ba) {
          string hex = BitConverter.ToString(ba);
          return hex.Replace("-","");
        }       

    }
}
