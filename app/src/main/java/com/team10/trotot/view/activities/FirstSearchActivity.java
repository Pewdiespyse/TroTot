package com.team10.trotot.view.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.team10.trotot.R;

import java.util.ArrayList;
import java.util.Locale;

public class FirstSearchActivity extends AppCompatActivity {
    FloatingActionButton btnFirstSearch;
    ImageButton btnSpeak;
    ImageButton searchCurrentLocation;
    EditText editTextFirstSearch;
    final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_search);

        btnSpeak = (ImageButton)findViewById(R.id.imageButton_first_search_speech);
        editTextFirstSearch = (EditText)findViewById(R.id.editText_first_search);
        searchCurrentLocation = (ImageButton)findViewById(R.id.imageButton_first_search_location);

        btnFirstSearch = (FloatingActionButton) findViewById(R.id.btnFirstSearch);
        btnFirstSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUpdateUser = new Intent(FirstSearchActivity.this, MainActivity.class);
                String valueTextSearch = editTextFirstSearch.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("valueTextSearch", valueTextSearch);
                iUpdateUser.putExtras(bundle);
                startActivity(iUpdateUser);
            }
        });

        searchCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUpdateUser = new Intent(FirstSearchActivity.this, MainActivity.class);
                String valueTextSearch = "";
                Bundle bundle = new Bundle();
                bundle.putString("valueTextSearch", valueTextSearch);
                iUpdateUser.putExtras(bundle);
                startActivity(iUpdateUser);
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Đang lắng nghe ...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Thiết bị không hỗ trợ mic!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextFirstSearch.setText(result.get(0));
                }
                break;
            }

        }
    }
}
