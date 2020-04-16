package com.example.mobileprogramming;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class RVAdapterNotes extends RecyclerView.Adapter<RVAdapterNotes.CardViewObjectHolder> {
    private Context context;
    private List<Notes> notesList;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String SP_PREFERENCES_NAME = "fileNames";
    private String SP_FILE_NAMES = "files";


    public RVAdapterNotes(Context context, List<Notes> notesList){
        this.context = context;
        this.notesList = notesList;
    }

    public class CardViewObjectHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView menu;
        public CardView cardView;
        public  CardViewObjectHolder(View view){
            super(view);
            title = view.findViewById(R.id.cvTitle);
            cardView = view.findViewById(R.id.cardViewNote);
        }
    }

    @NonNull
    @Override
    public CardViewObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_notes,parent,false);
        return new CardViewObjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewObjectHolder holder, int position) {
        final Notes aNote =  notesList.get(position);
        String str = aNote.getFileName();
        String[] arrOfStr = str.split("\\.");
        final String title = arrOfStr[0];
        holder.title.setText(title);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Dosya açma
                File file = new File(Environment.getExternalStorageDirectory(),aNote.getFileName());
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null){
                        text.append(line);
                        text.append("\n");
                    }
                    reader.close();
                }
                catch (Exception e){
                    Toast.makeText(context, "Error: "+e, Toast.LENGTH_SHORT).show();
                }
                aNote.setNote(text.toString());
                showDialog(aNote, file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public void showDialog(final Notes aNote, final File file){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.take_notes_alert_dialog);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        ImageButton btnDelete = (ImageButton) dialog.findViewById(R.id.btnDelete);
        final EditText etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        final EditText etNote = (EditText) dialog.findViewById(R.id.etNote);
        etTitle.setEnabled(false);
        btnSave.setText("Edit");
        btnDelete.setVisibility(View.VISIBLE);
        String str = aNote.getFileName();
        String[] arrOfStr = str.split("\\.");
        String title = arrOfStr[0];
        etTitle.setText(title);
        etNote.setText(aNote.getNote());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    fos.write(etNote.getText().toString().getBytes());
                    fos.close();
                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
                catch (IOException e){
                    Toast.makeText(context, "Error: "+e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
                notesList.remove(aNote);
                sp = context.getSharedPreferences(SP_PREFERENCES_NAME, Context.MODE_PRIVATE);
                editor = sp.edit();
                String files ="";
                if(notesList != null){
                    for(Notes n: notesList){
                        files =  n.getFileName()+" "+files ;
                    }
                }
                editor.putString(SP_FILE_NAMES, files);
                editor.commit();
                dialog.dismiss();
                Intent i = new Intent(context, NotesActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();
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
}
