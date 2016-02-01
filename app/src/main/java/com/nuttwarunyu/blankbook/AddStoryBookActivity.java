package com.nuttwarunyu.blankbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class AddStoryBookActivity extends AppCompatActivity {

    EditText edtTitle, edtStory;
    ImageView imgPhoto;
    Bitmap bitmap;
    String categories = "ประสบการณ์";
    final int REQUEST_IMAGE_SELECTOR = 1;
    RadioGroup radioGroup;
    RadioButton radioButtonMyExp, radioButtonUnExp;
    String username, curDate, titleSave, storySave;
    ParseFile photoAuthor;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    void bindWidget() {
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        edtTitle = (EditText) findViewById(R.id.add_edt_title);
        edtStory = (EditText) findViewById(R.id.add_edt_story);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonMyExp = (RadioButton) findViewById(R.id.radio_myExp);
        radioButtonUnExp = (RadioButton) findViewById(R.id.radio_unExp);
    }

    void setTypeface(){

        Typeface myFont = Typeface.createFromAsset(getAssets(), "CSPraJad.otf");
        Typeface myFontBold = Typeface.createFromAsset(getAssets(), "CSPraJad-bold.otf");

        edtTitle.setTypeface(myFontBold);
        edtStory.setTypeface(myFont);
        radioButtonMyExp.setTypeface(myFont);
        radioButtonUnExp.setTypeface(myFont);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story_book);

        Fabric.with(getApplicationContext(), new Crashlytics());

        ParseUser parseUser = ParseUser.getCurrentUser();
        username = parseUser.getUsername();
        curDate = setCurrentDate();
        photoAuthor = parseUser.getParseFile("profileThumb");

        bindWidget();
        setTypeface();

        sharedPreferences = getSharedPreferences("saveDraft", Context.MODE_PRIVATE);
        titleSave = sharedPreferences.getString("titleSave", "");
        storySave = sharedPreferences.getString("storySave", "");

        edtTitle.setText(titleSave);
        edtStory.setText(storySave);

        Log.d("onCreate", "titleSave : " + titleSave + " storySave : " + storySave);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_myExp) {
                    categories = "ประสบการณ์";
                }
                if (checkedId == R.id.radio_unExp) {
                    categories = "เรื่องเล่า";
                }
            }
        });

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        titleSave = edtTitle.getText().toString();
        storySave = edtStory.getText().toString();
        Log.d("onPause", "titleSave : " + titleSave + " storySave : " + storySave);
        saveDraft();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        titleSave = edtTitle.getText().toString();
        storySave = edtStory.getText().toString();
        Log.d("onDestroy", "titleSave : " + titleSave + " storySave : " + storySave);
        saveDraft();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you wanna Draft your story");
        builder.setMessage("Are you sure you want to Exit?");
        builder.setIcon(R.drawable.logo);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtStory.setText("");
                edtTitle.setText("");
                AddStoryBookActivity.super.onBackPressed();
            }
        });
        builder.create().show();
    }

    private void saveDraft() {
        editor = sharedPreferences.edit();
        editor.putString("titleSave", titleSave);
        editor.putString("storySave", storySave);
        Log.d("SaveDraft", "titleSave : " + titleSave + " storySave : " + storySave);
        editor.apply();
    }

    private void saveStory() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();

        ParseFile file = new ParseFile("blankBook.png", image);

        ParseObject bookStory = new ParseObject("myBookTable");
        bookStory.put("title", edtTitle.getText().toString());
        bookStory.put("categories", categories);
        bookStory.put("story", edtStory.getText().toString());
        bookStory.put("photoFile", file);
        bookStory.put("photoAuthor", photoAuthor);
        bookStory.put("author", username);
        bookStory.put("currentdate", curDate);

        bookStory.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Parse Error", e.toString());
                    Toast.makeText(getApplicationContext(), "Oop! SomethingWrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder currentDate = new StringBuilder().append(day).append("-").append(month + 1)
                .append("-").append(year);

        return String.valueOf(currentDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECTOR || resultCode == Activity.RESULT_OK) {
            if (bitmap == null) {
                try {
                    InputStream stream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    //imgPhoto.setImageBitmap(bitmap);
                    Glide.with(getApplicationContext()).load(bitmap).centerCrop().into(imgPhoto);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "Bitmap Not null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "ActivityResult Not OK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                if (edtTitle.getText().toString().trim().length() == 0 || edtStory.getText().toString().trim().length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please write your story", Toast.LENGTH_SHORT).show();
                    return false;
                }
                new saveStoryBtn().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class saveStoryBtn extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddStoryBookActivity.this);
            progressDialog.setTitle("AVANG");
            progressDialog.setMessage("Saving. .");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveStory();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            edtStory.setText("");
            edtTitle.setText("");
            bitmap = null;
            progressDialog.dismiss();
            sharedPreferences.edit().clear().apply();
        }
    }
}
