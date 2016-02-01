package com.nuttwarunyu.blankbook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText registerUser, registerPass1, registerPass2;
    ImageView registerPhoto;
    final int REQUEST_IMAGE_SELECTOR = 1;
    Bitmap bitmap;

    void setTypeface() {

        Typeface myFont = Typeface.createFromAsset(getAssets(), "CSPraJad.otf");
        Typeface myFontBold = Typeface.createFromAsset(getAssets(), "CSPraJad-bold.otf");
        registerBtn.setTypeface(myFontBold);
        registerUser.setTypeface(myFont);
        registerPass1.setTypeface(myFont);
        registerPass2.setTypeface(myFont);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = (Button) findViewById(R.id.regis_button);
        registerUser = (EditText) findViewById(R.id.regis_username);
        registerPass1 = (EditText) findViewById(R.id.regis_password1);
        registerPass2 = (EditText) findViewById(R.id.regis_password2);
        registerPhoto = (ImageView) findViewById(R.id.regis_photo);

        setTypeface();

        registerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = registerUser.getText().toString().trim().toLowerCase();
                String password = registerPass1.getText().toString();
                String confirmPassword = registerPass2.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap photoFileBitmap = ((BitmapDrawable) registerPhoto.getDrawable()).getBitmap();
                photoFileBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] data = stream.toByteArray();
                String thumbName = username.replaceAll("\\s+", "");
                final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);

                if (password.equals(confirmPassword)) {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(username);
                    parseUser.setPassword(password);

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                parseUser.put("profileThumb", parseFile);
                                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainViewPager.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Password not same", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_SELECTOR || resultCode == Activity.RESULT_OK) {
            if (bitmap == null) {
                try {
                    InputStream stream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    //registerPhoto.setImageBitmap(bitmap);
                    Glide.with(getApplicationContext()).load(bitmap).fitCenter().into(registerPhoto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
