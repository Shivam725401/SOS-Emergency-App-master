package com.example.sos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class soscall extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    Button but1, but2, but3, but4, but5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soscall);

        but1 = findViewById(R.id.callambu);
        but2 = findViewById(R.id.callfire);
        but3 = findViewById(R.id.callpol);
        but4 = findViewById(R.id.calldis);
        but5 = findViewById(R.id.callgas);

        but1.setOnClickListener(v -> makeCall("108"));
        but2.setOnClickListener(v -> makeCall("104"));
        but3.setOnClickListener(v -> makeCall("100"));
        but4.setOnClickListener(v -> makeCall("104"));
        but5.setOnClickListener(v -> makeCall("1906"));
    }

    private void makeCall(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perms, int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        if (code == REQUEST_CALL) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Call permission granted", Toast.LENGTH_SHORT).show();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission required")
                        .setMessage("We need call permission to reach emergency services.")
                        .setPositiveButton("CONFIRM", (d, w) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL))
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission denied")
                        .setMessage("Enable CALL permission in settings to use this feature.")
                        .setPositiveButton("PROCEED", (d, w) -> openSettings())
                        .setNegativeButton("CLOSE", null)
                        .show();
            }
        }
    }

    private void openSettings() {
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivity(i);
    }
}
