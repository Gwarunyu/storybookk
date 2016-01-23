package com.nuttwarunyu.blankbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleStoryViewActivity extends AppCompatActivity {

    String singleTitle, singleStory, singleCategories, singlePhoto;
    ImageLoader imageLoader = new ImageLoader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story_view);

        Intent intent = getIntent();
        singleTitle = intent.getStringExtra("title");
        singleCategories = intent.getStringExtra("categories");
        singleStory = intent.getStringExtra("story");
        singlePhoto = intent.getStringExtra("photoFile");

        TextView txtTitle = (TextView) findViewById(R.id.single_title);
        TextView txtCategories = (TextView) findViewById(R.id.single_categories);
        TextView txtStory = (TextView) findViewById(R.id.single_story);

        ImageView imgPhotoFile = (ImageView) findViewById(R.id.main_backdrop);

        txtTitle.setText(singleTitle);
        txtCategories.setText(singleCategories);
        txtStory.setText(singleStory);

        imageLoader.DisplayImage(singlePhoto, imgPhotoFile);

    }
}
