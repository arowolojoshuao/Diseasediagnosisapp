package com.ecosa.diseasediagnosisapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/13/2018.
 */

public class EditDoctorAccountActivity extends AppCompatActivity {

    @BindView(R.id.edt_email_doctor_edit_account)
    EditText emailEditText;
    @BindView(R.id.edt_fname_doctor_edit_account)
    EditText firstNameEditText;
    @BindView(R.id.edt_lname_doctor_edit_account)
    EditText lastNameEditText;
    @BindView(R.id.edt_edtphone_doctor_edit_account)
    EditText phoneEditText;
    @BindView(R.id.iv_photo_doctor_edit_account)
    CircleImageView photo;

    private Doctor mDoctor;
    private String photoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_account_doctor);
        ButterKnife.bind(this);

        mDoctor =
                RepoManager.database().getDoctorDao().doctor(RepoManager.preferences().getString("username", ""));

        if (mDoctor != null) {
            render();
        }
    }

    private void render() {

        emailEditText.setText(mDoctor.getEmail());
        firstNameEditText.setText(mDoctor.getFirstName());
        lastNameEditText.setText(mDoctor.getLastName());
        phoneEditText.setText(mDoctor.getPhone());
        photoPath = mDoctor.getPhotoPath();

        Glide.with(this)
                .load(mDoctor.getPhotoPath())
                .into(photo);
    }
    @OnClick(R.id.btn_save_changes_doctor) public void saveChanges() {
        if (!Patterns.EMAIL_ADDRESS.matcher(Util.textOf(emailEditText)).matches()) {
            toast("Invalid email address");
            return;
        }
        if (Util.textOf(firstNameEditText).isEmpty()) {
            toast("Enter first name.");
            return;
        }
        if (Util.textOf(lastNameEditText).isEmpty()) {
            toast("Enter last Name.");
            return;
        }
        if (Util.textOf(phoneEditText).length() < 10) {
            toast("Invalid phone number");
            return;
        }

        if (photoPath == null || photoPath.isEmpty()) {
            toast("Please set photo");
            return;
        }

        mDoctor.setEmail(Util.textOf(emailEditText));
        mDoctor.setFirstName(Util.textOf(firstNameEditText));
        mDoctor.setLastName(Util.textOf(lastNameEditText));
        mDoctor.setPhone(Util.textOf(phoneEditText));
        mDoctor.setPhotoPath(photoPath);

        RepoManager.preferences().edit().putString("username", Util.textOf(emailEditText)).apply();
        RepoManager.database().getDoctorDao().update(mDoctor);

        toast("Account Updated!");
    }

    private void toast(String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
