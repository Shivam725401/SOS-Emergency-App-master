// MainActivity.java (final updated version for emergency call + SMS)

package com.example.sos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnstart); // Ensure your layout has a button with this ID

        btnStart.setOnClickListener(v -> {
            if (checkPermissions()) {
                startSOSService();
            } else {
                requestPermissions();
            }
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSION_REQUEST_CODE);
    }

    private void startSOSService() {
        Intent serviceIntent = new Intent(this, ServiceMine.class);
        ContextCompat.startForegroundService(this, serviceIntent);

        sendEmergencySMS();
        callAllContacts();
    }

    private void sendEmergencySMS() {
        SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);
        String message = sp.getString("msg", "I need help! This is an emergency.");

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllData();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(2);
                SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
            }
            cursor.close();
        }

        SmsManager.getDefault().sendTextMessage("100", null, message, null, null);
        Toast.makeText(this, "Emergency SMS sent", Toast.LENGTH_SHORT).show();
    }

    private void callAllContacts() {
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllData();

        if (cursor != null && cursor.moveToFirst()) {
            String number = cursor.getString(2);
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(callIntent);
            cursor.close();
        }

        new Handler().postDelayed(() -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:100"));
            startActivity(callIntent);
        }, 5000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startSOSService();
            } else {
                Toast.makeText(this, "All permissions are required for emergency features.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
