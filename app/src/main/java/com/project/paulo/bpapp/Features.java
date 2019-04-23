package com.project.paulo.bpapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
            StringTuple st = parseDates();
            System.out.println(st);
            dateRangePickerFragment.show(getFragmentManager(), "datePicker");
            dateRangePickerFragment.setCancelable(false);
        }
    };

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        System.out.println(String.format("Start: %d/%d/%d End: %d/%d/%d", startDay, startMonth, startYear, endDay, endMonth, endYear));
        TextView startDateTV = getActivity().findViewById(R.id.tv_startDate);
        TextView endDateTV = getActivity().findViewById(R.id.tv_endDate);
        startDateTV.setText(String.format("Start of Date Range\n%d/%d/%d", startDay, startMonth, startYear));
        endDateTV.setText(String.format("End of Date Range\n%d/%d/%d", endDay, endMonth, endYear));
    }

    private class StringTuple{
        String startDate;
        String endDate;

        public StringTuple(String s1, String s2){
            startDate = s1;
            endDate = s2;
        }

        @Override
        public String toString() {
            return startDate+" "+endDate;
        }
    }

    public StringTuple parseDates(){
        View v = getView().findViewById(R.id.datePickerView);
        String s1 = ((TextView)v.findViewById(R.id.tv_startDate)).getText().toString();
        String s2 = ((TextView)v.findViewById(R.id.tv_endDate)).getText().toString();
        return new StringTuple(s1, s2);
    }

}
