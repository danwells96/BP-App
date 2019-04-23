package com.project.paulo.bpapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class Features extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {

    ArrayList<FeatureModel> featureModels;
    ListView listView;
    private static FeatureAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.features, container, false);
        listView = (ListView)rootView.findViewById(R.id.feature_listView);
        featureModels = new ArrayList<>();
        //Get actual data from database here
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19", "Systolic Pressure High", 150.4));

        adapter = new FeatureAdapter(featureModels, getContext());
        listView.setAdapter(adapter);
        View datePicker = rootView.findViewById(R.id.datePickerView);
        datePicker.setOnClickListener(datePickerListener);

        return rootView;
    }

    View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(Features.this, false);
            dateRangePickerFragment.show(getFragmentManager(), "datePicker");
            dateRangePickerFragment.setCancelable(false);
        }
    };

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        System.out.println(String.format("Start: %d/%d/%d End: %d/%d/%d", startDay, startMonth, startYear, endDay, endMonth, endYear));
    }
}
