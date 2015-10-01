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

namespace mobi.pdf417.demo
{
    public partial class Pdf417ScannerPage : PhoneApplicationPage
    {

        /// <summary>
        /// Initializes the RecognizerControl
        /// </summary>
        private void InitializeRecognizer() {
            // sets license key
            // obtain your licence key at http://microblink.com/login or
            // contact us at http://help.microblink.com                   
            mRecognizer.LicenseKey = "Add license key here";

            // add PDF417 & ZXing recognizer settings
            Microblink.PDF417RecognizerSettings pdf417Settings = new Microblink.PDF417RecognizerSettings() {
                InverseScanMode = false,
                NullQuietZoneAllowed = true,
                UncertainScanMode = true
            };
            Microblink.ZXingRecognizerSettings zxingSettings = new Microblink.ZXingRecognizerSettings() {
                QRCode = true
            };
            mRecognizer.RecognizerSettings = new Microblink.IRecognizerSettings[] { pdf417Settings, zxingSettings };

            // these three events must be handled
            mRecognizer.OnCameraError += mRecognizer_OnCameraError;
            mRecognizer.OnScanningDone += mRecognizer_OnScanningDone;
            mRecognizer.OnInitializationError += mRecognizer_OnInitializationError;
            mRecognizer.OnDisplayNewTarget += mRecognizer_OnDisplayNewTarget;
            mRecognizer.OnDisplayDefaultTarget += mRecognizer_OnDisplayDefaultTarget;
            mRecognizer.OnDisplayPointSet += mRecognizer_OnDisplayPointSet;
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
                MessageBox.Show("Could not unlock API! Invalid license key!\nThe application will now terminate!");
                Application.Current.Terminate();
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
            // navigate to results page if type of recognition is SUCCESSFUL
            if (recognitionType == RecognitionType.SUCCESSFUL) {
                // Find barcode results in list of results.
                bool resultFound = false;
                foreach (var result in resultList) {
                    if (result.Valid && !result.Empty) {
                        // check if result is a PDF417 result
                        if (result is Microblink.PDF417RecognitionResult) {
                            // obtain the PDF417 result
                            Microblink.PDF417RecognitionResult pdf417Result = (Microblink.PDF417RecognitionResult)result;
                            // set it as input for results page
                            ResultsPage.dataType = "PDF417";
                            ResultsPage.uncertain = pdf417Result.Uncertain ? "yes" : "no";
                            ResultsPage.raw = pdf417Result.RawData;
                            ResultsPage.rawExt = null;
                            ResultsPage.stringData = pdf417Result.StringData;
                            ResultsPage.stringDataExt = null;
                            // mark as found
                            resultFound = true;
                            break;
                        }
                            // check if result is a ZXing result
                        else if (result is Microblink.ZXingRecognitionResult) {
                            // obtain the ZXing result
                            Microblink.ZXingRecognitionResult zxingResult = (Microblink.ZXingRecognitionResult)result;
                            // set it as input for results page
                            ResultsPage.dataType = zxingResult.BarcodeTypeString;
                            ResultsPage.uncertain = null;
                            ResultsPage.raw = zxingResult.RawData;
                            ResultsPage.rawExt = zxingResult.ExtendedRawData;
                            ResultsPage.stringData = zxingResult.StringData;
                            ResultsPage.stringDataExt = zxingResult.ExtendedStringData;
                            // mark as found
                            resultFound = true;
                            break;
                        }
                    }
                }
                // navigate to results page if some result was found
                if (resultFound) {
                    NavigationService.Navigate(new Uri("/ResultsPage.xaml", UriKind.Relative));
                }
            }
        }

        /// <summary>
        /// Here camera errors should be handled.
        /// </summary>
        /// <param name="error"></param>
        void mRecognizer_OnCameraError(Microblink.UserControls.CameraError error) {
            // just throw an exception
            throw new NotImplementedException();
        }


        public Pdf417ScannerPage() {
            InitializeComponent();
            // set up recognizer
            InitializeRecognizer();
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
            mRecognizer.InitializeControl(this.Orientation);
        }

        /// <summary>
        /// Called when the user leaves this page.
        /// Stops the recognition process.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnNavigatingFrom(NavigatingCancelEventArgs e) {
            // terminate the recognizer
            mRecognizer.TerminateControl();
            // call default behaviour
            base.OnNavigatingFrom(e);
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
            //AnimateButtons(e);
            // orientation change events MUST be forwarded to RecognizerControl
            mRecognizer.OnOrientationChanged(e);
        }

    }
}