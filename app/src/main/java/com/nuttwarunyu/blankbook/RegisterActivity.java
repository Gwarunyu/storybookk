package com.nuttwarunyu.blankbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText registerUser, registerPass1, registerPass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = (Button) findViewById(R.id.regis_button);
        registerUser = (EditText) findViewById(R.id.regis_username);
        registerPass1 = (EditText) findViewById(R.id.regis_password1);
        registerPass2 = (EditText) findViewById(R.id.regis_password2);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerUser.getText().toString().trim().toLowerCase();
                String password = registerPass1.getText().toString();
                String confirmPassword = registerPass2.getText().toString();

                if (password.equals(confirmPassword)) {
                    ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(username);
                    parseUser.setPassword(password);

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainViewPager.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Password not same", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
