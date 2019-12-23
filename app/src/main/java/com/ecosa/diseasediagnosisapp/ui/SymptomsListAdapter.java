package com.ecosa.diseasediagnosisapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecosa.diseasediagnosisapp.R;
import com.ecosa.diseasediagnosisapp.models.Symptom;
import com.ecosa.diseasediagnosisapp.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class SymptomsListAdapter extends RecyclerView.Adapter<SymptomsListAdapter.SymtomsListViewHolder> {

    private List<Symptom> symptoms = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SymptomsListAdapter(Context context, List<Symptom> symptoms) {
        this.symptoms = symptoms;
        mContext = context;
        if (mContext != null)
            mLayoutInflater = LayoutInflater.from(mContext);

    }
    @Override
    public SymtomsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        L.fine("Syms Adapter Created");
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        return new SymtomsListViewHolder(mLayoutInflater.inflate(R.layout.layout_symptoms, parent, false));
    }

    @Override
    public void onBindViewHolder(SymtomsListViewHolder holder, int position) {
        L.fine("Syms Adapter Binded");
        final int idx = position;
        final Symptom symptom = symptoms.get(position);
        holder.diseaseNameTextView.setText(symptom.getName());
        if (symptom.selected)
            holder.switchCompat.setChecked(true);
        else
            holder.switchCompat.setChecked(false);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                symptom.selected = !symptom.selected;
                notifyItemChanged(idx);
            }
        });
    }


    public List<Symptom> getSelected() {

        List<Symptom> symptoms = new ArrayList<>();
        for (Symptom symptom : this.symptoms) {

            if (symptom.selected)
                symptoms.add(symptom);
        }

        return symptoms;
    }

    @Override
    public int getItemCount() {
        L.fine("Syms ADapter gecount " + symptoms.size());
        return symptoms.size();
    }

    class SymtomsListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_disease_name_syms_layout)
        TextView diseaseNameTextView;
        @BindView(R.id.layout_syms_root)
        RelativeLayout root;
        @BindView(R.id.switch_select_syms_layout)
        SwitchCompat switchCompat;

        public SymtomsListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
