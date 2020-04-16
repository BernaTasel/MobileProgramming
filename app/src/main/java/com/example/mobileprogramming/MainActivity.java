package com.example.mobileprogramming;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String SP_PREFERENCES_NAME = "userInfo";
    private static final String SP_USER_NAME = "userName";

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;

    //SHARED PREFERENCES
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private final int SPLASH_TIME_OUT = 2000;

    Users user1 = new Users("Berna","123");
    Users user2 = new Users("İlayda", "1234");
    Users user3 = new Users("Teyhan","12345");

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        etUserName.setText("Berna");
        etPassword.setText("123");

        //SHARED PREFERENCES
        sp = getSharedPreferences(SP_PREFERENCES_NAME, MODE_PRIVATE);
        editor = sp.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUsersError()) {
                    if(counter < 2) {
                        if (checkPassword(etUserName.getText().toString(), etPassword.getText().toString())) {
                            editor.putString(SP_USER_NAME, etUserName.getText().toString());
                            editor.commit();
                            Intent i = new Intent(MainActivity.this, MenuActivity.class);
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                            finish();

                        } else {
                            etUserName.setText("");
                            etPassword.setText("");
                            Toast.makeText(getApplicationContext(), "Username or password is wrong!", Toast.LENGTH_SHORT).show();
                            counter = counter + 1;
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrong enter 3 times!", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, SPLASH_TIME_OUT);
                    }
                }
            }
        });

    }

    private void initView(){
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    boolean isEmpty (EditText text){
        CharSequence str = text.getText().toString();
        return  TextUtils.isEmpty(str);
    }

    public boolean checkUsersError(){
        boolean isValid = true;
        if(isEmpty(etUserName)){
            etUserName.setError("Username can not be empty.");
            isValid = false;
        }
        if(isEmpty(etPassword)){
            etPassword.setError("Password can not be empty.");
            isValid = false;
        }
        return isValid;
    }

    //TODO: Shared Preferences seçeneğini burdan özelleştirebilirsin
    public boolean checkPassword(String username, String password){
        boolean check = false;

        if(user1.getUserrname().equals(username) && user1.getPassword().equals(password)){

            check = true;
        }

        if(user2.getUserrname().equals(username) && user2.getPassword().equals(password)){
            check = true;
        }
        if(user3.getUserrname().equals(username) && user3.getPassword().equals(password)){
            check = true;
        }
        return check;
    }
}
