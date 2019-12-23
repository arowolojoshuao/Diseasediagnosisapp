package com.ecosa.diseasediagnosisapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Cause;
import com.ecosa.diseasediagnosisapp.models.Disease;
import com.ecosa.diseasediagnosisapp.models.Symptom;
import com.ecosa.diseasediagnosisapp.util.L;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lekan Adigun on 1/11/2018.
 */

public class DiseaseListAdapter extends RecyclerView.Adapter<DiseaseListAdapter.DiseaseListViewHolder> {

    private List<Disease> diseaseList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public DiseaseListAdapter(Context context, List<Disease> diseases) {
        this.diseaseList = diseases;
        mContext = context;
        if (mContext != null)
            mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public DiseaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(parent.getContext());

        return new DiseaseListViewHolder(mLayoutInflater.inflate(R.layout.layout_disease, parent, false));
    }

    @Override
    public void onBindViewHolder(DiseaseListViewHolder holder, int position) {

        Disease disease = diseaseList.get(position);
        String syms = buildSyms(disease);
        String causes = buildCauses(disease);

        holder.diseaseName.setText(disease.getName());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }

    private String buildCauses(Disease disease) {

        StringBuilder builder = new StringBuilder();
        List<Cause> causes = disease.getCauses();
        for (Cause c : causes) {
            builder.append("-").append(" ").append(c.getName()).append("\n");
        }
        return builder.toString();
    }

    private String buildSyms(Disease disease) {

        StringBuilder builder = new StringBuilder();
        List<Symptom> symptoms = disease.getSymptoms();
        for (Symptom s : symptoms) {
            builder.append("-").append(" ").append(s.getName()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {

        L.fine("GetItemCountCalled");
        return diseaseList.size();
    }

    class DiseaseListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_disease_name_disease_layout)
        TextView diseaseName;
        @BindView(R.id.tv_causes_disease_layout)
        TextView causesTextView;
        @BindView(R.id.tv_syms_disease_layout)
        TextView symsTextView;
        @BindView(R.id.layout_disease_root)
        RelativeLayout root;

        public DiseaseListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
