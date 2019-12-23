package com.ecosa.diseasediagnosisapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class Symptom {

    private String diseaseID = "";
    private String name = "";
    public boolean selected = false;

    public Symptom(JSONObject jsonObject) throws JSONException {
        diseaseID = jsonObject.getString("disease_id");
        name = jsonObject.getString("symptoms");
    }

    public String getName() {
        return name;
    }

    public String getDiseaseID() {
        return diseaseID;
    }
}
