package com.example.sos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.color.DynamicColors;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    CardView registerContact, editMessage, sosguid, helpline, showContact, Info, btnSosService;
    Button langChangeBtn;
    private View loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocale();

        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        setContentView(R.layout.activity_home);

        registerContact = findViewById(R.id.registerContact);
        editMessage = findViewById(R.id.editMessage);
        btnSosService = findViewById(R.id.btnSosService);
        sosguid = findViewById(R.id.sosguid);
        helpline = findViewById(R.id.helpline);
        Info = findViewById(R.id.Info);
        showContact = findViewById(R.id.showContact);
        langChangeBtn = findViewById(R.id.lanchang);
        loginbtn = findViewById(R.id.login);

        registerContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RegisterNumberActivity.class));
            }
        });

        editMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditMessageActivity.class));
            }
        });

        btnSosService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        sosguid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, guide.class));
            }
        });

        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, soscall.class));
            }
        });

        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Instructions.class));
            }
        });

        showContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ShowContact.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        langChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });

    }

    private void showLanguageDialog() {
        final String[] languages = {"English", "हिन्दी", "ગુજરાતી"};
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Select Language")
                .setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String langCode = "en";
                        switch (which) {
                            case 0:
                                langCode = "en";
                                break;
                            case 1:
                                langCode = "hi";
                                break;
                            case 2:
                                langCode = "gu";
                                break;
                        }
                        setLocale(langCode);
                        recreate(); // Restart activity
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("App_Lang", langCode);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("App_Lang", "en");
        setLocale(language);
    }
}
