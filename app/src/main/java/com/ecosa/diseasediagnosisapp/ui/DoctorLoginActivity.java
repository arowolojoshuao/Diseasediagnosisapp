package com.ecosa.diseasediagnosisapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class DoctorLoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_username_doctor_login)
    EditText usernameEditText;
    @BindView(R.id.edt_password_doctor_login)
    EditText passwordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_doctor_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login_patient_login) public void onLoginClick() {

        if (!Patterns.EMAIL_ADDRESS.matcher(Util.textOf(usernameEditText)).matches()) {
            toast("Invalid email address.");
            return;
        }
        if (Util.textOf(passwordEditText).isEmpty()) {
            toast("Enter password.");
            return;
        }

        String username = Util.textOf(usernameEditText);
        Doctor doctor = RepoManager.database().getDoctorDao().doctor(username);
        if (doctor != null) {
            if (!doctor.getPassword().trim().equalsIgnoreCase(Util.textOf(passwordEditText))) {
                toast("Incorrect password. Please retry");

            }else {
                RepoManager.preferences()
                        .edit().putString("username", doctor.getEmail())
                        .apply();

                Intent intent = new Intent(this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
    public void toast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
