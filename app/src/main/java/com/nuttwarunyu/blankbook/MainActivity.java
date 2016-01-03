package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment {

    List<ParseObject> parseObjectList;
    ListView listView;
    ProgressDialog progressDialog;
    CustomAdapter customAdapter;
    private List<StoryBook> storyBookList = null;
    private RelativeLayout relativeLayout;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_main, container, false);

        listView = (ListView) relativeLayout.findViewById(R.id.listView_newFeed);

        new TaskProcess().execute();

        return relativeLayout;
    }

    private class TaskProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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

                    StoryBook getBook = new StoryBook();
                    getBook.setStory((String) storyData.get("story"));
                    getBook.setTitle((String) storyData.get("title"));
                    getBook.setCategories((String) storyData.get("categories"));
                    getBook.setPhotoFile(image.getUrl());
                    storyBookList.add(getBook);
                }
            } catch (ParseException e) {
                Log.e("doInBackground", "Error");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            customAdapter = new CustomAdapter(getActivity().getApplicationContext(), storyBookList);
            listView.setAdapter(customAdapter);
            progressDialog.dismiss();
        }
    }
}
