package com.example.mobileprogramming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvNoNote;
    private FloatingActionButton btnNewNote;
    private RecyclerView recyclerView;
    private List<Notes> notesList;
    private Notes newNote;
    private RVAdapterNotes adapter;
    private Context context = NotesActivity.this;
    private FileOutputStream fos;
    //SHARED PREFERENCES
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String SP_PREFERENCES_NAME = "fileNames";
    private String SP_FILE_NAMES = "files";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initView();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

        //SHARED PREFEFRENCES
        sp = getSharedPreferences(SP_PREFERENCES_NAME, MODE_PRIVATE);
        editor = sp.edit();

        String file = sp.getString(SP_FILE_NAMES,null);

        if(file != null){
            String[] files = file.split(" ");
            for(String name: files){
                Notes aNote = new Notes(name);
                notesList.add(aNote);
            }
        }

        if(!notesList.isEmpty()){
            tvNoNote.setVisibility(View.INVISIBLE);
        }

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RVAdapterNotes(this,notesList);
        recyclerView.setAdapter(adapter);
    }

    public void initView(){
        notesList = new LinkedList<>();
        btnNewNote = findViewById(R.id.btnNewNote);
        tvNoNote = findViewById(R.id.tvNoNote);
        toolbar = findViewById(R.id.tbNotes);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        getSupportActionBar().setTitle("Notes");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    boolean isEmpty (EditText text){
        CharSequence str = text.getText().toString();
        return  TextUtils.isEmpty(str);
    }

    boolean isDifferent(EditText text){
        boolean cntrl = true;
        String inTitle = text.getText().toString();
        for(Notes n: notesList){
            String str = n.getFileName();
            String[] arrOfStr = str.split("\\.");
            String title = arrOfStr[0];
            if(title.equalsIgnoreCase(inTitle)){
                cntrl = false;
            }
        }
        return cntrl;
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.take_notes_alert_dialog);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        final EditText etNote = (EditText) dialog.findViewById(R.id.etNote);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(etTitle) && isDifferent(etTitle)){
                    String title = etTitle.getText().toString().toLowerCase();
                    String note = etNote.getText().toString();
                    newNote = new Notes(title+".txt", title, note);
                    notesList.add(newNote);
                    writeToFile(newNote);
                    String files ="";
                    if(notesList != null){
                        for(Notes n: notesList){
                            files =  n.getFileName()+" "+files ;
                        }
                    }
                    editor.putString(SP_FILE_NAMES,files);
                    editor.commit();
                    dialog.dismiss();
                    Intent i = new Intent(context, NotesActivity.class);
                    startActivity(i);
                    finish();
                }
                else if (isEmpty(etTitle)){
                    etTitle.setError("Please enter a title!");
                }
                else{
                    etTitle.setError("It's already used!");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void writeToFile(Notes newNote){
        String fileName = newNote.getFileName();
        String context = newNote.getNote();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
        try {
            fos = new FileOutputStream(file);
            fos.write(context.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();

        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
        }
    }

}
