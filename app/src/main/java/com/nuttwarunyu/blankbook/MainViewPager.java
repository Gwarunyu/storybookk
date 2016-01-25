package com.nuttwarunyu.blankbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import io.fabric.sdk.android.Fabric;


public class MainViewPager extends AppCompatActivity {

    ViewPager viewPager;
    ParseUser parseUser;
    ImageView mProfileImage;
    TitlePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(getApplicationContext(), new Crashlytics());
        setContentView(R.layout.main_view_pager);

        mProfileImage = (ImageView) findViewById(R.id.profile_image_view);

        parseUser = ParseUser.getCurrentUser();
        if (ParseUser.getCurrentUser() != null) {
            Toast.makeText(MainViewPager.this, "Welcome " + parseUser.toString(), Toast.LENGTH_SHORT).show();
        }
        MyPageViewAdapter myPageViewAdapter = new MyPageViewAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(myPageViewAdapter);

        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        //getUserDetailsFromParse();

    }

    private void getUserDetailsFromParse() {
        try {
            ParseFile file = parseUser.getParseFile("profileThumb");
            byte[] data = file.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mProfileImage.setImageBitmap(bitmap);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                if (parseUser != null) {
                    ParseUser.logOut();
                    parseUser = ParseUser.getCurrentUser();

                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            case R.id.action_profile:
                Toast.makeText(MainViewPager.this, "case R.id.action_profile:", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainView OnDestroy","Clear Cache ");
        FileCache fileCache = new FileCache(getApplicationContext());
        fileCache.clear();
    }
}
