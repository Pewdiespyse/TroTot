package com.team10.trotot.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.team10.trotot.R;

/**
 * Created by BUIHO on 11/6/2017.
 */

public class ProfileFeedbackActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextBody;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_feedback);
        addControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_feedback);
//        editTextName = (EditText) findViewById(R.id.editText_feedback_name);
        editTextBody = (EditText) findViewById(R.id.editText_profile_feedback_body);
//        editTextEmail = (EditText) findViewById(R.id.editText_feedback_mail);
//        editTextPhone = (EditText) findViewById(R.id.editText_feedback_phone);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_profile_feedback_send:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + "trotot.teamdev@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from TroTot");
                emailIntent.putExtra(Intent.EXTRA_TEXT, editTextBody.getText().toString());

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "send error", Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
