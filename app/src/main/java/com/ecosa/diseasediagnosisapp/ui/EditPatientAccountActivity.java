package com.ecosa.diseasediagnosisapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Patient;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lekan Adigun on 1/13/2018.
 */

public class EditPatientAccountActivity extends AppCompatActivity {

    @BindView(R.id.edt_fname_edit_account)
    EditText fNameEditText;
    @BindView(R.id.edt_lname_edit_account)
    EditText lastNameEditText;
    @BindView(R.id.edt_username_edit_account)
    EditText usernameEditText;
    @BindView(R.id.edt_phone_number_edit_account)
    EditText phoneNumberEditText;
    @BindView(R.id.edt_email_edit_account)
    EditText emailEditText;
    @BindView(R.id.edt_location_edit_account)
    EditText locationEditText;

    private Patient mPatient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_account);
        ButterKnife.bind(this);

        mPatient =
                RepoManager.database().getPatientDao().getPatient(RepoManager.preferences().getString("username", ""));
        if (mPatient != null) {
            render();
        }
    }
    private void render() {
        fNameEditText.setText(mPatient.getFirstName());
        lastNameEditText.setText(mPatient.getLastName());
        usernameEditText.setText(mPatient.getUsername());
        phoneNumberEditText.setText(mPatient.getPhoneNumber());
        emailEditText.setText(mPatient.getEmail());
        locationEditText.setText(mPatient.getLocation());
    }

    @OnClick(R.id.btn_save_changes_patient) public void onSaveCHangesClick() {
        if (Util.textOf(usernameEditText).isEmpty()) {
            toast("Please enter your username.");
            return;
        }
        if (Util.textOf(fNameEditText).isEmpty()) {
            toast("Please enter your first Name");
            return;
        }
        if (Util.textOf(lastNameEditText).isEmpty()) {
            toast("Please enter your last name");
            return;
        }
        if (Util.textOf(phoneNumberEditText).length() < 10) {
            toast("Invalid mobile number.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Util.textOf(emailEditText)).matches()) {
            toast("Invalid email address.");
            return;
        }
        if (Util.textOf(locationEditText).isEmpty()) {
            toast("Please enter location.");
            return;
        }

        mPatient.setFirstName(Util.textOf(fNameEditText));
        mPatient.setLastName(Util.textOf(lastNameEditText));
        mPatient.setUsername(Util.textOf(usernameEditText));
        mPatient.setPhoneNumber(Util.textOf(phoneNumberEditText));
        mPatient.setEmail(Util.textOf(emailEditText));
        mPatient.setLocation(Util.textOf(locationEditText));

        RepoManager.database().getPatientDao()
                .update(mPatient);

        RepoManager.preferences()
                .edit().putString("username", Util.textOf(usernameEditText))
                .apply();

        toast("Account Updated!");
        finish();
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
