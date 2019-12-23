package com.ecosa.diseasediagnosisapp.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.BitmapUtil;
import com.ecosa.diseasediagnosisapp.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/11/2018.
 */

public class DoctorRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.edt_email_doctor_reg)
    EditText emailEditText;
    @BindView(R.id.edt_fname_doctor_reg)
    EditText firstNameEditText;
    @BindView(R.id.edt_lname_doctor_reg)
    EditText lastNameEditText;
    @BindView(R.id.edt_edtphone_doctor_reg)
    EditText phoneEditText;
    @BindView(R.id.spinner_doctor_expertise)
    Spinner spinner;
    @BindView(R.id.iv_photo_doctor_reg)
    CircleImageView photo;
    @BindView(R.id.edt_password_doctor_reg)
    EditText paswordEditText;

    private String photoPath = "";
    private String expertise = "";

    public static final int STORAGE_ACCESS_CODE = 11, PERMISSION_CODE = 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_doctor_reg);

        ButterKnife.bind(this);

        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.expertise, android.R.layout.simple_spinner_dropdown_item));
        spinner.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.btn_register_doctor) public void registerClick() {

        if (!Patterns.EMAIL_ADDRESS.matcher(Util.textOf(emailEditText)).matches()) {
            toast("Invalid email address");
            return;
        }
        if (Util.textOf(paswordEditText).length() < 3) {
            toast("Invalid password. Too short");
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

        if (expertise == null || expertise.isEmpty()) {
            toast("Please select area of specialization");
            return;
        }

        Doctor doctor =
                new Doctor(Util.textOf(emailEditText),
                        Util.textOf(firstNameEditText),
                        Util.textOf(lastNameEditText),
                        Util.textOf(phoneEditText),
                        photoPath);
        doctor.setPassword(Util.textOf(paswordEditText));

        RepoManager.preferences().edit().putString("username", Util.textOf(emailEditText)).apply();
        RepoManager.preferences().edit().putBoolean("is_doctor", true).apply();
        doctor.setExpertise(spinner.getSelectedItem().toString());
        RepoManager.database().getDoctorDao().doctor(doctor);
        toast("Welcome!");
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.layout_pick_photo_doctor_reg) public void onPickPhoto() {
        requestPermission();
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, STORAGE_ACCESS_CODE);

    }

    private void requestPermission() {

        int status = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(status == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }else {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }else {
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }else {
            showDialog("Permission Denied", "Failed to access external Storage, permission denied.");
        }

    }

    private void showDialog(String title, String messgae) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(messgae)
                .setPositiveButton("OK", null)
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == STORAGE_ACCESS_CODE && resultCode == RESULT_OK && data != null) {
            photoPath = BitmapUtil.getPath(this, data.getData());
            Glide.with(this)
                    .load(new File(photoPath))
                    .into(photo);

        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        expertise = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        expertise = "";
    }
}
