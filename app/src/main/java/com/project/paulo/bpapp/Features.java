package com.project.paulo.bpapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Features extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {

    ArrayList<FeatureModel> featureModels;
    ListView listView;
    private static FeatureAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Creating listview and populating it with flagged features
        View rootView = inflater.inflate(R.layout.features, container, false);
        listView = (ListView)rootView.findViewById(R.id.feature_listView);
        featureModels = new ArrayList<>();
        //Get actual data from database here once implemented
        featureModels.add(new FeatureModel("1/1/19 22:55", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 21:12", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 15:55", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 15:54", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 14:30", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 12:04", "Systolic Pressure High", 150.4));
        featureModels.add(new FeatureModel("1/1/19 11:17", "Systolic Pressure High", 150.4));

        //Sets adapters and data to date range pickers
        adapter = new FeatureAdapter(featureModels, getContext());
        listView.setAdapter(adapter);
        View datePicker = rootView.findViewById(R.id.datePickerView);
        datePicker.setOnClickListener(datePickerListener);


        //Populate with patient names from database here (once implemented) that doctor is allowed access to
        List<String> patientNames = new ArrayList<String>();
        patientNames.add("Patient A - 001");
        patientNames.add("Patient B - 002");
        patientNames.add("Patient C - 003");
        patientNames.add("Patient D - 004");
        patientNames.add("Patient E - 005");
        patientNames.add("Patient F - 006");
        patientNames.add("Patient G - 007");
        patientNames.add("Patient H - 008");
        //Sets adapter and data for spinner
        Spinner patientSpinner = rootView.findViewById(R.id.patientSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, patientNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSpinner.setAdapter(spinnerAdapter);
        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                //Gets newly selected patient
                String item = adapterView.getItemAtPosition(pos).toString();
                //Applies filter to listview and updates data displayed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    //When date filter is clicked, creates new instance of date range picker
    View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StringTuple st = parseDates();
            DateRangePickerFragment dateRangePickerFragment = DateRangePickerFragment.newInstance(Features.this, false, st);
            dateRangePickerFragment.show(getFragmentManager(), "datePicker");
            dateRangePickerFragment.setCancelable(false);
        }
    };

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        //Updates date range text views with chosen start/end dates
        System.out.println(String.format("Start: %d/%d/%d End: %d/%d/%d", startDay, startMonth, startYear, endDay, endMonth, endYear));
        TextView startDateTV = getActivity().findViewById(R.id.tv_startDate);
        TextView endDateTV = getActivity().findViewById(R.id.tv_endDate);
        startDateTV.setText(String.format("Start of Date Range\n%d/%d/%d", startDay, startMonth, startYear));
        endDateTV.setText(String.format("End of Date Range\n%d/%d/%d", endDay, endMonth, endYear));

        //Apply filter to dataset displayed in listview and update listview adapter with updated data to refresh view
    }


    public StringTuple parseDates(){
        View v = getView().findViewById(R.id.datePickerView);
        String s1 = ((TextView)v.findViewById(R.id.tv_startDate)).getText().toString();
        String startString = s1.substring(s1.indexOf("\n")+1).trim();
        String s2 = ((TextView)v.findViewById(R.id.tv_endDate)).getText().toString();
        String endString = s2.substring(s2.indexOf("\n")+1).trim();
        return new StringTuple(startString, endString);
    }

}

