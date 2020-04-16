package com.example.mobileprogramming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SendMailActivity extends AppCompatActivity {

    private EditText etReceiver, etSubject, etMessage;
    private Button btnSend;
    private ImageButton btnAttach;
    private Toolbar toolbar;

    Uri uri = null;
    private static final int PICK_ATTACHMENT = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        initView();

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_ATTACHMENT);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("*/*");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{etReceiver.getText().toString()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,etSubject.getText().toString());
                    emailIntent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                    if (uri != null) {
                        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    startActivity(Intent.createChooser(emailIntent,"Send email"));
                }
                catch (Throwable t){
                    Toast.makeText(SendMailActivity.this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
                    Log.e("mesaj",t.toString() );
                }
            }
        });
    }


    private void initView(){
        etReceiver = findViewById(R.id.etReceiver);
        etReceiver.setHint("example@example.com");
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnAttach = findViewById(R.id.btnAttach);
        toolbar = findViewById(R.id.tbSendMail);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setTitle("Send Mail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_ATTACHMENT && resultCode == RESULT_OK){
            uri = data.getData();
            Toast.makeText(getApplicationContext(),"Attachment Successful", Toast.LENGTH_LONG).show();
        }else if(requestCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"Attachment Unsuccessful", Toast.LENGTH_LONG).show();
        }
    }

}
