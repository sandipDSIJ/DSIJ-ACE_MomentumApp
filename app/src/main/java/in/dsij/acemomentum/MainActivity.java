package in.dsij.acemomentum;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import in.dsij.acemomentum.db.DbUser;

import in.dsij.acemomentum.dialog.ReportDialogFragment;
import in.dsij.acemomentum.fcm.Config;
import in.dsij.acemomentum.fcm.MyFirebaseInstanceIDService;
import in.dsij.acemomentum.fcm.NotificationUtils;
import in.dsij.acemomentum.fragment.AccountFragment;
import in.dsij.acemomentum.fragment.AlertBottomSheetDialogFragment;
import in.dsij.acemomentum.fragment.ChangePassword;
import in.dsij.acemomentum.fragment.DeviceCheckFragment;
import in.dsij.acemomentum.fragment.DownloadablesBottomSheetDialogFragment;
import in.dsij.acemomentum.fragment.ExitBottomSheetDialogFragment;
import in.dsij.acemomentum.fragment.FeedPagerFragment;
import in.dsij.acemomentum.fragment.ForgotFragment;
import in.dsij.acemomentum.fragment.LiveFeedFragment;
import in.dsij.acemomentum.fragment.LoginFragment;
import in.dsij.acemomentum.fragment.RatingBottomSheetDialogFragment;
import in.dsij.acemomentum.fragment.ResetPasswordConfirmationFragment;
import in.dsij.acemomentum.fragment.SignUpFragment;
import in.dsij.acemomentum.fragment.SignupErrorFragment;
import in.dsij.acemomentum.fragment.SplashFragment;
import in.dsij.acemomentum.fragment.TrackerFragment;
import in.dsij.acemomentum.fragment.TrackerPagerFragment;
import in.dsij.acemomentum.fragment.WebViewFragment;
import in.dsij.acemomentum.net.CallGenerator;
import in.dsij.acemomentum.net.res.ResMessage;
import in.dsij.acemomentum.util.BottomNavigationViewHelper;
import in.dsij.acemomentum.util.CheckForSDCard;
import in.dsij.acemomentum.util.Downloader;
import in.dsij.acemomentum.util.DrawerLocker;
import in.dsij.acemomentum.util.FileDownloader;
import in.dsij.acemomentum.view.CustomTypefaceSpan;
import in.dsij.acemomentum.view.MyTextView;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.Callbacks,
        DeviceCheckFragment.Callbacks, ForgotFragment.Callbacks, ResetPasswordConfirmationFragment.Callbacks,
        SignupErrorFragment.Callbacks, SignUpFragment.Callbacks, DrawerLocker, SplashFragment.Callbacks,
        AccountFragment.Callbacks, ExitBottomSheetDialogFragment.Callbacks, DrawerLayout.DrawerListener,
        RatingBottomSheetDialogFragment.Callbacks, DownloadablesBottomSheetDialogFragment.Callbacks,ReportDialogFragment.Callbacks,ChangePassword.Callbacks {

    private static final String LOG_TAG = "MainActivity";

    public static final String EXTRA_PRODUCT_ID = "MainActivity.Extra.EXTRA_PRODUCT_ID";
    public static final String EXTRA_OPEN_PRODUCTS = "MainActivity.Extra.EXTRA_OPEN_PRODUCTS";
    private static final String DIAG_EXIT = "MainActivity.Dialog.DIAG_EXIT";
    private static final String DIAG_RATE = "MainActivity.Dialog.DIAG_RATE";
    private static final String DIALOG_REPORT = "MainActivity.Dialog.Report";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    /*private ImageView ivInstruction;*/

    private Realm realm;
    private ProgressDialog pDialog;
    //Drawer Menu
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private BottomNavigationView mNavigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            popAllFragments();

            switch (item.getItemId()) {
                case R.id.navigation_calls:
                    setFragment(FeedPagerFragment.newInstance());
                    return true;
                case R.id.navigation_mindshare:
                    setFragment(WebViewFragment.newInstance(G.net.MINDSHARE_URL, "Mindshare"));
                    return true;
                case R.id.navigation_tracker:
                    setFragment(TrackerPagerFragment.newInstance());
                    return true;
                case R.id.navigation_screener:
                    setFragment(WebViewFragment.newInstance(G.net.SCREENER_URL, "Screener"));
                    return true;
                case R.id.navigation_products:
                    setFragment(WebViewFragment.newInstance(G.net.PRODUCTS_URL, "Products"));
                    return true;
            }
            return false;
        }

    };
    private NetworkReceiver receiver;
    private CompoundButton notificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*top_layout=(RelativeLayout)findViewById(R.id.top_layout);*/
        /*ivInstruction=(ImageView)findViewById(R.id.ivInstruction);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

       // FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                    }
                });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(this);

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        /*ivInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_layout.startAnimation(outToRightAnimation(200));
                top_layout.setVisibility(View.GONE);

                //llMain.startAnimation(inFromLeftAnimation(200));
                return;
            }
        });*/

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean enabled = sharedPref.getBoolean(getString(R.string.enable_notifications), false);

        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        MenuItem switchItem;
        switchItem = nav.getMenu().findItem(R.id.drawer_nav_notifications);
        notificationSwitch = (CompoundButton) MenuItemCompat.getActionView(switchItem);
        notificationSwitch.setChecked(enabled);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveNotificationPreference(isChecked);
            }
        });

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(mNavigation);

        /*showBottomNavigation(false);
        setDrawerEnabled(false);*/

//        showActionBar(false);

        /*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setDrawerEnabled(false);*/

        realm = Realm.getDefaultInstance();

        setFragment(SplashFragment.newInstance());

        receiver = new NetworkReceiver();

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(LOG_TAG, "Firebase reg id: " + regId);

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("ACE_mom_FIRST_TIME", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ACE_mom_FIRST_TIME", true);
            editor.commit();
        }
        return !ranBefore;
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/WorkSansRegular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
                Log.w(LOG_TAG, "Tried to unregister the reciver when it's not registered");
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        boolean closeDrawer = true;

        if (id == R.id.drawer_nav_notifications) {

            notificationSwitch.setChecked(!notificationSwitch.isChecked());
            closeDrawer = false;

        } else if (id == R.id.drawer_nav_terms) {
            showTerms();
        } else if (id == R.id.drawer_nav_privacy) {
            showPrivacy();
        } else if (id == R.id.drawer_nav_rate_us) {
            RatingBottomSheetDialogFragment dialogFragment = RatingBottomSheetDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), DIAG_RATE);

        } else if (id == R.id.drawer_nav_about_us) {
            showAbout();
        } else if (id == R.id.drawer_nav_contact_us) {
            showContactUs();
        } else if (id == R.id.drawer_nav_logout) {
            makeLogoutCall();
        }  else if (id == R.id.drawer_nav_report_issue) {
            reportApp();
        } else if (id == R.id.nav_change_password) {
            showBottomNavigation(false);
            setDrawerEnabled(false);
            setFragment(ChangePassword.newInstance());
        } else if (id == R.id.drawer_nav_disclamer) {
            setFragment(WebViewFragment.newInstance(G.net.URL_DISCLAIMER, "Disclaimer"));
        }

        if (closeDrawer) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return false;
    }

    private void saveNotificationPreference(boolean enabled) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.enable_notifications), enabled);
        editor.apply();
    }

    private void reportApp() {

        if (!MyApplication.isConnected()) {
            Snackbar.make(mNavigation, "! Not Available Offline", Snackbar.LENGTH_LONG).show();
            return;
        }
        DbUser dbUser = realm.where(DbUser.class).findFirst();
        ReportDialogFragment reportDialog = ReportDialogFragment.newInstance("Report Issue", null, dbUser.getEmail());
        reportDialog.show(getSupportFragmentManager(), DIALOG_REPORT);

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void postLowRating(int rating, String comments) {
        CallGenerator.postLowRating(rating, comments).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.isSuccessful()) {
                    try {
                        ResMessage resMessage = response.body();

                        Log.v(LOG_TAG, G.net.postLowRating.TAG +
                                G.tag.RESPONSE +
                                new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                        .toJson(resMessage));

                        Log.w(LOG_TAG, G.net.postLowRating.TAG +
                                G.tag.RESPONSE + "Received Empty Message");

                    } catch (Exception e) {
                        Log.w(LOG_TAG, G.net.postLowRating.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                    }
                } else {

                    ResMessage errorMessage = null;

                    try {
                        String errorRes = response.errorBody().string();

                        errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                    } catch (IOException e) {
                        //
                    }

                    Log.w(LOG_TAG, G.net.postLowRating.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                            new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create()
                                    .toJson(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                Log.e(LOG_TAG, G.net.postLowRating.TAG + G.tag.FAILED + "Failed API Call : ", t);
            }
        });
    }

    @Override
    public void openPdf(String title, String url) {
        //  Intent intent = new Intent(Intent.ACTION_VIEW);
        String urlLink=url;
        urlLink=urlLink.replaceAll("~","");

        if(isWriteStoragePermissionGranted() && isReadStoragePermissionGranted())
        {
            new DownloadTask(MainActivity.this, G.net.BASE_URL+urlLink);

        }
    }


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(LOG_TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG, "Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(LOG_TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG,"Permission is granted2");
            return true;
        }
    }


    public void showPdf(String fName)
    {
///storage/emulated/0/NKDROID FILES/pdf_open_parameters.pdf
        File file = new File(Environment.getExternalStorageDirectory()+"/Acemomentum/"+fName);
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent1 = Intent.createChooser(intent, "Open File");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }

    }

    @Override
    public void submitReport(String email, String phone, String description) {

        CallGenerator.postFeedback(email, phone, ": ACE Momentum", description)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            //Todo:  Msg by response code
                            Snackbar.make(mNavigation, "Sent Successfully", Snackbar.LENGTH_SHORT).show();

                        } else {
                            switch (response.code()) {
                                case 400:
                                    break;
                                case 401:
                                    break;
                                case 402:
                                    break;
                                case 403:
                                    break;
                                default:
                                    break;
                            }
                            Log.e("Retrofit", "Error Response code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(LOG_TAG, "Error Post Feedback", t);
                    }
                });

    }

    @Override
    public void onChangePassword() {
        makeLogoutCall();
    }


    public class DownloadTask {

        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
            Log.e(TAG+"fileName", downloadFileName);

            //Start Downloading Task
            new DownloadingTask().execute();
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();
                        String nm=outputFile.getName();
                        Toast.makeText(context, "Downloaded Successfully "+nm, Toast.LENGTH_SHORT).show();
                        //encryptPdf(downloadFileName,downloadFileName);
                        showPdf(downloadFileName);
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    //Change button text if exception occurs

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

                }


                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                                + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {

                        apkStorage = new File(
                                Environment.getExternalStorageDirectory() + "/"
                                        + "Acemomentum");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }

                return null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(LOG_TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(LOG_TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    // downloadPdfFile();
                }else{
                    //progress.dismiss();
                }
                break;

            case 3:
                Log.d(LOG_TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(LOG_TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //SharePdfFile();
                }else{
                    // progress.dismiss();
                }
                break;
        }
    }
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                //connected
//                MyApplication.setConnectionStatus(G.net.NETWORK_AVAILABLE);

                Log.d(LOG_TAG, "Network: " + "Connected");

            } else if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
//                MyApplication.setConnectionStatus(G.net.NETWORK_CONNECTING);

                Log.d(LOG_TAG, "Network: " + "Connecting");

            } else {
                //not connected
//                MyApplication.setConnectionStatus(G.net.NETWORK_DISCONNECTED);

                Log.d(LOG_TAG, "Network: " + "Not Connected");

            }
        }
    }

    private void showBottomNavigation(boolean show) {

        if (show) {
            mNavigation.setVisibility(View.VISIBLE);
        } else {
            mNavigation.setVisibility(View.GONE);
        }

    }

    /** User password. */
    public static byte[] USER = "Hello".getBytes();
    /** Owner password. */
    public static byte[] OWNER = "World".getBytes();

    /* public void encryptPdf(String src, String dest) throws IOException, DocumentException {
         PdfReader reader = new PdfReader(Environment.getExternalStorageDirectory()+"/NKDROID FILES/"+src);
         PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Environment.getExternalStorageDirectory()+"/NKDROID FILES/new1.pdf"));
         stamper.setEncryption(USER, OWNER,
                 PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
 //        stamper.close();
   //      reader.close();
     }*/
    private void gotoLoginFlow() {
        popAllFragments();
        showBottomNavigation(false);
        setDrawerEnabled(false);
        setFragment(LoginFragment.newInstance());
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_content_main, fragment, null)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void makeLogoutCall() {

        CallGenerator.logout()
                .enqueue(new Callback<ResMessage>() {
                    @Override
                    public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                        if (response.isSuccessful()) {
                            try {
                                ResMessage resMessage = response.body();

                                Log.v(LOG_TAG, G.net.logout.TAG +
                                        G.tag.RESPONSE +
                                        new GsonBuilder()
                                                .setPrettyPrinting()
                                                .create()
                                                .toJson(resMessage));

                                Log.w(LOG_TAG, G.net.logout.TAG +
                                        G.tag.RESPONSE + "Received Empty Message");

                            } catch (Exception e) {
                                Log.w(LOG_TAG, G.net.logout.TAG + G.tag.RESPONSE + "Parse response Error: " + response.body(), e);
                            }
                        } else if (response.code() == 403 || response.code() == 404 || response.code() == 500 || response.code() == 503) {
                            // String strError=response.errorBody().toString();
                            Snackbar.make(mNavigation, "Sorry, the server is currently\n unavailable please try agein later", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        else {

                            ResMessage errorMessage = null;

                            try {
                                String errorRes = response.errorBody().string();

                                errorMessage = new GsonBuilder().create().fromJson(errorRes, ResMessage.class);

                            } catch (IOException e) {
                                //
                            }

                            Log.w(LOG_TAG, G.net.loginWithPassword.TAG + G.tag.ERROR + "Error Response code : " + response.code() + " Response Body:\n" +
                                    new GsonBuilder()
                                            .setPrettyPrinting()
                                            .create()
                                            .toJson(errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResMessage> call, Throwable t) {

                    }
                });

        //In Any condition clear Db and Open Login Flow, for user to Sign in with Password
        gotoLoginFlow();

        //Disable Notifications
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.enable_notifications), false);
        editor.apply();

    }

    public void popImmediate() {
        try {
            getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            //do nothing
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            showExitDialog();
        } else {
           try{
               DbUser dbUser = realm.where(DbUser.class).findFirst();
               if(!dbUser.getDisplayName().isEmpty() || dbUser.getDisplayName().length()>1)
               {
                   showBottomNavigation(true);
                   setDrawerEnabled(true);
                   super.onBackPressed();

               } else {
                   super.onBackPressed();
               }
           } catch (Exception e)
           {
               super.onBackPressed();
               e.printStackTrace();
           }

           /* if(getSupportFragmentManager().getFragments().getClass().equals("LoginFragment.class") )
            {
             //   showBottomNavigation(true);
              //  setDrawerEnabled(true);
                super.onBackPressed();
            } else if((getSupportFragmentManager().getFragments().equals(ForgotFragment.class)))
            {
              //  showBottomNavigation(true);
              //  setDrawerEnabled(true);
                super.onBackPressed();
            } else
            super.onBackPressed();*/
        }
    }

    private void showExitDialog() {

        ExitBottomSheetDialogFragment exitBottomSheetDialogFragment = ExitBottomSheetDialogFragment.newInstance();

        exitBottomSheetDialogFragment.show(getSupportFragmentManager(), DIAG_EXIT);

    }

    @Override
    public void logoutFromOthers() {
        gotoMainFlow();
    }

    @Override
    public void onClickSignIn() {
        gotoLoginFlow();
    }

    @Override
    public void onClickResetPassword() {
        setFragment(ForgotFragment.newInstance());
    }

    private void gotoMainFlow() {
//        popAllFragments();

        // TODO: 1/23/2018  setNavigationHeader();
        showBottomNavigation(true);
        setDrawerEnabled(true);

        boolean openProducts = getIntent().getBooleanExtra(EXTRA_OPEN_PRODUCTS, false);

        if (openProducts) {
            mNavigation.setSelectedItemId(R.id.navigation_calls);
        } else {
            mNavigation.setSelectedItemId(R.id.navigation_calls);
        }

    }

    private void setNavigationHeader() {

        /*try {
            ivNavProfile.setImageURI("res:/" + R.mipmap.ic_hollow_white);
            tvNavName.setText(sUser.getPublicationname());
            tvNavEmail.setText(sUser.getEmail());
            tvVersion.setText("V " + MyApplication.getVersionName());
        } catch (Exception e) {
            //do nothing
        }*/
    }

    @Override
    public void loggedIn() {

        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
        gotoMainFlow();

    }

    @Override
    public void exitApp() {
        this.finish();
    }

    @Override
    public void onClickSignUp() {
        popAllFragments();
        setFragment(SignUpFragment.newInstance());
    }

    @Override
    public void onClickForgotPassword() {
        setFragment(ForgotFragment.newInstance());
    }

    @Override
    public void showProgressDialog(String message) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void showOfflineDialog() {
        // TODO: 11/13/2017 Show Dialog

        AlertBottomSheetDialogFragment alertBottomSheetDialogFragment =
                AlertBottomSheetDialogFragment.newInstance("You are offline.\nPlease connect to the internet.",
                        "Settings",
                        "Exit",
                        false);
    }

    /*@Override
    public void showHelpScreen() {
        top_layout.startAnimation(inFromRightAnimation(200));
        top_layout.setVisibility(View.VISIBLE);
    }*/

    private void popAllFragments() {
        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickSendResetLink() {
        popAllFragments();
        setFragment(ResetPasswordConfirmationFragment.newInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void showPrivacy() {
        setFragment(WebViewFragment.newInstance(G.net.PRIVACY_URL, "Privacy Policies"));
    }

    @Override
    public void showTerms() {
        setFragment(WebViewFragment.newInstance(G.net.TERMS_URL, "Terms & Conditions"));
    }

    @Override
    public void showAbout() {
        setFragment(WebViewFragment.newInstance(G.net.ABOUT_US_URL, "About Us"));
    }

    @Override
    public void showContactUs() {

        String email = G.net.EMAIL_CONTACT_US;
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ACE Momentum: Feedback/Enquiry for android app");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello,\n");
        try {
            startActivity(
                    Intent.createChooser(emailIntent, "Send email via : ")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(LOG_TAG, "Email Client Not Found", e);
            Snackbar.make(mNavigation, "! Email Client Not Found", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClickUpdateDetails() {

    }

    public static void showOfflineDialog(boolean connected) {

        if (connected) {

        }

    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);

        if (enabled) {
            setupNavHeader();
        }
    }

    private void setupNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Find Header viees
        View headerView = navigationView.getHeaderView(0);
        MyTextView tvProfileTitles = (MyTextView) headerView.findViewById(R.id.tvProfileTitles);
        MyTextView tvDisplayName=(MyTextView) headerView.findViewById(R.id.tvDisplayName);
        MyTextView tvUsername=(MyTextView)headerView.findViewById(R.id.tvUsername);

        //Get Current user
        DbUser dbUser = realm.where(DbUser.class).findFirst();

        //Set Header Views
        String displayName = dbUser.getDisplayName();
        String[] names = displayName.split(" ");
        String profileTitles;
        try {
            profileTitles = String.valueOf(names[0].charAt(0)) + String.valueOf(names[1].charAt(0));
        } catch (Exception e) {
            profileTitles = String.valueOf(displayName.charAt(0));
        }
        tvProfileTitles.setText(profileTitles);

        tvDisplayName.setText(dbUser.getDisplayName());
        tvUsername.setText(dbUser.getUsername());
    }

    @Override
    public void showActionBar(boolean shown) {
        try {
            if (shown) {
                getSupportActionBar().show();
            } else {
                getSupportActionBar().hide();
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    @Override
    public void showUpButton(boolean shown) {
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(shown);
        } catch (Exception e) {
            //do nothing
        }
    }

    @Override
    public void setViewPagerWithTabBar(ViewPager viewPager) {

    }

    @Override
    public void setActionBarTitle(String title) {

    }


    /*private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {

            String fileUrl = strings[0];
// -> https://letuscsolutions.files.wordpress.com/2015/07/five-point-someone-chetan-bhagat_ebook.pdf
            String fileName = strings[1];
// ->five-point-someone-chetan-bhagat_ebook.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Acemomentum");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hidepDialog();
            Toast.makeText(getApplicationContext(), "Download PDf successfully", Toast.LENGTH_SHORT).show();

            Log.d("Download complete", "----------");
        }
    }*/


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public static Animation outToRightAnimation(long durationMillis) {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        outtoRight.setDuration(durationMillis);
        return outtoRight;
    }

    public static Animation inFromLeftAnimation(long durationMillis) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromLeft.setDuration(durationMillis);
        return inFromLeft;
    }

    public static Animation inFromRightAnimation(long durationMillis) {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(durationMillis);
        return inFromRight;
    }

}