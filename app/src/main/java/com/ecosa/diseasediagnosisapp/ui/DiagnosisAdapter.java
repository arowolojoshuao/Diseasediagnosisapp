package com.ecosa.diseasediagnosisapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Cause;
import com.ecosa.diseasediagnosisapp.models.Disease;
import com.ecosa.diseasediagnosisapp.models.Symptom;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {

    private List<Disease> diseases;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public DiagnosisAdapter(Context context, List<Disease> diseases) {
        this.diseases = diseases;
        mContext = context;
        if (mContext != null)
            mLayoutInflater = LayoutInflater.from(mContext);

    }
    @Override
    public DiagnosisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new DiagnosisViewHolder(mLayoutInflater.inflate(R.layout.layout_disease, parent, false));
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
    public void onBindViewHolder(DiagnosisViewHolder holder, int position) {

        Disease disease = diseases.get(position);
        holder.diseaseNameTextView.setText(disease.getName());
        String causes = buildCauses(disease);
        String syms = buildSyms(disease);
        holder.causesTextView.setText(causes);
        holder.symsTextView.setText(syms);
    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }

    class DiagnosisViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_disease_name_disease_layout)
        TextView diseaseNameTextView;
        @BindView(R.id.tv_causes_disease_layout)
        TextView causesTextView;
        @BindView(R.id.tv_syms_disease_layout)
        TextView symsTextView;
        @BindView(R.id.layout_disease_root)
        CardView root;

        public DiagnosisViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
