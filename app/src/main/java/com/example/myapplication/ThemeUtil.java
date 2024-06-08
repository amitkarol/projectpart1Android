package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ThemeUtil {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_NIGHT_MODE = "night_mode";
    public static final String THEME_CHANGED_ACTION = "com.example.myapplication.THEME_CHANGED";

    public static void applyTheme(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isNightMode = preferences.getBoolean(KEY_NIGHT_MODE, false);
        if (isNightMode) {
            activity.setTheme(R.style.NightTheme);
        } else {
            activity.setTheme(R.style.DayTheme);
        }

        // Apply text color change
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        changeTextColor(rootView, isNightMode);
    }

    public static void setNightMode(Context context, boolean isNightMode) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, isNightMode);
        editor.apply();

        Intent intent = new Intent(THEME_CHANGED_ACTION);
        context.sendBroadcast(intent);
    }

    public static boolean isNightMode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_NIGHT_MODE, false);
    }

    public static void changeTextColor(View view, boolean isNightMode) {
        int textColor = isNightMode ? view.getResources().getColor(R.color.white) : view.getResources().getColor(R.color.black);
        int hintTextColor = isNightMode ? view.getResources().getColor(R.color.white) : view.getResources().getColor(R.color.black);

        changeTextColor(view, textColor, hintTextColor);
    }

    public static void changeTextColor(View view, int textColor, int hintTextColor) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                changeTextColor(child, textColor, hintTextColor);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(textColor);
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(textColor);
            ((EditText) view).setHintTextColor(hintTextColor);
        }
    }
}
