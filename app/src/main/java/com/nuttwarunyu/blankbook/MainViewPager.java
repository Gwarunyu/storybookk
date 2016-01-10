package com.nuttwarunyu.blankbook;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseUser;

import io.fabric.sdk.android.Fabric;


public class MainViewPager extends AppCompatActivity {

    ViewPager viewPager;
    TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(getApplicationContext(), new Crashlytics());
        setContentView(R.layout.main_view_pager);

        txtUsername = (TextView) findViewById(R.id.txtUsername);

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (ParseUser.getCurrentUser() != null) {
            txtUsername.setText(parseUser.getUsername());
        }
        MyPageViewAdapter myPageViewAdapter = new MyPageViewAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(myPageViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d("TAG MENU : ", "  " + menu);
        getMenuInflater().inflate(R.menu.main_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(MainViewPager.this, "case R.id.action_logout:", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                Toast.makeText(MainViewPager.this, "case R.id.action_profile:", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
