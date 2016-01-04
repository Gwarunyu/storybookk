package com.nuttwarunyu.blankbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.fabric.sdk.android.Fabric;

public class AddStoryBookActivity extends Fragment {

    EditText edtTitle, edtStory, edtCategories;
    ImageView imgPhoto;
    Button addButtonSave;
    Bitmap bitmap;
    private RelativeLayout linearLayout;
    final int REQUEST_IMAGE_SELECTOR = 1;

    void bindWidget() {
        addButtonSave = (Button) linearLayout.findViewById(R.id.add_button_save);
        imgPhoto = (ImageView) linearLayout.findViewById(R.id.imgPhoto);
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

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
            }
        });

        addButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] image = byteArrayOutputStream.toByteArray();

                ParseFile file = new ParseFile("blankBook.png",image);
                file.saveInBackground();

                ParseObject bookStory = new ParseObject("myBookTable");
                bookStory.put("title", edtTitle.getText().toString());
                bookStory.put("categories", edtCategories.getText().toString());
                bookStory.put("story", edtStory.getText().toString());
                bookStory.put("photoFile",file);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECTOR || resultCode == Activity.RESULT_OK) {
            if (bitmap == null) {
                try {
                    InputStream stream = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    imgPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getActivity().getApplicationContext(), "Bitmap Not null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ActivityResult Not OK", Toast.LENGTH_SHORT).show();
        }
    }
}
