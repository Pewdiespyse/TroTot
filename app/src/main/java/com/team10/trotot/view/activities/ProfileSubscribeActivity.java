package com.team10.trotot.view.activities;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.view.supports.PrefUtil;

import static com.team10.trotot.view.activities.ProfileAuthenticationActivity.PROFILE;

public class ProfileSubscribeActivity extends AppCompatActivity  implements FirebaseAuth.AuthStateListener {

    Toolbar toolbar;
    Button btnDangNhap;
    Button buttonSubscribe;
    EditText editTextMail;
    EditText editTextPass;
    EditText editTextConfirm;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    TextView textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_subscribe);
        mAuth = FirebaseAuth.getInstance();
        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSubscribeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSubscribeActivity.this, ProfileAuthenticationActivity.class);
                startActivity(i);
            }
        });

        buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = editTextMail.getText().toString();
                String pass = editTextPass.getText().toString();
                String confirm = editTextConfirm.getText().toString();
                if(pass.equals(confirm))
                {
                    createAccount(mail,pass);
                }else
                {
                    textViewStatus.setText("Password không trùng khớp");
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_subcribe);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnDangNhap = findViewById(R.id.btnSignIn);
        buttonSubscribe = findViewById(R.id.button_subscribe);
        editTextMail = findViewById(R.id.editText_subscribe_email);
        editTextPass = findViewById(R.id.editText_subscribe_password);
        editTextConfirm = findViewById(R.id.editText_subscribe_password_confirm);
        textViewStatus = findViewById(R.id.textview_status);
    }

    private void createAccount(String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            PrefUtil.set(getApplicationContext(), PREF_STRING.USER_ID,user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ProfileSubscribeActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth != null)
        {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                ProfileAuthenticationActivity.isLogin = true;
                PROFILE.setEmail(user.getEmail());
                //PROFILE.setPhone(user.getPhoneNumber());
                PROFILE.setUserId(user.getUid());
                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("mail").setValue(user.getEmail());
                Intent iUpdateUser = new Intent(ProfileSubscribeActivity.this, MainActivity.class);
                startActivity(iUpdateUser);
            }

        }
    }
}
