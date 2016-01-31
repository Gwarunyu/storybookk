package com.nuttwarunyu.blankbook;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class SingleStoryViewActivity extends AppCompatActivity {

    ImageView photoUser;
    String singleTitle, singleStory, singleCategories, singlePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story_view);

        TextView txtTitle = (TextView) findViewById(R.id.single_title);
        TextView txtCategories = (TextView) findViewById(R.id.single_categories);
        TextView txtStory = (TextView) findViewById(R.id.single_story);
        ImageView imgPhotoFile = (ImageView) findViewById(R.id.main_backdrop);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        singleTitle = intent.getStringExtra("title");
        singleCategories = intent.getStringExtra("categories");
        singleStory = intent.getStringExtra("story");
        singlePhoto = intent.getStringExtra("photoFile");

        txtTitle.setText(singleTitle);
        txtCategories.setText(singleCategories);
        txtStory.setText(singleStory);

        Glide.with(getApplicationContext()).load(singlePhoto).centerCrop().into(imgPhotoFile);
    }
}
