package com.team10.trotot.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.model.basic_classes.User;
import com.team10.trotot.view.supports.PrefUtil;

/**
 * Created by Pewdiespyse on 02/11/2017.
 */

public class ProfileAuthenticationActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
    public static boolean isLogin = false;
    static FirebaseAuth mAuth;
    FirebaseUser user;
    //Google
    Button btnDangNhapGoogle;
    private static GoogleSignInClient mGoogleSignInClient;
    public static int CODE_LOGIN_GOOGLE = 3;
    //Facebook

    LoginButton btnDangNhapFacebook;
    CallbackManager callbackManager;

    Button btnTaoTaiKhoan;
    Button buttonSignin;
    EditText editTextMail;
    EditText editTextPass;
    TextView textViewForgetPass;
    public static int TYPE_LOGIN = 1;
    private static String USER_ID = "";
    public static User PROFILE = new User();
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_authentication);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());

        CreateClientLoginGoogle();
        mAuth = FirebaseAuth.getInstance();

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

        btnDangNhapGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TYPE_LOGIN = 1;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, CODE_LOGIN_GOOGLE);

            }
        });

        btnDangNhapFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                TYPE_LOGIN = 2;
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btnDangNhapFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnTaoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileAuthenticationActivity.this, ProfileSubscribeActivity.class);
                startActivity(i);
            }
        });

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = editTextMail.getText().toString();
                String pass = editTextPass.getText().toString();
                signIn(mail,pass);
            }
        });
        textViewForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileAuthenticationActivity.this, ProfileForgetPassword.class);
                startActivity(i);
            }
        });
    }

    private void addControls() {
        editTextMail = findViewById(R.id.editText_email);
        editTextPass = findViewById(R.id.editText_password);
        buttonSignin = findViewById(R.id.button_signin);
        textViewForgetPass = findViewById(R.id.textview_forget_pass);

        btnTaoTaiKhoan = (Button) findViewById(R.id.btnSubscribe);
        //Google
        btnDangNhapGoogle = (Button) findViewById(R.id.btnGoogleSignIn);
        //Facebook
        btnDangNhapFacebook = (LoginButton) findViewById(R.id.btnFacebookSignIn);
        btnDangNhapFacebook.setReadPermissions("email", "public_profile");

        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_authentication);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //logout
    public static void LogOut(){
        isLogin = false;
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut();

        //Facebook
        LoginManager.getInstance().logOut();

//
        //Google



    }

    //tạo client đăng nhập google
    private void CreateClientLoginGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Google
        if (requestCode == CODE_LOGIN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
            }
        }
        else {
            //Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            USER_ID = user.getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ProfileAuthenticationActivity.this, getString(R.string.profile_login_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            USER_ID = user.getUid();

                        } else {
                            Toast.makeText(ProfileAuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            USER_ID = user.getUid();
                            Toast.makeText(ProfileAuthenticationActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ProfileAuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    protected void onStart() {
        super.onStart();
        //PrefUtil.set(getApplicationContext(), PREF_STRING.USER_ID, USER_ID);
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefUtil.set(getApplicationContext(), PREF_STRING.USER_ID, USER_ID);
        mAuth.removeAuthStateListener(this);

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            isLogin = true;
            //Đăng nhập thành công
            USER_ID = user.getUid();
            Toast.makeText(ProfileAuthenticationActivity.this, getString(R.string.profile_login_success), Toast.LENGTH_SHORT).show();
            PROFILE.setName(user.getDisplayName());
            if(user.getPhotoUrl() != null)
            {
                PROFILE.setAvatarUrl(user.getPhotoUrl().toString());
            }
            PROFILE.setEmail(user.getEmail());
            //PROFILE.setPhone(user.getPhoneNumber());
            PROFILE.setUserId(user.getUid());
            if(user.getDisplayName() != "")
            {
                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("name").setValue(user.getDisplayName());
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("mail").setValue(user.getEmail());
            PrefUtil.set(getApplicationContext(), PREF_STRING.USER_ID,user.getUid());

            Intent iUpdateUser = new Intent(ProfileAuthenticationActivity.this, MainActivity.class);
            startActivity(iUpdateUser);
        }
        else
            isLogin = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
