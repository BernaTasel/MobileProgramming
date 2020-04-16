package com.example.mobileprogramming;

import java.io.Serializable;

public class Notes implements Serializable {
    private String fileName;
    private String note;

    public Notes(String fileName) {
        this.fileName = fileName;
    }


    public Notes(String fileName, String title, String note) {
        this.fileName = fileName;
        this.note = note;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
