package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ParseObject> parseObjectList;
    ListView listView;
    Button btnAdd;
    ProgressDialog progressDialog;
    CustomAdapter customAdapter;
    private List<StoryBook> storyBookList = null;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        listView = (ListView) findViewById(R.id.listView_newFeed);
        btnAdd = (Button) findViewById(R.id.btn_openAddActivity);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStoryBookActivity.class);
                startActivity(intent);
            }
        });

        Log.d("Tag1", "Before going to TaskProcess");
        new TaskProcess().execute();
    }

    private class TaskProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Parse Loading");
            progressDialog.setMessage("L o a d i n g. .");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            storyBookList = new ArrayList<StoryBook>();

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("myBookTable");
                parseObjectList = query.find();
                for (ParseObject storyData : parseObjectList) {

                    ParseFile image = (ParseFile) storyData.get("photoFile");
                    Log.d("ParseFile image. . .", image + ". . . . . .");

                    StoryBook getBook = new StoryBook();
                    getBook.setTitle((String) storyData.get("title"));
                    getBook.setCategories((String) storyData.get("categories"));
                    getBook.setPhotoFile(image.getUrl());
                    storyBookList.add(getBook);
                    Log.d("doInBackground", "onProcess");
                }
            } catch (ParseException e) {
                Log.d("doInBackground", "Error");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("onPostExecute", " " + storyBookList);
            customAdapter = new CustomAdapter(getApplicationContext(), storyBookList);
            Log.d("onPostExecute", " " + customAdapter);
            listView.setAdapter(customAdapter);
            progressDialog.dismiss();
        }
    }
}
