package com.example.rachid.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rachid on 02/05/2016.
 */
public class VersionActivity extends AppCompatActivity {

    private static final String TAG = "VersionActivity";
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        activity = this;
    }
}