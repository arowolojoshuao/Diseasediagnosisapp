package com.ecosa.diseasediagnosisapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ecosa.diseasediagnosisapp.App;
import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.async.Task;
import com.ecosa.diseasediagnosisapp.models.Disease;
import com.ecosa.diseasediagnosisapp.models.History;
import com.ecosa.diseasediagnosisapp.repo.RepoManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lekan Adigun on 1/15/2018.
 */

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.rv_history)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_no_history)
    TextView noHistory;

    private DiagnosisAdapter diagnosisAdapter;
    private List<Disease> diseaseList = new ArrayList<>();

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
        setContentView(R.layout.layout_history);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diagnosisAdapter = new DiagnosisAdapter(this, diseaseList);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("History");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView.setAdapter(diagnosisAdapter);
        App.getApp().getExecutorService()
                .execute(new FetchHistoryTask(callBack));
    }

    private Task.CallBack<List<Disease>> callBack = new Task.CallBack<List<Disease>>() {
        @Override
        public void done(List<Disease> data) {

            diseaseList.clear();
            for (Disease disease : data) {
                diseaseList.add(disease);
            }
            diagnosisAdapter = new DiagnosisAdapter(HistoryActivity.this, diseaseList);
            mRecyclerView.setAdapter(diagnosisAdapter);

            if (diseaseList.size() <= 0)
                noHistory.setVisibility(View.VISIBLE);
            else
                noHistory.setVisibility(View.GONE);
        }

        @Override
        public void error(Throwable throwable) {

        }
    };

    private class FetchHistoryTask extends Task<List<Disease>> {

        public FetchHistoryTask(CallBack callBack) {
            super(callBack);
        }

        @Override
        protected List<Disease> work() {
            List<History> all = RepoManager.database().getHistoryDao().histories();

            StringBuilder builder = new StringBuilder();
            for (History history : all) {
                builder.append(history.getDiseaseID())
                        .append(",");
            }

            return RepoManager.list(HistoryActivity.this, builder.toString().split(","));
        }
    }
}
