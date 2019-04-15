package com.project.paulo.bpapp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeatureAdapter extends ArrayAdapter<FeatureModel> {
    private ArrayList<FeatureModel> dataset;
    Context ctxt;

    private static class ViewHolder{
        TextView tvDate;
        TextView tvFeature;
        TextView tvValue;
    }

    public FeatureAdapter(ArrayList data, Context context){
        super(context, R.layout.feature_row, data);
        this.dataset = data;
        this.ctxt = context;
    }

}
