package com.example.admin.notificationdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button  btnBundleNotication;

    private NotificationManager notificationManager;
    private int bundleNotificationId = 100;
    private int tempNotificationId = 100;
    private NotificationCompat.Builder summaryNotificationBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds();
        setListner();
        setData();
    }

    private void setData() {
        try {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListner() {
        btnBundleNotication.setOnClickListener(MainActivity.this);
    }

    private void getIds() {
        try {
            btnBundleNotication = findViewById(R.id.btnBundleNotication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnBundleNotication:
                    bundleNotification();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bundleNotification() {
        String bundle_notification_id = "bundle_notification_" + bundleNotificationId;

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notification", "Summary Notification Clicked");
        resultIntent.putExtra("notification_id", bundleNotificationId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, bundleNotificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //We need to update the bundle notification every time a new notification comes up.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannels().size() < 2) {
                NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(groupChannel);
                NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
        summaryNotificationBuilder = new NotificationCompat.Builder(this, "bundle_channel_id")
                .setGroup(bundle_notification_id)
                .setGroupSummary(true)
                .setContentTitle("Bundled Notification " + bundleNotificationId)
                .setContentText("Content Text for group summary")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent);


        if (tempNotificationId == bundleNotificationId) {
            tempNotificationId = bundleNotificationId + 1;
        } else {
            tempNotificationId++;
        }

        resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notification", "Single notification clicked");
        resultIntent.putExtra("notification_id", tempNotificationId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultPendingIntent = PendingIntent.getActivity(this,tempNotificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "channel_id")
                .setGroup(bundle_notification_id)
                .setContentTitle("New Notification " + tempNotificationId)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is bundle notification testing demo done by Sakib Syed for tesing purpose only !!!!!!!!!!!!!!!!!")) //This is used for multi line notification.
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroupSummary(false)
                .setContentIntent(resultPendingIntent);

        notificationManager.notify(tempNotificationId, notification.build());
        notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            int notification_id = extras.getInt("notification_id");
            Toast.makeText(getApplicationContext(), "Notification with ID " + notification_id + " is cancelled", Toast.LENGTH_LONG).show();
            notificationManager.cancel(notification_id);
        }
    }
}
