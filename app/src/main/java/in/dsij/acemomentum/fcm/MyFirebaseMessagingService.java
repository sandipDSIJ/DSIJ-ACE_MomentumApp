/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.dsij.acemomentum.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import in.dsij.acemomentum.MainActivity;
import in.dsij.acemomentum.OfferPageActivity;
import in.dsij.acemomentum.R;
import in.dsij.acemomentum.db.DbCall;
import in.dsij.acemomentum.db.DbProduct;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static in.dsij.acemomentum.G.cloud.GENERAL_NOTIFICATION_NO_ACTION;
import static in.dsij.acemomentum.G.cloud.GENERAL_NOTIFICATION_OPEN_APP;
import static in.dsij.acemomentum.G.cloud.IMAGE_URL;
import static in.dsij.acemomentum.G.cloud.NEW_CALL_NO_ACTION;
import static in.dsij.acemomentum.G.cloud.NEW_CALL_OPEN_APP;
import static in.dsij.acemomentum.G.cloud.NEW_CALL_OPEN_LIVE;
import static in.dsij.acemomentum.G.cloud.NEW_CALL_OPEN_PRODUCT;
import static in.dsij.acemomentum.G.cloud.NEW_PRODUCT_NO_ACTION;
import static in.dsij.acemomentum.G.cloud.NEW_PRODUCT_OPEN_APP;
import static in.dsij.acemomentum.G.cloud.NEW_PRODUCT_OPEN_PRODUCT;
import static in.dsij.acemomentum.G.cloud.PROMOTION_OPEN_LINK;


public class  MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = "MyFirebaseMsgService";
    Bitmap bitmap;


    // [START receive_message]

    private void handleDataMessage(JSONObject json){
        Log.e(LOG_TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(LOG_TAG, "title: " + title);
            Log.e(LOG_TAG, "message: " + message);
            Log.e(LOG_TAG, "isBackground: " + isBackground);
            Log.e(LOG_TAG, "payload: " + payload.toString());
            Log.e(LOG_TAG, "imageUrl: " + imageUrl);
            Log.e(LOG_TAG, "timestamp: " + timestamp);

           // sendNotification(title,message, imageUrl, "True");
            sendNotificationData(title,"message");

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            playNotificationSound();
            Map<String, String> message = remoteMessage.getData();
            Intent intent;
            String messageString = message.get("message");
            //String  imageUri=message.get("image");
            Log.d(LOG_TAG, "Message Content: " + messageString);

            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            CloudMessage cloudMessage = new Gson().fromJson(messageString, CloudMessage.class);

            Log.d(LOG_TAG, "Message data payload: " + remoteMessage.getData());

            int cloudMessageType=1;
            try
            {
                cloudMessageType = cloudMessage.getType();
            } catch (Exception e)
            {
                cloudMessageType=1;
                Log.e(LOG_TAG,e.toString());
            }

            switch (cloudMessageType) {

                case NEW_CALL_OPEN_APP:
                    insertCall(cloudMessage);

                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // TODO: 12/20/2017 Parse date and add to description
                    sendNotification(cloudMessage.getNotificationTitle(),  cloudMessage.getNotificationMessage(), intent);

                    break;
                case NEW_CALL_OPEN_PRODUCT:
                    insertCall(cloudMessage);

                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(MainActivity.EXTRA_PRODUCT_ID, cloudMessage.getProductId());
                    intent.putExtra(MainActivity.EXTRA_OPEN_PRODUCTS, true);

                    // TODO: 12/20/2017 Parse date and add to description
                    //sendNotification(cloudMessage.getProductName(),"Recommended: " + cloudMessage.getReccoPrice(), intent);
                    sendNotification(cloudMessage.getNotificationTitle(),  cloudMessage.getNotificationMessage(), intent);

                    break;
                case NEW_CALL_OPEN_LIVE:
                    insertCall(cloudMessage);

                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // TODO: 12/20/2017 Parse date and add to description
                    // sendNotification(cloudMessage.getProductName(),"Recommended: " + cloudMessage.getReccoPrice(), intent);
                    sendNotification(cloudMessage.getNotificationTitle(),  cloudMessage.getNotificationMessage(), intent);

                    break;
                case NEW_CALL_NO_ACTION:
                    insertCall(cloudMessage);

                    // TODO: 12/20/2017 Parse date and add to description
                    // sendNotification(cloudMessage.getProductName(),"Recommended: " + cloudMessage.getReccoPrice(), null);
                    sendNotification(cloudMessage.getNotificationTitle(),  cloudMessage.getNotificationMessage(), null);

                    break;

                case NEW_PRODUCT_OPEN_APP:
                    insertProduct(cloudMessage);

                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    //sendNotification("New", cloudMessage.getProductName(), intent);
                    sendNotification("New",  cloudMessage.getNotificationMessage(), intent);
                    break;
                case NEW_PRODUCT_OPEN_PRODUCT:
                    insertProduct(cloudMessage);

                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(MainActivity.EXTRA_PRODUCT_ID, cloudMessage.getProductId());
                    intent.putExtra(MainActivity.EXTRA_OPEN_PRODUCTS, true);

                    sendNotification("New", cloudMessage.getProductName(), intent);

                    break;
                case NEW_PRODUCT_NO_ACTION:
                    insertProduct(cloudMessage);

                    sendNotification("New", cloudMessage.getProductName(), null);

                    break;

                case PROMOTION_OPEN_LINK:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cloudMessage.getLink()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    sendNotification(cloudMessage.getNotificationTitle(), cloudMessage.getNotificationMessage(), intent);
                    break;

                case GENERAL_NOTIFICATION_OPEN_APP:
                    intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    sendNotification(cloudMessage.getNotificationTitle(), cloudMessage.getNotificationMessage(), intent);
                    break;
                case GENERAL_NOTIFICATION_NO_ACTION:
                    sendNotification(cloudMessage.getNotificationTitle(), cloudMessage.getNotificationMessage(), null);
                    break;
                default:
                    break;
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(LOG_TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void insertCall(CloudMessage cloudMessage) {

        Realm realm;
        realm = Realm.getDefaultInstance();
        try {

            realm.beginTransaction();
            RealmResults<DbCall> mDbCalls;

            mDbCalls = realm.where(DbCall.class).findAllSorted(DbCall.PRIORITY_ID, Sort.ASCENDING);
            Log.d("DB_CallDatabase",mDbCalls.toString());

            DbCall dbCall = realm.createObject(DbCall.class, cloudMessage.getId())
                    .setCompany(cloudMessage.getCompany())
                    .setReccoDate(cloudMessage.getReccoDate())
                    .setReccoPrice(cloudMessage.getReccoPrice())
                    .setTargetPrice(cloudMessage.getReccoPrice())
                    .setCurrentMarketPrice(cloudMessage.getCurrentMarketPrice())
                    .setTemplateID(cloudMessage.getTemplateId())
                    .setPriority(cloudMessage.getPriorityId())
                    .setProductId(cloudMessage.getProductId())
                    .setProductName(cloudMessage.getProductName());

            realm.commitTransaction();

            mDbCalls = realm.where(DbCall.class).findAllSorted(DbCall.PRIORITY_ID, Sort.ASCENDING);
            Log.d("DB_CallDatabase",mDbCalls.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }

    private void insertProduct(CloudMessage cloudMessage) {

        Realm realm;
        realm = Realm.getDefaultInstance();
        try {

            realm.beginTransaction();

            DbProduct dbProduct = realm.createObject(DbProduct.class, cloudMessage.getProductId())
                    .setProductName(cloudMessage.getProductName())
                    .setSubscribed(cloudMessage.isSubscribed())
                    .setTrial(cloudMessage.isTrial())
                    .setNew(cloudMessage.isNew());

            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();

        }
    }

    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    /*private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(LOG_TAG, "Short lived task is done.");
    }


    private void sendNotification(String title, String message, Intent intent) {

        String channelId = getString(R.string.default_notification_channel_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean notify = sharedPref.getBoolean(getString(R.string.enable_notifications), false);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        final int icon = R.mipmap.ic_launcher_ace_momentum;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(channelId, title, importance);
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            notificationManager.createNotificationChannel(mChannel);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_small_notification).setTicker(title).setWhen(0) //your app icon
                    .setBadgeIconType(R.mipmap.ic_launcher_ace_momentum) //your app icon
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                    .setChannelId(channelId)
                    .setStyle(inboxStyle)
                    .setContentTitle(title)
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setNumber(1)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis())
                    .setSound(defaultSoundUri);
            if (intent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }
            if (notify) {
                try {
                    notificationManager.notify(0, notificationBuilder.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_small_notification).setTicker(title).setWhen(0)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                            .setContentTitle(title)
                            .setStyle(inboxStyle)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri);

            if (intent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }
            if (notify) {
                try {
                    if (notificationManager != null) {
                        notificationManager.notify(0, notificationBuilder.build());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void sendNotificationData(String title,String messageBody) {
       // bitmap=getBitmapfromUrl(imageUrl);
        Intent intent = new Intent(this, OfferPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //  intent.putExtra("imageUrl",imageUrl);
        String channelId = getString(R.string.default_notification_channel_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap licon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_ace_momentum);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(licon)/*Notification icon image*/
                .setSmallIcon(R.mipmap.ic_small_notification)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setChannelId(channelId)
                .setStyle(inboxStyle)/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

       /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_small_notification).setTicker(title).setWhen(0) //your app icon
                .setBadgeIconType(R.mipmap.ic_launcher) //your app icon
                .setLargeIcon(licon)
                .setChannelId(channelId)
                .setStyle(bigStyle)
                .setContentTitle(title)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setContentText(message)
                .setWhen(System.currentTimeMillis());*/


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + this.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
