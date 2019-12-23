package com.ecosa.diseasediagnosisapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.models.Patient;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.L;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/15/2018.
 */

public class DashBoardActivity extends AppCompatActivity {

    private volatile boolean isDoctor = false;
    private Doctor mDoctor;
    private Patient mPatient;

    @BindView(R.id.tv_username_dashboard)
    TextView usernameTextView;
    @BindView(R.id.tv_phone_dashboard)
    TextView phoneNumberTextView;
    @BindView(R.id.iv_photo_dashboard)
    CircleImageView photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("DashBoard");
        }

        ButterKnife.bind(this);
        isDoctor = RepoManager.preferences().getBoolean("is_doctor", false);
        String uName = RepoManager.preferences().getString("username", "");

        if (isDoctor) {
            mDoctor = RepoManager.database().getDoctorDao().doctor(uName);
        }else {
            mPatient = RepoManager.database().getPatientDao().getPatient(uName);
        }
        if (isDoctor) {
            if (mDoctor != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mDoctor.getFirstName()));
                phoneNumberTextView.setText(mDoctor.getPhone());

//                L.fine("Photo " + mPatient.getPhotoUri());
                Glide.with(this)
                        .load(new File(mDoctor.getPhotoPath()))
                        .into(photo);
            }
        }else {
            if (mPatient != null) {
                usernameTextView.setText(String.valueOf("Welcome, " + mPatient.getFirstName()));
                phoneNumberTextView.setText(mPatient.getPhoneNumber());

//                L.fine("Photo " + mPatient.getPhotoUri());
                Glide.with(this)
                        .load(new File(mPatient.getPhotoUri()))
                        .into(photo);
            }
        }
    }
    @OnClick(R.id.btn_history_dashboard) public void history() {

        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_edit_profile_dashboard) public void editProfile() {
        if (isDoctor)
            startActivity(new Intent(this, EditDoctorAccountActivity.class));
        else
            startActivity(new Intent(this, EditPatientAccountActivity.class));
    }
    @OnClick(R.id.btn_logout_dashboard) public void logOut() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out?")
                .setMessage("Are you sure you are logging out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RepoManager.preferences().edit().clear().apply();
                        Intent intent = new Intent(DashBoardActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }
    @OnClick(R.id.btn_diagnos_dashboard) public void diagnosis() {

        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }
}
