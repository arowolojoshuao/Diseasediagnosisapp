package com.ecosa.diseasediagnosisapp.repo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ecosa.diseasediagnosisapp.App;
import com.ecosa.diseasediagnosisapp.models.Cause;
import com.ecosa.diseasediagnosisapp.models.Disease;
import com.ecosa.diseasediagnosisapp.models.Symptom;
import com.ecosa.diseasediagnosisapp.repo.room.AppDatabase;
import com.ecosa.diseasediagnosisapp.util.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class RepoManager {

    private static AppDatabase appDatabase;
    public static final String DATABASE_NAME = "disease_diag";
    public static final String PREF = "pref";
    private static SharedPreferences mSharedPreferences;

    public static synchronized AppDatabase database() {
        if (appDatabase == null) {
            appDatabase =
                    Room.databaseBuilder(App.getApp().getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return appDatabase;
    }

    public static synchronized SharedPreferences preferences() {

        if (mSharedPreferences == null) {
            mSharedPreferences
                    = PreferenceManager.getDefaultSharedPreferences(App.getApp().getApplicationContext());
        }
        return mSharedPreferences;
    }

    public static List<Disease> diseases(Context context) {

        List<Disease> diseases = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder data = new StringBuilder();

            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            String json = data.toString();
            JSONArray array = new JSONArray(json);
            L.fine(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                diseases.add(new Disease(object));
            }
        }catch (Exception e) {
            L.wtf(e);
        }

        return diseases;
    }

    public static List<Symptom> symptoms(Context context) {

        List<Disease> all = diseases(context);
        List<Symptom> symptoms = new ArrayList<>();

        for (Disease disease : all) {
            symptoms.addAll(disease.getSymptoms());
        }

        return symptoms;
    }
    public static List<Cause> causes(Context context, String id) {

        List<Disease> all = diseases(context);
        List<Cause> causes = new ArrayList<>();

        for (Disease disease : all) {
            if (disease.getId().trim().equalsIgnoreCase(id.trim())) {
                causes = disease.getCauses();
                break;
            }
        }
        return causes;
    }
    public static List<Disease> list(Context context, String...ids) {

        List<Disease> diseases = diseases(context);
        List<Disease> result = new ArrayList<>();
        List<String> idList = filterDups(ids);

        for (Disease disease : diseases) {
            for (String id : idList) {
                if (disease.getId().trim().equalsIgnoreCase(id.trim())) {
                    result.add(disease);
                }
            }
        }
        L.fine("Total Disease Found ==> " + result.size());
        return result;
    }
    private static List<String> filterDups(String...ids) {

        List<String> result = new ArrayList<>();
        L.fine("Size Before --> " + ids.length);
        for (String id : ids) {
            if (!result.contains(id))
                result.add(id);
        }
        L.fine("Size After ==> " + result.size());

        return result;
    }
}
