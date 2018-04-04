package com.team10.trotot.view.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity implements ValueEventListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView textViewUserNameHead;
    TextView textViewUserMail;
    private CircleImageView circleImageView;
    TextView textViewUserName;
    TextView textViewUserSex;
    TextView textViewUserBirth;
    TextView textViewUserCity;
    TextView textViewUserPhone;
    ImageButton imageButtonUserName;
    ImageButton imageButtonUserBirth;
    ImageButton imageButtonUserCity;
    ImageButton imageButtonUserPhone;
    ImageButton imageButtonUserSex;
    Button btnUpdateUserPhoto;
    ArrayList<String> arrayListSex;
    ArrayAdapter<String> arrayAdapterSex;
    Toolbar toolbar;
    User user;
    String USER_ID = "";
    private static final int PICK_IMAGE = 100;
    private Bitmap bitmap;
    View.OnClickListener EventUploadImage = null;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        user = new User();
        USER_ID = ProfileAuthenticationActivity.PROFILE.getUserId();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(USER_ID);
        databaseReference.addValueEventListener(this);

        addControls();
        addEvents();
    }

    private void addEvents() {
        imageButtonUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShow(getString(R.string.profile_user_dialog_title_name),1);
            }
        });
        imageButtonUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSexShow(getString(R.string.profile_user_dialog_title_sex));
            }
        });

        imageButtonUserBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        imageButtonUserCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShow(getString(R.string.profile_user_dialog_title_city),3);

            }
        });
        imageButtonUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShow(getString(R.string.profile_user_dialog_title_phone),4);

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        EventUploadImage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        };

        btnUpdateUserPhoto.setOnClickListener(EventUploadImage);

    }

    private void addControls() {
        circleImageView = findViewById(R.id.imageView_profile_user_avatar);
        textViewUserNameHead = findViewById(R.id.textView_profile_user_name);
        textViewUserMail = findViewById(R.id.textView_profile_user_email);
        textViewUserName = findViewById(R.id.tv_user_name);
        textViewUserBirth = findViewById(R.id.tv_user_birth);
        textViewUserCity = findViewById(R.id.tv_user_city);
        textViewUserPhone = findViewById(R.id.tv_user_phone);
        textViewUserSex = findViewById(R.id.tv_user_sex);
        imageButtonUserName = findViewById(R.id.imageButton_user_name);
        imageButtonUserBirth = findViewById(R.id.imageButton_user_birth);
        imageButtonUserCity = findViewById(R.id.imageButton_user_city);
        imageButtonUserPhone = findViewById(R.id.imageButton_user_phone);
        imageButtonUserSex = findViewById(R.id.imageButton_user_sex);
        btnUpdateUserPhoto = findViewById(R.id.btn_update_user_photo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        //avatar
        String avatarUrl = ProfileAuthenticationActivity.PROFILE.getAvatarUrl();
        if (avatarUrl != "")
            Picasso.with(getApplicationContext())
                    .load(avatarUrl).placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(circleImageView);
        else
            Picasso.with(getApplicationContext()).load(R.drawable.default_avatar).into(circleImageView);
        textViewUserMail.setText(ProfileAuthenticationActivity.PROFILE.getEmail());
        textViewUserNameHead.setText(ProfileAuthenticationActivity.PROFILE.getName());
//        //Load hình từ firebase dùng Glide
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users").child(ProfileAuthenticationActivity.PROFILE.getUserId() + ".png");
//        if (storageReference != null) {
//            GlideApp.with(this)
//                    .load(storageReference)
//                    .into(circleImageView);
//        }
    }

    private void DialogSexShow(String title) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileUserActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_user_sex, null);
        alertDialog.setView(dialogView);

        final Button buttonMale = dialogView.findViewById(R.id.btn_dialog_male);
        final Button buttonFemale = dialogView.findViewById(R.id.btn_dialog_female);
        final Button buttonOther = dialogView.findViewById(R.id.btn_dialog_other);

        alertDialog.setTitle(title);

        final  Dialog dialog = alertDialog.create();

        dialog.show();
        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewUserSex.setText(buttonMale.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("gender").setValue(textViewUserSex.getText().toString());
                dialog.dismiss();
            }
        });

        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewUserSex.setText(buttonFemale.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("gender").setValue(textViewUserSex.getText().toString());
                dialog.dismiss();
            }
        });

        buttonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewUserSex.setText(buttonOther.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("gender").setValue(textViewUserSex.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void DialogShow(String title, final int id) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileUserActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_motel_detail_review_comment, null);
        alertDialog.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.et_dialog_motel_detail_review_comment);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertDialog.setTitle(title);
        alertDialog.setPositiveButton(getString(R.string.home_model_detail_ok), null);
        alertDialog.setNegativeButton(getString(R.string.home_model_detail_cancel), null);

        Dialog dialog = alertDialog.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (editText.getText().length() > 0) {
//                            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000 * 1000);
//                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_CONTENT).setValue(editText.getText().toString());
//                            FirebaseDatabase.getInstance().getReference().child("comments").child(motelID).child(timeStamp).child(FIRE_BASE_STRING_COMMENTS_USER_ID).setValue(userID);
//
                            switch (id)
                            {
                                case 1:
                                    textViewUserName.setText(editText.getText().toString());
                                    textViewUserNameHead.setText(editText.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("name").setValue(editText.getText().toString());
                                    break;
                                case 3:
                                    textViewUserCity.setText(editText.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("city").setValue(editText.getText().toString());
                                    break;
                                case 4:
                                    textViewUserPhone.setText(editText.getText().toString());
                                    FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("phone").setValue(editText.getText().toString());
                                    break;
                            }

                        }
                        dialog.dismiss();

                    }
                });
            }
        });

        dialog.show();
    }

    private void showDatePicker() {
        final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                textViewUserBirth.setText(simpleDateFormat.format(calendar.getTime()));
                FirebaseDatabase.getInstance().getReference().child("users").child(USER_ID).child("birth").setValue(calendar.getTimeInMillis());
            }
        };
        DatePickerDialog datePickerDialog =new DatePickerDialog(
                ProfileUserActivity.this,
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);

        if (user!= null) {
            textViewUserNameHead.setText(user.getName());
            textViewUserName.setText(user.getName());
            if (user.getCity() != "") {
                textViewUserCity.setText(user.getCity());
            }
            else {
                textViewUserCity.setText(getString(R.string.profile_user_city));
            }
            if (user.getPhone() != "") {
                textViewUserPhone.setText(user.getPhone());
            }
            else {
                textViewUserPhone.setText(getString(R.string.profile_user_phone));
            }
            if (user.getGender() != "") {
                textViewUserSex.setText(user.getGender());
            }
            else {
                textViewUserSex.setText(getString(R.string.profile_user_sex));
            }
            Calendar calendar = Calendar.getInstance();

            if (user.getBirth() == 0)
                user.setBirth(System.currentTimeMillis());
            calendar.setTimeInMillis((long)user.getBirth());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            textViewUserBirth.setText(simpleDateFormat.format(calendar.getTime()));
        }
        else {
            textViewUserName.setText(getString(R.string.profile_user_name));
            textViewUserCity.setText(getString(R.string.profile_user_city));
            textViewUserBirth.setText(getString(R.string.profile_user_birth));
            textViewUserPhone.setText(getString(R.string.profile_user_phone));
            textViewUserSex.setText(getString(R.string.profile_user_sex));
            //textViewUserNameHead.setText(user.getName());

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    //Up lên storage và trả về link url
    private void upBitMapOnStorage(Bitmap bitmap, String imgName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        //push hình bằng byte data
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users").child(imgName);
        storageReference.putBytes(data);
    }

    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in the method <code>onActivityResult()</code>
     */
    public void selectImageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     * Retrives the result returned from selecting image, by invoking the method
     * <code>selectImageFromGallery()</code>
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = decodeUri(selectedImage);
                        circleImageView.setImageBitmap(bitmap);
                        //Set hình rồi up lên firebase luôn
                        upBitMapOnStorage(bitmap, user.getUserId() + ".png");
                        Toast.makeText(ProfileUserActivity.this, "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    //Chuyển đường dẫn thành bitmap
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 480;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
