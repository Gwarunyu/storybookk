package com.nuttwarunyu.blankbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class AddStoryBookActivity extends AppCompatActivity {

    EditText edtTitle, edtStory, edtCategories;
    Button addButtonSave;


    void bindWidget() {
        addButtonSave = (Button) findViewById(R.id.add_button_save);
        edtCategories = (EditText) findViewById(R.id.add_edt_cate);
        edtTitle = (EditText) findViewById(R.id.add_edt_title);
        edtStory = (EditText) findViewById(R.id.add_edt_story);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_book);

        bindWidget();

        addButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject bookStory = new ParseObject("myBookTable");
                bookStory.put("title", edtTitle.getText().toString());
                bookStory.put("categories", edtCategories.getText().toString());
                bookStory.put("story", edtStory.getText().toString());

                bookStory.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            edtStory.setText("");
                            edtTitle.setText("");
                            edtCategories.setText("");
                            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Parse Error", e.toString());
                            Toast.makeText(getApplicationContext(), "Oop! SomethingWrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
