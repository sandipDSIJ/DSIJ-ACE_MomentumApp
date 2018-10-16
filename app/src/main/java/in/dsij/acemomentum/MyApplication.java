package in.dsij.acemomentum;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import in.dsij.acemomentum.util.NetworkReceiver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static final String LOG_TAG = "MyApplication";
    private static boolean connected;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm
        Realm.init(getApplicationContext());

        // Realm configuration
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Fresco.initialize(getApplicationContext());
        // Initialize Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                .withLimit(1000)
                                .build())
                        .build());

        getDeviceDetails();
//check if network available
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //connected
            connected = true;
        } else {
            //not connected
            connected = false;
        }

        //register receiver for runtime network change
        registerReceiver(new NetworkReceiver(),
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void getDeviceDetails() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            G.device.VERSION_NAME = pInfo.versionName;
            G.device.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        G.device.DEVICE_SERIAL = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static boolean isConnected() {
        return connected;
    }
    public static void setConnectionStatus(int status) {

        Log.d("Network", "Network state change");

        if (status == G.net.NETWORK_AVAILABLE) {
            connected = true;
            Log.d(LOG_TAG, "Network Available");
        } else if (status == G.net.NETWORK_CONNECTING) {
            Log.d(LOG_TAG, "Network Connecting");
        } else {
            connected = false;
            Log.d(LOG_TAG, "Network Not Available");
        }
    }
}
