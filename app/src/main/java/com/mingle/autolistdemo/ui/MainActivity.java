package com.mingle.autolistdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mingle.autolistdemo.R;
import com.nineoldandroids.animation.AnimatorSet;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);


    }


    public void fragmentType(View view) {

        startActivity(new Intent(this, MansFragmentActivity.class));

    }

    public void activityType(View view) {

        startActivity(new Intent(this, MansActivity.class));

    }








}
