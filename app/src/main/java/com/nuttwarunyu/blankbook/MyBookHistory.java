package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    CustomAdapter customAdapter;
    private List<StoryBook> storyBookList = null;
    String author;
    Button fbLoginAgain, avangLoginAgain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mybook_history, container, false);
        listView = (ListView) view.findViewById(R.id.listView_user_content);
        fbLoginAgain = (Button) view.findViewById(R.id.fb_login_btn_again);
        avangLoginAgain = (Button) view.findViewById(R.id.avang_login_btn_again);

        ParseUser parseUser = ParseUser.getCurrentUser();
        author = parseUser.getUsername();

        Log.d("myHistory", "author  :  " + author);

        if (author != null) {
            Log.d("myHistory", "author  :  notnull ");
            fbLoginAgain.setVisibility(View.GONE);
            avangLoginAgain.setVisibility(View.GONE);
            new TaskProcess().execute();
        }

        if (author == null) {
            Log.d("myHistory", "author  :  null ");
            fbLoginAgain.setVisibility(View.VISIBLE);
            avangLoginAgain.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class TaskProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("myHistory", "TaskProcess");
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
            customAdapter = new CustomAdapter(getActivity(), storyBookList);
            listView.setAdapter(customAdapter);
        }
    }
}
