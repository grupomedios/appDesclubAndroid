package com.grupomedios.desclub.desclubandroid.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubutil.MCXApplication;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    private long SPLASH_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MCXApplication) getApplication()).inject(this);

        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(),
                        DesclubMainActivity.class);
                startActivity(mainIntent);
                //Destroy the activity to prevent the user to return
                //to this activity by pressing the back button
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_DELAY);
    }
}
