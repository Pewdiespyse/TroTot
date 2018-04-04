package com.team10.trotot.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.team10.trotot.R;

public class NewLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loading);

        //DatabaseSyncService.syncLocalToFirebase();

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(500);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(NewLoadingActivity.this,
                            FirstSearchActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
