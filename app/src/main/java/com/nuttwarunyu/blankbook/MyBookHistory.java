package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
            try {
                parseObjectList = query.find();
                for (ParseObject storyData : parseObjectList) {

                    ParseFile image = (ParseFile) storyData.get("photoFile");

                    StoryBook storyBook = new StoryBook();
                    storyBook.setStory((String) storyData.get("story"));
                    storyBook.setTitle((String) storyData.get("title"));
                    storyBook.setCategories((String) storyData.get("categories"));
                    storyBook.setPhotoFile(image.getUrl());
                    storyBook.setAuthor((String) storyData.get("author"));
                    storyBookList.add(storyBook);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            customAdapter = new CustomAdapter(getActivity(), storyBookList);
            listView.setAdapter(customAdapter);
        }
    }
}
