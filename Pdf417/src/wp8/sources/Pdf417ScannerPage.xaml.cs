using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microblink;
using Microblink.UserControls;
using System.Windows.Media.Imaging;
using System.Text;

namespace Microblink
{
    public partial class Pdf417ScannerPage : PhoneApplicationPage
    {

        private bool mInitialized = false;

        public static string LicenseKey { get; set; }

        public static bool Scan_PDF417 { get; set; }
        public static bool Scan_USDL { get; set; }
        public static bool Scan_QR_Code { get; set; }
        public static bool Scan_Code_128 { get; set; }
        public static bool Scan_Code_39 { get; set; }
        public static bool Scan_EAN_13 { get; set; }
        public static bool Scan_EAN_8 { get; set; }
        public static bool Scan_ITF { get; set; }
        public static bool Scan_UPCA { get; set; }
        public static bool Scan_UPCE { get; set; }
        public static bool Scan_Aztec { get; set; }
        public static bool Scan_Data_Matrix { get; set; }

        private static bool Scan_ZXing {
            get {
                return Scan_QR_Code || Scan_Code_128 || Scan_Code_39 || Scan_EAN_13 || Scan_EAN_8 || Scan_ITF || Scan_UPCA || Scan_UPCE || Scan_Aztec || Scan_Data_Matrix;
            }
        }

        private static bool Scan_Bardecoder {
            get {
                return Scan_Code_128 || Scan_Code_39;
            }
        }

        public static bool Option_Beep { get; set; }
        public static bool Option_NoDialog { get; set; }
        public static bool Option_RemoveOverlay { get; set; }
        public static bool Option_Uncertain { get; set; }
        public static bool Option_QuietZone { get; set; }
        public static bool Option_HighRes { get; set; }
        public static bool Option_FrontFace { get; set; }
        public static bool Option_CustomUI { get; set; }
        public static bool Option_InverseScanning { get; set; }

        public delegate void FailureEventHandler(string message);

        public delegate void CompletedEventHandler(IList<Microblink.IRecognitionResult> resultList);

        public delegate void CancelEventHandler();

        public event FailureEventHandler OnFailure;

        public event CompletedEventHandler OnComplete;

        public event CancelEventHandler OnCancel;

        /// <summary>
        /// Initializes the RecognizerControl
        /// </summary>
        private void InitializeRecognizer() {
            // sets license key            
            mRecognizer.LicenseKey = LicenseKey;

            // add recognizer settings
            int numRecognizers = 0;
            Microblink.PDF417RecognizerSettings pdf417Settings = null;
            if (Scan_PDF417) {
                pdf417Settings = new Microblink.PDF417RecognizerSettings();
                pdf417Settings.InverseScanMode = Option_InverseScanning;
                pdf417Settings.NullQuietZoneAllowed = Option_QuietZone;
                pdf417Settings.UncertainScanMode = Option_Uncertain;
                numRecognizers++;
            }
            Microblink.ZXingRecognizerSettings zxingSettings = null;
            if (Scan_ZXing) {
                zxingSettings = new Microblink.ZXingRecognizerSettings();
                zxingSettings.AztecCode = Scan_Aztec;
                zxingSettings.Code128 = Scan_Code_128;
                zxingSettings.Code39 = Scan_Code_39;
                zxingSettings.DataMatrixCode = Scan_Data_Matrix;
                zxingSettings.EAN13Code = Scan_EAN_13;
                zxingSettings.EAN8Code = Scan_EAN_8;
                zxingSettings.ITFCode = Scan_ITF;
                zxingSettings.QRCode = Scan_QR_Code;
                zxingSettings.UPCACode = Scan_UPCA;
                zxingSettings.UPCECode = Scan_UPCE;
                zxingSettings.InverseScanMode = Option_InverseScanning;
                numRecognizers++;
            }
            Microblink.BarDecoderRecognizerSettings bardecoderSettings = null;
            if (Scan_Bardecoder) {
                bardecoderSettings = new Microblink.BarDecoderRecognizerSettings();
                bardecoderSettings.ScanCode128 = Scan_Code_128;
                bardecoderSettings.ScanCode39 = Scan_Code_39;
                bardecoderSettings.InverseScanMode = Option_InverseScanning;
                numRecognizers++;
            }
            Microblink.USDLRecognizerSettings usdlSettings = null;
            if (Scan_USDL) {
                usdlSettings = new Microblink.USDLRecognizerSettings();
                usdlSettings.NullQuietZoneAllowed = Option_QuietZone;
                usdlSettings.UncertainScanMode = Option_Uncertain;
                numRecognizers++;
            }
            Microblink.IRecognizerSettings[] recognizers = new Microblink.IRecognizerSettings[numRecognizers];
            int index = 0;
            if (usdlSettings != null) {
                recognizers[index++] = usdlSettings;
            }
            if (pdf417Settings != null) {
                recognizers[index++] = pdf417Settings;
            }
            if (bardecoderSettings != null) {
                recognizers[index++] = bardecoderSettings;
            }
            if (zxingSettings != null) {
                recognizers[index++] = zxingSettings;
            }            

            mRecognizer.RecognizerSettings = recognizers;
            mRecognizer.GenericRecognizerSettings = new Recognition.GenericRecognizerSettings() { AllowMultipleScanResults = true };
            mInitialized = true;
        }

        void mRecognizer_OnDisplayPointSet(IList<Windows.Foundation.Point> points) {
            mViewFinder.DisplayPointSet(points);
        }

        void mRecognizer_OnDisplayDefaultTarget(DetectionStatus detectionStatus) {
            mViewFinder.DisplayDefaultPosition();
        }

        void mRecognizer_OnDisplayNewTarget(Windows.Foundation.Point uleft, Windows.Foundation.Point uright, Windows.Foundation.Point lleft, Windows.Foundation.Point lright, DetectionStatus detectionStatus) {
            mViewFinder.DisplayPosition(uleft, uright, lleft, lright, detectionStatus);
        }

        /// <summary>
        /// Handles initialization error(invalid license)
        /// </summary>
        /// <param name="errorType">initialization error type(only INVALID_LICENSE_KEY at the moment)</param>
        void mRecognizer_OnInitializationError(InitializationErrorType errorType) {
            // handle licensing error by displaying error message and terminating the application
            if (errorType == InitializationErrorType.INVALID_LICENSE_KEY) {
                if (OnFailure != null) {
                    OnFailure("Could not unlock API! Invalid license key!");
                } else { 
                    MessageBox.Show("Could not unlock API! Invalid license key!\nThe application will now terminate!");
                    Application.Current.Terminate();
                }
            } else {
                // there are no other error types
                throw new NotImplementedException();
            }
        }

        /// <summary>
        /// Handles completed scanning events.
        /// Navigates to results page if scanning was successful.
        /// </summary>
        /// <param name="resultList">list of recognition results</param>
        /// <param name="recognitionType">type of recognition</param>
        void mRecognizer_OnScanningDone(IList<Microblink.IRecognitionResult> resultList, RecognitionType recognitionType) {
            if (recognitionType == RecognitionType.SUCCESSFUL) {
                if (OnComplete != null) {
                    OnComplete(resultList);
                    if (Option_Beep) {
                        mBeepSound.Play();
                    }
                }
                if (!Option_NoDialog) {
                    // Find barcode results in list of results.
                    bool resultFound = false;
                    IReadOnlyDictionary<string, object> elements = null;
                    foreach (var result in resultList) {
                        if (result.Valid && !result.Empty) {
                            // check if result is a PDF417 result
                            if (result is Microblink.PDF417RecognitionResult) {
                                // obtain the PDF417 result
                                Microblink.PDF417RecognitionResult pdf417Result = (Microblink.PDF417RecognitionResult)result;
                                elements = pdf417Result.Elements;
                                // mark as found
                                resultFound = true;
                                break;
                            }
                             // check if result is a ZXing result
                            else if (result is Microblink.ZXingRecognitionResult) {
                                // obtain the ZXing result
                                Microblink.ZXingRecognitionResult zxingResult = (Microblink.ZXingRecognitionResult)result;
                                elements = zxingResult.Elements;
                                // mark as found
                                resultFound = true;
                                break;
                            }                                
                            // check if result is a Bardecoder result
                            else if (result is Microblink.BarDecoderRecognitionResult) {
                                // obtain the Bardecoder result
                                Microblink.BarDecoderRecognitionResult bdecoderResult = (Microblink.BarDecoderRecognitionResult)result;
                                elements = bdecoderResult.Elements;
                                // mark as found
                                resultFound = true;
                                break;
                            }
                            // check if result is a USDL result
                            else if (result is Microblink.USDLRecognitionResult) {
                                // obtain the USDL result
                                Microblink.USDLRecognitionResult usdlResult = (Microblink.USDLRecognitionResult)result;
                                elements = usdlResult.Elements;
                                // mark as found
                                resultFound = true;
                                break;
                            }
                        }
                    }
                    // display dialog if result are found
                    if (resultFound && elements != null) {
                        StringBuilder msg = new StringBuilder();
                        foreach (string key in elements.Keys) {
                            msg.Append(key);
                            msg.Append(": ");
                            msg.Append(elements[key] != null ? elements[key].ToString().Trim() : "");
                            msg.Append("\n");
                        }
                        MessageBox.Show(msg.ToString());
                    }
                }
                // navigate back to caller page
                NavigationService.GoBack();
            }                                            
        }

        /// <summary>
        /// Here camera errors should be handled.
        /// </summary>
        /// <param name="error"></param>
        void mRecognizer_OnCameraError(Microblink.UserControls.CameraError error) {
            if (OnFailure != null) {
                switch (error) {
                    case CameraError.CameraNotReady: OnFailure("Camera not ready"); break;
                    case CameraError.NoCameraAtSelectedSensorLocation: OnFailure("No camera at selected sensor location"); break;
                    case CameraError.NotSupported: OnFailure("Camera not supported"); break;
                    case CameraError.PreviewSizeTooSmall: OnFailure("Camera preview size too small"); break;
                    default: OnFailure("Camera error"); break;
                }
            } 
            // just throw an exception
            else throw new NullReferenceException();
        }

        public Pdf417ScannerPage() {
            InitializeComponent();
            // these events must be handled
            mRecognizer.OnCameraError += mRecognizer_OnCameraError;
            mRecognizer.OnScanningDone += mRecognizer_OnScanningDone;
            mRecognizer.OnInitializationError += mRecognizer_OnInitializationError;
            mRecognizer.OnDisplayNewTarget += mRecognizer_OnDisplayNewTarget;
            mRecognizer.OnDisplayDefaultTarget += mRecognizer_OnDisplayDefaultTarget;
            mRecognizer.OnDisplayPointSet += mRecognizer_OnDisplayPointSet;
        }

        /// <summary>
        /// Called when this page is navigated to.
        /// Starts the recognition process.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatedTo(NavigationEventArgs e) {
            // call default behaviour
            base.OnNavigatedTo(e);
            // initialize the recognition process
            if (!mInitialized) {
                InitializeRecognizer();
            }
            mRecognizer.InitializeControl(this.Orientation);
        }

        /// <summary>
        /// Called when the user leaves this page.
        /// Stops the recognition process.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatingFrom(NavigatingCancelEventArgs e) {
            // trigger cancel event
            if (OnCancel != null) {
                OnCancel();
            }
            // terminate the recognizer
            mRecognizer.TerminateControl();
            // call default behaviour
            base.OnNavigatingFrom(e);
        }

        /// <summary>
        /// Animate rotation of "Cancel" and "Light" buttons
        /// on orientation change.
        /// </summary>
        /// <param name="e">orientation change event info</param>
        private void AnimateButtons(OrientationChangedEventArgs e) {
            // check if a new orientation is landscape or portrait
            if (e.Orientation == PageOrientation.Landscape || e.Orientation == PageOrientation.LandscapeLeft || e.Orientation == PageOrientation.LandscapeRight) {
                // align "Light" button to the left
                mLightButton.HorizontalAlignment = System.Windows.HorizontalAlignment.Left;
                // align "Cancel" button to the bottom
                mCancelButton.VerticalAlignment = System.Windows.VerticalAlignment.Bottom;
                // start the portrait to landscape animation
                mP2LAnimation.Begin();
            } else {
                // align "Light" button to the right
                mLightButton.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
                // align "Cancel" button to the top
                mCancelButton.VerticalAlignment = System.Windows.VerticalAlignment.Top;
                // start the landscape to portrait animation
                mL2PAnimation.Begin();
            }
        }

        /// <summary>
        /// Handles orientation change event by animating
        /// buttons and rectangle and forwarding the event
        /// to RecognizerControl
        /// </summary>
        /// <param name="e">orientation change event info</param>
        protected override void OnOrientationChanged(OrientationChangedEventArgs e) {
            // call default behaviour
            base.OnOrientationChanged(e);
            // animate "Cancel" and "Light" buttons
            AnimateButtons(e);
            // orientation change events MUST be forwarded to RecognizerControl
            mRecognizer.OnOrientationChanged(e);
        }

        /// <summary>
        /// Handles "Light" button click event.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mLightButton_Click(object sender, RoutedEventArgs e) {
            // check if the camera light is available
            if (mRecognizer.IsTorchSupported) {
                // toggle camera light
                mRecognizer.TorchOn = !mRecognizer.TorchOn;
                // toggle "Light" button icon
                if (mRecognizer.TorchOn) {
                    mLightImage.ImageSource = new BitmapImage(new Uri("Assets/Icons/icon_flashlight_selected.png", UriKind.Relative));
                } else {
                    mLightImage.ImageSource = new BitmapImage(new Uri("Assets/Icons/icon_flashlight.png", UriKind.Relative));
                }
            } else {
                // if camera light is not available display message
                MessageBox.Show("Camera light is not supported on this device!");
            }
        }

        /// <summary>
        /// Sets up "Light" button rotation center to be at
        /// the center of the button. We must do it at 
        /// button loaded event when button dimensions are known.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mLightButton_Loaded(object sender, RoutedEventArgs e) {
            mLightButtonRotation.CenterX = mLightButton.ActualWidth / 2.0;
            mLightButtonRotation.CenterY = mLightButton.ActualHeight / 2.0;
        }

        /// <summary>
        /// Sets up "Cancel" button rotation center to be at
        /// the center of the button. We must do it at 
        /// button loaded event when button dimensions are known.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mCancelButton_Loaded(object sender, RoutedEventArgs e) {
            mCancelButtonRotation.CenterX = mCancelButton.ActualWidth / 2.0;
            mCancelButtonRotation.CenterY = mCancelButton.ActualHeight / 2.0;
        }

        /// <summary>
        /// Handles "Cancel" button click event by
        /// going back to caller page.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mCancelButton_Click(object sender, RoutedEventArgs e) {
            NavigationService.GoBack();
        }

    }
}