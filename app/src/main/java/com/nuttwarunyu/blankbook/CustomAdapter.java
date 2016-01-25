package com.nuttwarunyu.blankbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell-NB on 22/12/2558.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    private ViewHolder mViewHolder;
    //ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private List<StoryBook> storyBooksList = null;
    private ArrayList<StoryBook> arrayList;

    public CustomAdapter(Context context, List<StoryBook> storyBooksList) {

        this.context = context;
        this.storyBooksList = storyBooksList;
        mInflater = LayoutInflater.from(context);
        this.arrayList = new ArrayList<StoryBook>();
        this.arrayList.addAll(storyBooksList);
        //imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return storyBooksList.size();
    }

    @Override
    public Object getItem(int position) {
        return storyBooksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.story_feed, null);

            //Locate textView in storyFeed
            holder.title = (TextView) view.findViewById(R.id.story_title);
            holder.categories = (TextView) view.findViewById(R.id.story_categories);
            holder.contentThumbnail = (ImageView) view.findViewById(R.id.story_thumbnail);
            holder.author = (TextView) view.findViewById(R.id.story_author);
            holder.userThumbnail = (ImageView) view.findViewById(R.id.story_thumbnail_user);
            holder.date = (TextView) view.findViewById(R.id.story_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(storyBooksList.get(position).getTitle());
        holder.categories.setText(storyBooksList.get(position).getCategories());
        holder.author.setText(storyBooksList.get(position).getAuthor());
        holder.date.setText(storyBooksList.get(position).getDate());

        //set Image
       // imageLoader.DisplayImage(storyBooksList.get(position).getPhotoFile(), holder.contentThumbnail);
       // imageLoader.DisplayImage(storyBooksList.get(position).getPhotoAuthor(), holder.userThumbnail);

        Glide.with(context).load(storyBooksList.get(position).getPhotoFile()).fitCenter().into(holder.contentThumbnail);
        Glide.with(context).load(storyBooksList.get(position).getPhotoAuthor()).fitCenter().into(holder.userThumbnail);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleStoryViewActivity.class);
                intent.putExtra("title", storyBooksList.get(position).getTitle());
                intent.putExtra("categories", storyBooksList.get(position).getCategories());
                intent.putExtra("story", storyBooksList.get(position).getStory());
                intent.putExtra("photoFile", storyBooksList.get(position).getPhotoFile());
                intent.putExtra("photoAuthor", storyBooksList.get(position).getPhotoAuthor());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class ViewHolder {
        ImageView contentThumbnail;
        TextView title;
        TextView author;
        TextView categories;
        ImageView userThumbnail;
        TextView date;
    }
}
