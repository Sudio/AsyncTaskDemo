package com.example.flyme.asynctaskdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Flyme on 2016/2/8.
 */
public class HandlerActivity extends AppCompatActivity {
    private int title = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTask(), 1, 5000);
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            setTitle("Welcome to Mr Wu's blog" + title);
            title++;
        }
    }
}
