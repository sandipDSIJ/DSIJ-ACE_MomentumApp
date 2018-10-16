package in.dsij.acemomentum;

/**
 * Created by Vikas on 9/12/2017.
 */

public class G {

    public static class device {
        public static String DEVICE_SERIAL = "unidentified";
        public static String VERSION_NAME = "undefined";
        public static int VERSION_CODE = 0;
        public static long PRODUCT_ID = 202;
        public static final int APP_ID = 61;

    }

    public static class cloud {
        public static final int NEW_CALL_OPEN_APP = 1;
        public static final int NEW_CALL_OPEN_PRODUCT = 2;
        public static final int NEW_CALL_OPEN_LIVE = 3;
        public static final int NEW_CALL_NO_ACTION = 4;

        public static final int NEW_PRODUCT_OPEN_APP = 5;
        public static final int NEW_PRODUCT_OPEN_PRODUCT = 6;
        public static final int NEW_PRODUCT_NO_ACTION = 7;

        public static final int PROMOTION_OPEN_LINK = 8;

        public static final int GENERAL_NOTIFICATION_OPEN_APP = 9;
        public static final int GENERAL_NOTIFICATION_NO_ACTION = 10;
        public static final int IMAGE_URL = 11;
    }

    public static class net {
//          public static final String BASE_URL = "http://52.220.224.255";
        public static final String BASE_URL = "https://www.dsij.in";
        public static final String BASE_URL_DEMO = "http://192.168.1.245";
//        public static final String BASE_URL = "https://da5a16c9-d665-465d-bccb-0b1f93af45a5.mock.pstmn.io";

        public static final String ABOUT_US_URL = "https://www.dsij.in/about-us";
        public static final String PRIVACY_URL = "https://www.dsij.in/privacy-policy";
        public static final String TERMS_URL = "https://www.dsij.in/home/terms";
        public static final String URL_DISCLAIMER = "https://www.dsij.in/disclaimer";

        public static final String EMAIL_CONTACT_US = "rajeshp@dsij.in";

        public static final String MINDSHARE_URL="https://www.dsij.in/AppMindshare";
        public static final String SCREENER_URL="https://www.dsij.in/screenerlanding.aspx";
        public static final String PRODUCTS_URL="https://www.dsij.in/MobileAppProduct/DSIJ_Products.html";
       // public static final String PRODUCTS_URL="http://app.tru-next.com/trunext/productBuySubscriptionInformation.htm";

        public static final int NETWORK_AVAILABLE = 1;
        public static final int NETWORK_CONNECTING = 2;
        public static final int NETWORK_DISCONNECTED = 3;


        public static class loginWithPassword {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/ValidateUser_DSIJ";

            public static final String TAG = "Login with Password";

            public static class success {
                public static final int LOGGED_IN = 200;
                public static final int NEW_VERSION_AVAILABLE = 201;
                public static final int NOT_USING_MINIMUM_VERSION = 202;
            }

            public static class error {
                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int INCORRECT_PASSWORD = 411;
                public static final int USER_LOCKED = 412;
                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;

            }
        }

        public static class loginWithToken {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/CheckLogin_DSIJ";

            public static final String TAG = "Login with Token";

            public static class error {
                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int ALREADY_SIGNED_INTO_OTHER_DEVICE = 411;
                public static final int TOKEN_EXPIRED = 412;
                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;

            }
        }

        public static class logout {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/AppLogout_DSIJ";

            public static final String TAG = "Logout";

            public static class error {
//                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int INVALID_TOKEN = 411;
                //                public static final int TOKEN_EXPIRED = 412;
//                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int INVALID_APP_ID = 417;
                public static final int FAILED_TO_UNREGISTER_CLOUD = 412;

            }
        }

        public static class signUp {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/SignUpDsij_DSIJ";

            public static final String TAG = "Sign Up";

            public static class error {
                //                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int INTERNAL_SERVER_ERROR = 411;
                //                public static final int INTERNAL_SERVER_ERROR = 414;
//                public static final int INTERNAL_SERVER_ERROR = 416;
                //                public static final int ERROR_IN_SENDING_EMAIL = 416;
                public static final int EMAIL_ALREADY_EXISTS = 412;
                public static final int ERROR_IN_SENDING_MAIL = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;

            }
        }

        public static class resetPassword {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/ForgotUserNameORPassword_DSIJ";

            public static final String TAG = "Reset Password";

            public static class error {
                public static final int INVALID_USERNAME = 412;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int INTERNAL_SERVER_ERROR = 414;
                public static final int ERROR_IN_SENDING_EMAIL = 411;
                //                public static final int NOT_AUTHORISED = 413;
                //                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;

            }
        }
        public static class changePassword {

            //public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/DsijMobileAppChangePassword";

            public static final String TAG = "Change Password";

            public static class error {
                public static final int INVALID_USERNAME = 412;
                public static final int INTERNAL_SERVER_ERROR = 410;
                //public static final int INTERNAL_SERVER_ERROR = 414;
                public static final int INVALID_PASSWORD_FORMAT = 411;
                public static final int NOT_AUTHORISED = 413;
                //                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;

            }
        }
        public static class notification {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/RegisterOrDeregisterForPushNotification_DSIJ";

            public static final String TAG = "Notification";

            public static class error {
                /*public static final int INVALID_TOKEN = 413;
                public static final int INVALID_USERNAME = 412;
                public static final int INVALID_GCM_ID = 415;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
                public static final int INTERNAL_SERVER_ERROR = 414;
                //                public static final int ERROR_IN_SENDING_EMAIL = 411;
                //                public static final int NOT_AUTHORISED = 413;
                //                public static final int SERVER_ERROR = 414;

                public static final int EMPTY_PARAMS = 416;*/


                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
//                public static final int INVALID_APP_ID = 403;
//                public static final int FAILED_TO_UNREGISTER = 404;

                public static final int INVALID_GCM_ID = 415;
//                public static final int UNSUPPORTED = 406;

                public static final int SERVER_ERROR = 414;

            }
        }

        public static class getProducts {

            public static final String ENDPOINT = "desktopmodules/services/api/MobileApp/GetInvestorProdutList_DSIJ";

            public static final String TAG = "Get Products";

            public static class error {
                /*public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;*/


                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int EMPTY_PARAMS = 416;
//                public static final int INVALID_APP_ID = 403;
//                public static final int FAILED_TO_UNREGISTER = 404;

//                public static final int EMPTY_GCM_ID = 405;
//                public static final int UNSUPPORTED = 406;

                public static final int SERVER_ERROR = 410;

            }
        }

        public static class getProductBrief {

            public static final String ENDPOINT = "desktopmodules/services/api/MobileApp/GetInvestorProductBrief_DSIJ";
            public static final String TAG = "Get Product Brief";

            public static class error {
                /*public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;*/


                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int EMPTY_PARAMS = 416;
//                public static final int INVALID_APP_ID = 403;
//                public static final int FAILED_TO_UNREGISTER = 404;

//                public static final int EMPTY_GCM_ID = 405;
//                public static final int UNSUPPORTED = 406;

                public static final int PRODUCT_DOES_NOT_EXISTS = 414;

                public static final int SERVER_ERROR = 410;

            }
        }

        public static class getCalls {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetInvestorCallForProducts_DSIJ";

            public static final String TAG = "Get Calls";

            public static class error {

                public static final int INTERNAL_SERVER_ERROR = 410;
                /*public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;

//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int USER_NOT_SUBSCRIBED = 415;*/


                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int NOT_SUBSCRIBED = 415;
                public static final int EMPTY_PARAMS = 416;
//                public static final int INVALID_APP_ID = 403;
//                public static final int FAILED_TO_UNREGISTER = 404;

//                public static final int EMPTY_GCM_ID = 405;
//                public static final int UNSUPPORTED = 406;

                public static final int PRODUCT_DOES_NOT_EXISTS = 411;


                public static final int SERVER_ERROR = 410;

            }
        }

        public static class getDownloadables {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetDownloadablesInvestor_DSIJ";

            public static final String TAG = "Get Downloadables";

        }

        public static class GetAcemomentumFiles {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetInvestorFiles_DSIJ";

            public static final String TAG = "Get Downloadables";

        }

        public static class DownloadFileWithPassword {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/DownloadFileWithPassword_DSIJ";

            public static final String TAG = "Downloadables path";

            public static class error {
                /*public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int USER_NOT_SUBSCRIBED = 415;*/

                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int CALL_DOES_NOT_EXIST = 414;
                public static final int USER_NOT_SUBSCRIBED = 415;


                public static final int NOT_SUBSCRIBED = 410;

                public static final int CALL_DOES_NOT_EXISTS = 414;

                public static final int SERVER_ERROR = 410;

            }
        }

        public static class GetInvestorFiles {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetInvestorFiles_DSIJ";

            public static final String TAG = "Get Downloadables";

            public static class error {
                /*public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int USER_NOT_SUBSCRIBED = 415;*/

                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int CALL_DOES_NOT_EXIST = 414;
                public static final int USER_NOT_SUBSCRIBED = 415;
//                public static final int INVALID_APP_ID = 403;
//                public static final int FAILED_TO_UNREGISTER = 404;

//                public static final int EMPTY_GCM_ID = 405;
//                public static final int UNSUPPORTED = 406;

//                public static final int PRODUCT_DOES_NOT_EXISTS = 407;

                public static final int NOT_SUBSCRIBED = 410;

                public static final int CALL_DOES_NOT_EXISTS = 414;

                public static final int SERVER_ERROR = 410;

            }
        }

        public static class postLowRating {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/SubmitLowRating_DSIJ";

            public static final String TAG = "Post Low Rating";

            public static class error {


                public static final int USERNAME_NOT_REGISTERED = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int INVALID_APP_ID = 415;
                public static final int ERROR_IN_SENDING_MAIL = 411;
//                public static final int FAILED_TO_UNREGISTER = 404;

//                public static final int EMPTY_GCM_ID = 405;
//                public static final int UNSUPPORTED = 406;

//                public static final int PRODUCT_DOES_NOT_EXISTS = 407;

//                public static final int NOT_SUBSCRIBED = 408;

//                public static final int CALL_DOES_NOT_EXISTS = 409;

//                public static final int TRACKER_NOT_AVAILABLE = 411;
//                public static final int ERROR_IN_MAIL_TO_SUPPORT = 412;

                public static final int SERVER_ERROR = 410;

            }
        }

        public static class getTrackerData {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetInvestorCallTracker_DSIJ/";

            public static final String TAG = "Get Tracker Data";

            public static class error {
                public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                public static final int INVALID_APP_ID = 414;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 416;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
//                public static final int EMPTY_PARAMS = 416;
//                public static final int USER_NOT_SUBSCRIBED = 415;

            }
        }

        public static class getLiveCalls {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/GetLiveCall_DSIJ";

            public static final String TAG = "Get Live Calls";

            public static class error {
                public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
//                public static final int INTERNAL_SERVER_ERROR = 411;
//                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int USER_NOT_SUBSCRIBED = 415;
            }
        }
        public static class SubmitFeedback {

            public static final String ENDPOINT = "/desktopmodules/services/api/MobileApp/DsijMobileAppComplaintOrFeedback_DSIJ";

            public static final String TAG = "SubmitFeedback";
            public static final String SUBJECT_LINE = "ACE Momentum: Feedback/Enquiry for android app";
            public static class error {

                public static final int INVALID_USERNAME = 412;
                public static final int INVALID_TOKEN = 413;
                //                public static final int INTERNAL_SERVER_ERROR = 411;
                //                public static final int INTERNAL_SERVER_ERROR = 410;
                //                public static final int ERROR_IN_SENDING_EMAIL = 414;
                //                public static final int NOT_AUTHORISED = 413;
                public static final int SERVER_ERROR = 414;
                //                public static final int USERNAME_NOT_REGISTERED = 415;
                public static final int EMPTY_PARAMS = 416;
                public static final int USER_NOT_SUBSCRIBED = 415;
            }
        }
    }



    public static class tag {
        public static final String REQUEST = " :: REQUEST >> ";
        public static final String RESPONSE = " :: RESPONSE << ";
        public static final String FAILED = " :: FAILED !! ";
        public static final String ERROR = " :: ERROR ?? ";
        public static final String CANCEL = " :: CANCEL $$ ";
        public static final String ERROR_UNKNOWN = " ?? UNKNOWN ERROR ?? ";
        public static final String WARNING = " ** WARNING ** ";

        public static final String DB_TRANSACTION_WRITE = " $$ DB WRITE ";
        public static final String DB_TRANSACTION_READ = " $$ DB READ ";
        public static final String DB_SUCCESS = "SUCCESS ";
        public static final String DB_FAIL = "FAIL ";

        public static final String COUNTLY = " :: COUNTLY :: ";
    }
}
