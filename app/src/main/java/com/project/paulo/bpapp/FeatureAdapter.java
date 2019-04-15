package com.project.paulo.bpapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeatureModel featureModel = getItem(position);
        ViewHolder holder;

        final View result;

        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.feature_row, parent, false);
            holder.tvDate = (TextView)convertView.findViewById(R.id.textView_date);
            holder.tvFeature = (TextView)convertView.findViewById(R.id.textView_feature);
            holder.tvValue = (TextView)convertView.findViewById(R.id.textView_featureValue);

            result = convertView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }


        holder.tvDate.setText(featureModel.getDate());
        holder.tvFeature.setText(featureModel.getFeature());
        holder.tvValue.setText(featureModel.getValue().toString());

        return super.getView(position, convertView, parent);
    }
}
