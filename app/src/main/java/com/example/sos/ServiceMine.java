package com.example.sos;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class ServiceMine extends Service {

    DatabaseHelper databaseHelper;
    LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startForegroundService();
        sendEmergencyAlert();
    }

    private void startForegroundService() {
        String channelId = "sos_channel";
        String channelName = "Emergency Channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Emergency Mode Active")
                .setContentText("Sending SOS alerts and calls...")
                .setSmallIcon(R.drawable.help_icon)
                .build();

        startForeground(1, notification);
    }

    private void sendEmergencyAlert() {
        ArrayList<ContactModel> contacts = databaseHelper.fetchAllContacts();
        String message = getSavedMessage();
        String location = getCurrentLocation();

        if (location != null) {
            message += "\nLocation: " + location;
        }

        for (ContactModel contact : contacts) {
            sendSMS(contact.getNumber(), message);
            makeCall(contact.getNumber());
        }

        sendSMS("100", message);  // Police
        makeCall("100");
    }

    private String getSavedMessage() {
        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        return sp.getString("msg", "I am in emergency! Please help.");
    }

    private String getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            return "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
        }
        return null;
    }

    private void sendSMS(String number, String msg) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
        }
    }

    private void makeCall(String number) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY; // Don't restart service after it’s killed
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
