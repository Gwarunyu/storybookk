package com.nuttwarunyu.blankbook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.Signature;
import java.util.Collection;

import io.fabric.sdk.android.Fabric;


public class LogInActivity extends AppCompatActivity {

    //Button loginBtn;
    //EditText loginUsername, loginPassword;
    Button loginRegister;
    Permission permission;
    ParseUser parseUser;
    ImageView mProfileImage;
    String email, name;
    TextInputLayout inputUsername, inputPassword;

    private void bindWidget() {

        loginRegister = (Button) findViewById(R.id.login_register);
        mProfileImage = (ImageView) findViewById(R.id.mProfileImage);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Fabric.with(getApplicationContext().getApplicationContext(), new Crashlytics());
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_log_in);

        bindWidget();

        Button fbLoginBtn = (Button) findViewById(R.id.fb_login_btn);

        if (ParseUser.getCurrentUser() != null) {
            parseUser = ParseUser.getCurrentUser();
            Log.d("LogInActivity ", " By ParseUser.getCurrentUser() != null");
            GoToMainViewPager();
        }

        fbLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookLogIn();
            }
        });

        /*loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
                Log.d("LogInActivity ", " ParseUser.logInInBackground");
            }
        });*/

        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

   /* private void checkLogin() {

        String username = loginUsername.getText().toString().trim().toLowerCase();
        String password = loginPassword.getText().toString().trim();

        if (username.equals("") || password.equals("")){
            inputUsername.setError(null);
            inputPassword.setError(null);
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    GoToMainViewPager();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    private void openFacebookLogIn() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LogInActivity.this,
                (Collection<String>) permission,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB();
                            GoToMainViewPager();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            //getUserDetailsFromParse();
                            GoToMainViewPager();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserDetailsFromFB() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    name = response.getJSONObject().getString("name");

                    Log.d("GraphRequest ", " is response.... Name :" + name + "  email :" + email);

                    JSONObject picture = response.getJSONObject().getJSONObject("picture");
                    Log.d("GraphRequest", " getPicture is : " + picture);
                    JSONObject data = picture.getJSONObject("data");

                    //  Returns a 50x50 profile picture
                    String pictureUrl = data.getString("url");
                    new ProfilePhotoAsync(pictureUrl).execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();
    }

    class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        String url;

        public ProfilePhotoAsync(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            bitmap = DownloadImageBitmap(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProfileImage.setImageBitmap(bitmap);
            saveNewUser();
        }
    }

    private void saveNewUser() {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        //TODO  parseUser.setEmail(email);

        Log.d("saveNewUser()", "Process");
//        Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("profileThumb", parseFile);
                //Finally save all the user details
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "New user Signed up", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            Log.d("DownloadImageBitmap", "Process");
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();

        //Fetch profile photo
        try {
            ParseFile file = parseUser.getParseFile("profileThumb");
            byte[] data = file.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mProfileImage.setImageBitmap(bitmap);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loginRegister.setText(parseUser.getUsername());

    }

    private void GoToMainViewPager() {
        Intent intent = new Intent(getApplicationContext(), MainViewPager.class);
        startActivity(intent);
        finish();
    }

}

