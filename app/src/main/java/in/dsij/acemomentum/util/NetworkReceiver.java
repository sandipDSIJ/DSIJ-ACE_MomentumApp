package in.dsij.acemomentum.util;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.dsij.acemomentum.G;
import in.dsij.acemomentum.MyApplication;


public class NetworkReceiver extends BroadcastReceiver {
    public NetworkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //connected
            MyApplication.setConnectionStatus(G.net.NETWORK_AVAILABLE);
        } else if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            MyApplication.setConnectionStatus(G.net.NETWORK_CONNECTING);
        } else {
            //not connected
            MyApplication.setConnectionStatus(G.net.NETWORK_DISCONNECTED);
        }
    }
}
