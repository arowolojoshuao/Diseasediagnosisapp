package com.ecosa.diseasediagnosisapp.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lekan Adigun on 1/15/2018.
 */

@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    private long uuid = 0;

    private int diseaseID = 0;
    private String dateString = "";

    @Ignore
    SimpleDateFormat SMF = new SimpleDateFormat("d/M/yyyy, mm:ss", Locale.getDefault());

    public History() {}

    public History(String id) {
        try {
            diseaseID = Integer.parseInt(id.trim());
            dateString = SMF.format(new Date());
        }catch (Exception e) {}
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public int getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(int diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
