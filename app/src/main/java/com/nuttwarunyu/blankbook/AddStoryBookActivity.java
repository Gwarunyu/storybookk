package com.nuttwarunyu.blankbook;

import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.fabric.sdk.android.Fabric;

public class AddStoryBookActivity extends Fragment {

    EditText edtTitle, edtStory, edtCategories;
    Button addButtonSave;
    private RelativeLayout linearLayout;
    //private FragmentActivity fragmentActivity;

    void bindWidget() {
        addButtonSave = (Button) linearLayout.findViewById(R.id.add_button_save);
        edtCategories = (EditText) linearLayout.findViewById(R.id.add_edt_cate);
        edtTitle = (EditText) linearLayout.findViewById(R.id.add_edt_title);
        edtStory = (EditText) linearLayout.findViewById(R.id.add_edt_story);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        linearLayout = (RelativeLayout) inflater.inflate(R.layout.activity_add_story_book, container, false);

       Fabric.with(getActivity().getApplicationContext(), new Crashlytics());
        bindWidget();

        edtStory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    edtStory.setGravity(Gravity.LEFT | Gravity.TOP);
                } else {
                    edtStory.setGravity(Gravity.CENTER);
                }
            }
        });
        edtStory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edtStory.setGravity(Gravity.CENTER);
                } else {

                }
            }
        });

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
                            Toast.makeText(getActivity().getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Parse Error", e.toString());
                            Toast.makeText(getActivity().getApplicationContext(), "Oop! SomethingWrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return linearLayout;
    }
}
