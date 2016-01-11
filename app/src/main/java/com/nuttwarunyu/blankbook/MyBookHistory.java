package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MyBookHistory extends Fragment {

    List<ParseObject> parseObjectList;
    ListView listView;
    ProgressDialog progressDialog;
    CustomAdapter customAdapter;
    private List<StoryBook> storyBookList = null;
    String author;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        listView = (ListView) view.findViewById(R.id.listView_user_content);

        ParseUser parseUser = ParseUser.getCurrentUser();
        author = parseUser.getUsername();
        new TaskProcess().execute();

        listView.setAdapter(customAdapter);
        return view;
    }

    private class TaskProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("User Content Loading");
            progressDialog.setMessage("L o a d i n g. .");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            storyBookList = new ArrayList<StoryBook>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("myBookTable");

            query.whereEqualTo("author", author);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Query Author Name . . ", " is . . " + objects.size());

                        if (objects.size() == 0) {
                            Log.d("Query's Value is ", " Null ");
                            return;
                        }


                        for (int i = 0; i < objects.size(); i++) {

                            ParseObject storyData = objects.get(i);
                            ParseFile image = (ParseFile) storyData.get("photoFile");


                            StoryBook storyBook = new StoryBook();
                            storyBook.setStory((String) storyData.get("story"));
                            storyBook.setTitle((String) storyData.get("title"));
                            storyBook.setCategories((String) storyData.get("categories"));
                            storyBook.setPhotoFile(image.getUrl());
                            storyBookList.add(storyBook);
                        }

                    } else {
                        Log.d("object Query ", " object Error : " + e.toString());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            customAdapter = new CustomAdapter(getActivity(), storyBookList);
            Log.d("onPostExecute", "add storyBook in Adapter" + storyBookList);
            //listView.setAdapter(customAdapter);
            progressDialog.dismiss();
        }
    }
}
