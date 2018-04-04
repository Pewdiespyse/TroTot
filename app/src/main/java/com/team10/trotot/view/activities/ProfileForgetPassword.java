package com.team10.trotot.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.team10.trotot.R;

public class ProfileForgetPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    TextView textViewSubcribe;
    EditText editTextEmail;
    Button buttonSendMail;
    TextView textViewError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_forget_password);

        addControls();
        addEvents();
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textViewSubcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileForgetPassword.this, ProfileSubscribeActivity.class);
                startActivity(i);
            }
        });

        buttonSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                boolean checkMail = CheckMail(email);
                if(checkMail)
                {
                    firebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileForgetPassword.this, "send success",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ProfileForgetPassword.this, "send fail",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else
                {
                    textViewError.setText("Địa chỉ mail không hợp lệ!");
                }

            }
        });
    }

    private boolean CheckMail(String email) {
        return true;
        //return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_authentication);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewSubcribe = findViewById(R.id.textView_subcribe);
        editTextEmail = findViewById(R.id.edittext_forget_pass_email);
        buttonSendMail = findViewById(R.id.button_send_mail);
        textViewError = findViewById(R.id.textView_status);
    }


}
