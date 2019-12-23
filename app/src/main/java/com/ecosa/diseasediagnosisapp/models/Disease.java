package com.ecosa.diseasediagnosisapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class Disease {

    private String name = "";
    private String id = "";
    private String description = "";
    private List<Symptom> symptoms = new ArrayList<>();
    private List<Cause> causes = new ArrayList<>();
    private String treatment = "";

    public Disease(JSONObject object) throws JSONException {
        name = object.getString("disease_name");
        id = object.getString("id");
        description = object.getString("disease_description");
        JSONArray syms = object.getJSONArray("symptoms");
        JSONArray causesArray = object.getJSONArray("causes");
        JSONArray treatmentArray = object.getJSONArray("treatment");

        for (int i = 0; i < syms.length(); i++) {
            symptoms.add(new Symptom(syms.getJSONObject(i)));
        }
        for (int i = 0; i < causesArray.length(); i++) {
            causes.add(new Cause(causesArray.getJSONObject(i)));
        }
        for (int i = 0; i < treatmentArray.length(); i++) {
            treatment = treatmentArray.getJSONObject(i).getString("disease_treatment");
        }
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }


    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public String getName() {
        return name;
    }

    public List<Cause> getCauses() {
        return causes;
    }

    @Override
    public String toString() {
        return name + " " +id + " Syms ==> " + symptoms.size() + " Causes ==> " + causes.size();
    }
}
