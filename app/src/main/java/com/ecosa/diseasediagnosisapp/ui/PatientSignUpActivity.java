package com.ecosa.diseasediagnosisapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Patient;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.BitmapUtil;
import com.ecosa.diseasediagnosisapp.util.Util;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class PatientSignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.edt_fname_patient_reg)
    EditText fNameEditText;
    @BindView(R.id.edt_lname_patient_reg)
    EditText lastNameEditText;
    @BindView(R.id.edt_username_patient_reg)
    EditText usernameEditText;
    @BindView(R.id.edt_phone_number_patient_reg)
    EditText phoneNumberEditText;
    @BindView(R.id.edt_email_patient_reg)
    EditText emailEditText;
    @BindView(R.id.edt_location_patient_reg)
    EditText locationEditText;
    @BindView(R.id.edt_password_patient_reg)
    EditText passwordEditText;
    @BindView(R.id.edt_age_patient_reg)
    EditText ageEditText;
    @BindView(R.id.sex_spinner_patient_reg)
    Spinner mSpinner;
    @BindView(R.id.edt_height_patient_reg)
    EditText heightEditText;
    @BindView(R.id.edt_weight_patient_reg)
    EditText weightEditText;
    @BindView(R.id.edt_state_patient_reg)
    EditText stateEditText;
    @BindView(R.id.edt_country_patient_reg)
    EditText countryEditText;
    @BindView(R.id.iv_photo_patient_reg)
    CircleImageView photo;
    @BindView(R.id.tv_label_add_photo_patident_reg)
    TextView label;

    private ProgressDialog progressDialog;
    private String gender = "", photoPath = "";
    private File selectedPhotoFile;

    public static final int PERMISSION_CODE = 11, STORAGE_ACCESS_CODE = 13;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patient_reg);

        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Patient Registration");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_dropdown_item));
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_register_patient_reg) public void onRegisterClick() {

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
        if (Util.textOf(passwordEditText).length() < 3) {
            toast("Please enter password.");
            return;
        }
        if (Util.textOf(locationEditText).isEmpty()) {
            toast("Please enter location.");
            return;
        }
        if (Util.textOf(ageEditText).isEmpty()) {
            toast("Enter your age!");
            return;
        }
        if (gender.isEmpty()) {
            toast("Please select your gender.");
            return;
        }
        if (Util.textOf(heightEditText).isEmpty()) {
            toast("Enter height");
            return;
        }
        if (Util.textOf(weightEditText).isEmpty()) {
            toast("Enter weight");
            return;
        }
        if (Util.textOf(stateEditText).isEmpty()) {
            toast("Enter your state.");
            return;
        }
        if (Util.textOf(countryEditText).isEmpty()) {
            toast("Enter your country name.");
            return;
        }
        if (selectedPhotoFile == null) {
            toast("Please select a photo of you.");
            return;
        }

        Patient patient = new Patient(Util.textOf(usernameEditText),
                Util.textOf(fNameEditText),
                Util.textOf(lastNameEditText),
                Util.textOf(passwordEditText),
                Util.textOf(emailEditText),
                Util.textOf(phoneNumberEditText),
                Util.textOf(locationEditText));
        patient.setAge(Util.textOf(ageEditText));
        patient.setCountry(Util.textOf(countryEditText));
        patient.setGender(gender);
        patient.setHeight(Util.textOf(heightEditText));
        patient.setWeight(Util.textOf(weightEditText));
        patient.setState(Util.textOf(stateEditText));
        patient.setPhotoUri(selectedPhotoFile.getAbsolutePath());

        RepoManager.database().getPatientDao()
                .newPatient(patient);

        RepoManager.preferences()
                .edit().putString("username", Util.textOf(usernameEditText))
                .putString("photo_uri", selectedPhotoFile.getAbsolutePath())
                .apply();

        toast("Welcome to DiseaseDiagnosis System!");
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void toast(String text) {

        if (text == null || text.isEmpty()) return;

        Toast.makeText(this, text, Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        gender = "";
    }

    @OnClick(R.id.layout_pick_photo_patient_reg) public void onPickPhotoClick() {
        openGallery();
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

            selectedPhotoFile = new File(photoPath);
            label.setVisibility(View.GONE);
        }
    }
}
