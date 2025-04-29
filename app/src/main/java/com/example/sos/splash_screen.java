package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the splash screen to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        // Delay of 1 second before transitioning to HomeActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is logged in
                SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                Intent intent;
                if (isLoggedIn) {
                    // Go to HomeActivity if already logged in
                    intent = new Intent(splash_screen.this, HomeActivity.class);
                } else {
                    // Go to LoginActivity if not logged in
                    intent = new Intent(splash_screen.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();

                // Add transition animation for smooth experience
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 1000);  // 1000ms = 1 second
    }
}
