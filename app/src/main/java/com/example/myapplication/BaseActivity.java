package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtil.applyTheme(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ThemeUtil.applyTheme(this);
    }
}
