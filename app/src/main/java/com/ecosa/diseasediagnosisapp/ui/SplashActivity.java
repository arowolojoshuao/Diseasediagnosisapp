package com.ecosa.diseasediagnosisapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iboju)
    RelativeLayout iboju;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash);

        ButterKnife.bind(this);

        String user = RepoManager.preferences()
                .getString("username", "");
        if (user.isEmpty()) {
            iboju.setVisibility(View.GONE);
        }else {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 1000);
        }
    }

    @OnClick(R.id.btn_patient_reg_splash) public void patientReg() {
        Intent intent = new Intent(this, PatientSignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_doctor_login) public void doctorLogin() {

        Intent intent = new Intent(this, DoctorLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_doctor_reg_splash) public void doctorReg() {

        Intent intent = new Intent(this, DoctorRegistrationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_patient_login) public void patientLogin() {
        Intent intent = new Intent(this, PatientLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iboju) public void iboju() {}

}
