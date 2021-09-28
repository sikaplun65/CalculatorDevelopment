package com.example.calculatordevelopment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class ChangeThemeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_theme_layout);

        findViewById(R.id.linght_theme_button).setOnClickListener(v -> { ;
            finishWithResult("light");
        });

        findViewById(R.id.dark_theme_button).setOnClickListener(v -> {
            finishWithResult("dark");
        });
    }

    private void finishWithResult(String theme) {
        Intent intent = new Intent();
        intent.putExtra("theme", theme);
        setResult(RESULT_OK, intent);

        //SharedPreferences.setCurrentTheme("light");

        finish();
    }
}
