package com.ecosa.diseasediagnosisapp.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ecosa.diseasediagnosisapp.App;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.async.Task;
import com.ecosa.diseasediagnosisapp.models.Disease;
import com.ecosa.diseasediagnosisapp.models.Doctor;
import com.ecosa.diseasediagnosisapp.models.History;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;
import com.ecosa.diseasediagnosisapp.util.L;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lekan Adigun on 1/11/2018.
 */

public class DiagnosisActivity extends AppCompatActivity {

    @BindView(R.id.rv_possible_causes)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_doctor_phone_disease_diag)
    TextView doctorPhoneTextView;
    @BindView(R.id.tv_doctor_name_diag)
    TextView doctorNameTextView;
    @BindView(R.id.tv_doctor_practise_disease_diag)
    TextView expertise;
    @BindView(R.id.iv_doctor_disease_diag)
    CircleImageView circleImageView;
    @BindView(R.id.tv_syms_label_diag_activity)
    TextView symsLabelTextView;
    @BindView(R.id.tv_treatment_diagnosis_activity)
    TextView treatmentTextView;

    private DiagnosisAdapter diseaseListAdapter;
    private List<Disease> diseaseList = new ArrayList<>();
    private Doctor mDoctor;
    private String syms = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_diagnosis_activity);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Diagnosis");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String ids = intent.getStringExtra("ids");
        syms = intent.getStringExtra("syms");
        String[] splitted = ids.split(",");

        L.fine("Data ==> " + ids);
        App.getApp()
                .getExecutorService().execute(new FindDiseaseTask(callBack, splitted));

        List<Doctor> all = RepoManager.database().getDoctorDao().all();
        if (all.size() > 0) {
            int rand = new Random().nextInt(all.size());
            if (rand == 0) rand = 1;

            mDoctor = RepoManager.database().getDoctorDao().doctor(rand);
        }
        render();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        diseaseListAdapter = new DiagnosisAdapter(this, diseaseList);
        mRecyclerView.setAdapter(diseaseListAdapter);

    }

    private void render() {

        symsLabelTextView.setText("For Symptoms " + syms);
        if (mDoctor != null) {
            doctorNameTextView.setTypeface(null, Typeface.BOLD);
            doctorNameTextView.setText("Dr. " + mDoctor.getFirstName() + " " + mDoctor.getLastName());
            doctorPhoneTextView.setText(mDoctor.getPhone());
            expertise.setText(mDoctor.getExpertise());

            Glide.with(this)
                    .load(new File(mDoctor.getPhotoPath()))
                    .into(circleImageView);
        }
    }

    @OnClick(R.id.layout_call_doctor) public void onCallDoctor() {

        if (mDoctor == null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:07035452307"));
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        }else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mDoctor.getPhone()));
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        }
    }

    @OnClick(R.id.layout_message_doctor) public void onMessageDoctor() {

        if (mDoctor == null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:07035452307"));
            intent.putExtra("sms_body", "Hi, Dr Mark");
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        }else {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mDoctor.getPhone()));
            intent.putExtra("sms_body", "Hi, Dr " + mDoctor.getLastName());
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
        }
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

    private Task.CallBack<List<Disease>> callBack = new Task.CallBack<List<Disease>>() {
        @Override
        public void done(List<Disease> data) {
            diseaseList.clear();
            for (Disease disease : data) {
                diseaseList.add(disease);
            }
            diseaseListAdapter = new DiagnosisAdapter(DiagnosisActivity.this, diseaseList);
            mRecyclerView.setAdapter(diseaseListAdapter);

            if (diseaseList.size() > 0) {
                Disease disease = diseaseList.get(0);
                treatmentTextView.setText(disease.getTreatment());
            }

            App.getApp().getExecutorService()
                    .execute(new HistoryBackup(diseaseList));
        }

        @Override
        public void error(Throwable throwable) {
            L.wtf(throwable);
        }
    };

    private class HistoryBackup implements Runnable {

        private List<Disease> diseaseList;

        public HistoryBackup(List<Disease> diseases) {
            diseaseList = diseases;
        }
        @Override
        public void run() {

            for (Disease disease : diseaseList) {

                if (disease != null) {
                    History history = RepoManager.database().getHistoryDao().history(disease.getId());
                    if (history == null) {
                        L.fine("History never exists ==> " + disease.getId());
                        history = new History(disease.getId());
                        RepoManager.database()
                                .getHistoryDao().history(history);
                    }
                }

            }
        }
    }

    private class FindDiseaseTask extends Task<List<Disease>> {

        String[] ids;
        private FindDiseaseTask(CallBack callBack, String[] ids) {
            super(callBack);
            this.ids = ids;
        }

        @Override
        protected List<Disease> work() {
            return RepoManager.list(DiagnosisActivity.this, ids);
        }
    }
}
