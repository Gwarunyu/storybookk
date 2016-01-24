package com.nuttwarunyu.blankbook;

import com.parse.Parse;
import com.parse.ParseFile;

/**
 * Created by Dell-NB on 22/12/2558.
 */
public class StoryBook {
    String title;
    String categories;
    String story;
    String photoFile;
    String author;
    String date;
    String photoAuthor;

    //Get


    public String getPhotoAuthor() {
        return photoAuthor;
    }

    public String getTitle() {
        return title;
    }

    public String getCategories() {
        return categories;
    }

    public String getStory() {
        return story;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    //Set


    public void setPhotoAuthor(String photoAuthor) {
        this.photoAuthor = photoAuthor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
