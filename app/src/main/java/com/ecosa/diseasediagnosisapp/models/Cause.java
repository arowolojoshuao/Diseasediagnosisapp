package com.ecosa.diseasediagnosisapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class Cause {

    private String diseaseID = "";
    private String name = "";

    public Cause(JSONObject jsonObject) throws JSONException {
        diseaseID = jsonObject.getString("disease_id");
        name = jsonObject.getString("causes");
    }

    public String getDiseaseID() {
        return diseaseID;
    }

    public String getName() {
        return name;
    }
}
