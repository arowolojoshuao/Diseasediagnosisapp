package com.ecosa.diseasediagnosisapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.App;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.async.Task;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.models.Patient;
import com.ecosa.diseasediagnosisapp.models.Symptom;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class ActivityMain extends AppCompatActivity {

    private List<Symptom> symptoms = new ArrayList<>();
    private SymptomsListAdapter symptomsListAdapter;
    private Patient mPatient;
    private Doctor mDoctor;
    private Boolean isDoctor = false;

    @BindView(R.id.rv_symptoms)
    RecyclerView recyclerView;
    @BindView(R.id.tv_username_main)
    TextView usernameTextView;
    @BindView(R.id.tv_phone_main)
    TextView phoneNumberTextView;
    @BindView(R.id.iv_photo_activity_main)
    CircleImageView photo;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_disease);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        App.getApp().getExecutorService()
                .execute(new AllSymtomsTask(callBack));

        List<Patient> all = RepoManager.database().getPatientDao().all();
        L.fine("All Patient ==> " + all.size());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        symptomsListAdapter = new SymptomsListAdapter(this, symptoms);
        recyclerView.setAdapter(symptomsListAdapter);

        isDoctor = RepoManager.preferences().getBoolean("is_doctor", false);
        String uName = RepoManager.preferences().getString("username", "");
        if (isDoctor) {
            mDoctor = RepoManager.database().getDoctorDao().doctor(uName);
        } else {
            mPatient = RepoManager.database().getPatientDao().getPatient(uName);
        }
        if (isDoctor) {
            if (mDoctor != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mDoctor.getFirstName()));
                phoneNumberTextView.setText(mDoctor.getPhone());

                Glide.with(this)
                        .load(mDoctor.getPhotoPath())
                        .into(photo);
            }
        } else {
            if (mPatient != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mPatient.getFirstName()));
                phoneNumberTextView.setText(mPatient.getPhoneNumber());

                L.fine("Photo " + mPatient.getPhotoUri());
                Glide.with(this)
                        .load(new File(mPatient.getPhotoUri()))
                        .into(photo);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isDoctor = RepoManager.preferences().getBoolean("is_doctor", false);
        String uName = RepoManager.preferences().getString("username", "");
        if (isDoctor) {
            mDoctor = RepoManager.database().getDoctorDao().doctor(uName);

        } else {
            mPatient = RepoManager.database().getPatientDao().getPatient(uName);
        }

        if (isDoctor) {
            if (mDoctor != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mDoctor.getFirstName()));
                phoneNumberTextView.setText(mDoctor.getPhone());

                Glide.with(this)
                        .load(mDoctor.getPhotoPath())
                        .into(photo);
            }
        } else {
            if (mPatient != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mPatient.getFirstName()));
                phoneNumberTextView.setText(mPatient.getPhoneNumber());
            }
        }
    }

    @OnClick(R.id.btn_sign_out_main)
    public void onSignOutClick() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out?")
                .setMessage("Are you sure you are logging out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RepoManager.preferences().edit().clear().apply();
                        Intent intent = new Intent(ActivityMain.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }

    @OnClick(R.id.btn_edit_profile_main)
    public void editProfile() {

        if (isDoctor)
            startActivity(new Intent(this, EditDoctorAccountActivity.class));
        else
            startActivity(new Intent(this, EditPatientAccountActivity.class));
    }

    @OnClick(R.id.btn_diagnos)
    public void onDiagnoseClick() {

        if (symptomsListAdapter == null) {
            return;
        }

        StringBuilder ids = new StringBuilder();
        StringBuilder syms = new StringBuilder();
        List<Symptom> selected = symptomsListAdapter.getSelected();
        if (selected.size() > 0) {

            for (Symptom symptom : selected) {
                ids.append(symptom.getDiseaseID())
                        .append(",");
                syms.append(symptom.getName())
                        .append(", ");
            }

            L.fine("Ids ==> " + ids.toString());
            Intent intent = new Intent(this, DiagnosisActivity.class);
            intent.putExtra("ids", ids.toString());
            intent.putExtra("syms", syms.toString());
            startActivity(intent);
        } else {
            toast("Please select one or more symptoms.");
        }
    }

    private void toast(String t) {
        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
    }

    private class AllSymtomsTask extends Task<List<Symptom>> {

        public AllSymtomsTask(CallBack callBack) {
            super(callBack);
        }

        @Override
        protected List<Symptom> work() {
            return RepoManager.symptoms(ActivityMain.this);
        }
    }

    private Task.CallBack<List<Symptom>> callBack = new Task.CallBack<List<Symptom>>() {
        @Override
        public void done(List<Symptom> data) {
            symptoms = data;

            symptomsListAdapter = new SymptomsListAdapter(ActivityMain.this, symptoms);
            recyclerView.setAdapter(symptomsListAdapter);
        }

        @Override
        public void error(Throwable throwable) {
            L.wtf(throwable);
        }
    };
}
