package com.example.sos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);

        loginButton.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String user = usernameEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString().trim();

            // Dummy credentials (replace with real authentication as needed)
            String correctUsername = "admin";
            String correctPassword = "admin123";
            if (user.equals(correctUsername) && pass.equals(correctPassword)) {
                // Save login status (optional)
                SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                prefs.edit().putBoolean("isLoggedIn", true).apply();

                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
