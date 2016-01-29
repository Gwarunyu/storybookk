package com.nuttwarunyu.blankbook;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    List<ParseObject> parseObjectList;
    ListView listView;
    ProgressDialog progressDialog;
    CustomAdapter customAdapter;
    private List<StoryBook> storyBookList = null;
    SwipeRefreshLayout swipeRefreshLayout;

    boolean loadingMore;
    private int limit = 10;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.activity_main, container, false);
        listView = (ListView) swipeRefreshLayout.findViewById(R.id.listView_newFeed);
        swipeRefreshLayout = (SwipeRefreshLayout) swipeRefreshLayout.findViewById(R.id.swipe_view);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150);

        new TaskProcess().execute();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if (lastInScreen == totalItemCount && totalItemCount != 0) {

                }


                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        return swipeRefreshLayout;
    }

    @Override
    public void onRefresh() {
        new RefreshProcess().execute();
    }

    private class RefreshProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            storyBookList = new ArrayList<StoryBook>();

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("myBookTable");
                query.orderByDescending("createdAt");
                parseObjectList = query.find();

                for (ParseObject storyData : parseObjectList) {

                    ParseFile image = (ParseFile) storyData.get("photoFile");
                    ParseFile photoAuthor = (ParseFile) storyData.get("photoAuthor");

                    StoryBook storyBook = new StoryBook();
                    storyBook.setStory((String) storyData.get("story"));
                    storyBook.setTitle((String) storyData.get("title"));
                    storyBook.setCategories((String) storyData.get("categories"));
                    storyBook.setPhotoFile(image.getUrl());
                    storyBook.setPhotoAuthor(photoAuthor.getUrl());
                    storyBook.setAuthor((String) storyData.get("author"));
                    storyBook.setDate((String) storyData.get("currentdate"));
                    storyBookList.add(storyBook);
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
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class TaskProcess extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("AVANG");
            progressDialog.setMessage("L o a d i n g. .");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            storyBookList = new ArrayList<StoryBook>();

            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("myBookTable");
                query.orderByDescending("createdAt");
                parseObjectList = query.find();
                for (ParseObject storyData : parseObjectList) {

                    ParseFile image = (ParseFile) storyData.get("photoFile");
                    ParseFile photoAuthor = (ParseFile) storyData.get("photoAuthor");

                    StoryBook storyBook = new StoryBook();
                    storyBook.setStory((String) storyData.get("story"));
                    storyBook.setTitle((String) storyData.get("title"));
                    storyBook.setCategories((String) storyData.get("categories"));
                    storyBook.setPhotoFile(image.getUrl());
                    storyBook.setPhotoAuthor(photoAuthor.getUrl());
                    storyBook.setAuthor((String) storyData.get("author"));
                    storyBook.setDate((String) storyData.get("currentdate"));
                    storyBookList.add(storyBook);
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
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}
