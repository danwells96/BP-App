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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Features extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener {

    ArrayList<FeatureModel> featureModels;
    ArrayList<FeatureModel> dataList;
    ListView listView;
    private FeatureAdapter adapter;
    private boolean init = true;
    TextView noPatientTV;
    TextView emptyListTV;
    String patientFilter = "";
    Date start = null;
    Date end = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Sets title of activity for continuity
        getActivity().setTitle("Features");
        //Creating listview and populating it with flagged features
        View rootView = inflater.inflate(R.layout.features, container, false);
        listView = (ListView)rootView.findViewById(R.id.feature_listView);
        noPatientTV = rootView.findViewById(R.id.tv_noPatient);
        emptyListTV = rootView.findViewById(R.id.tv_emptyList);
        featureModels = new ArrayList<>();
        //Get actual data from database here once implemented but placeholder data for now
        featureModels.add(new FeatureModel("2019-01-25 10:03:12", "Systolic Pressure High", 150.4, "001"));
        featureModels.add(new FeatureModel("2019-01-20 10:03:12", "Systolic Pressure High", 150.4, "002"));
        featureModels.add(new FeatureModel("2019-01-15 10:03:12", "Systolic Pressure High", 150.4, "002"));
        featureModels.add(new FeatureModel("2019-01-25 10:03:12", "Systolic Pressure High", 150.4, "003"));
        featureModels.add(new FeatureModel("2019-01-25 10:01:12", "Systolic Pressure High", 150.4, "002"));
        featureModels.add(new FeatureModel("2019-01-18 10:03:12", "Systolic Pressure High", 150.4, "007"));
        featureModels.add(new FeatureModel("2018-01-01 10:03:12", "Systolic Pressure High", 150.4, "005"));
        featureModels.add(new FeatureModel("2018-01-12 10:03:12", "Systolic Pressure High", 150.4, "005"));
        featureModels.add(new FeatureModel("2018-01-25 10:03:12", "Systolic Pressure High", 150.4, "005"));
        featureModels.add(new FeatureModel("2018-01-24 10:03:12", "Systolic Pressure High", 150.4, "004"));
        featureModels.add(new FeatureModel("2018-01-23 10:03:12", "Systolic Pressure High", 150.4, "007"));
        featureModels.add(new FeatureModel("2018-01-25 10:03:12", "Systolic Pressure High", 150.4, "006"));
        featureModels.add(new FeatureModel("2018-01-21 10:03:12", "Systolic Pressure High", 150.4, "001"));
        featureModels.add(new FeatureModel("2018-01-25 10:03:12", "Systolic Pressure High", 150.4, "002"));

        dataList = new ArrayList<>();
        //Sets adapters and data for listview
        adapter = new FeatureAdapter(dataList, getContext());
        listView.setAdapter(adapter);
        //Sets on click listener for date range picker
        View datePicker = rootView.findViewById(R.id.datePickerView);
        datePicker.setOnClickListener(datePickerListener);


        //Populate with patient names from database here (once implemented) that doctor is allowed access to
        List<String> patientNames = new ArrayList<String>();
        patientNames.add("No patient selected");
        patientNames.add("Patient A - 001");
        patientNames.add("Patient B - 002");
        patientNames.add("Patient C - 003");
        patientNames.add("Patient D - 004");
        patientNames.add("Patient E - 005");
        patientNames.add("Patient F - 006");
        patientNames.add("Patient G - 007");
        patientNames.add("Patient H - 008");
        //Sets adapter and data for patient spinner
        Spinner patientSpinner = rootView.findViewById(R.id.patientSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, patientNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSpinner.setAdapter(spinnerAdapter);
        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                //Prevents view being updated on instantiation
                if(init){
                    init = false;
                    listView.setVisibility(View.INVISIBLE);
                    noPatientTV.setVisibility(View.VISIBLE);
                }else {
                    //Gets newly selected patient
                    String item = adapterView.getItemAtPosition(pos).toString();
                    //Applies filter to listview and updates data displayed
                    dataList.clear();
                    //Filters data based on selected spinner item
                    if(!item.equals("No patient selected")) {
                        String pId = item.substring(item.lastIndexOf(" ") + 1);
                        patientFilter = pId;
                        for (int i = 0; i < featureModels.size(); i++) {
                            if (start != null && end != null) {
                                try {
                                    String featureDateString = featureModels.get(i).getDate().substring(0, 10);
                                    Date featureDate = format.parse(featureDateString);
                                    if (featureModels.get(i).patientId.equals(pId) && featureDate.before(end) && featureDate.after(start)) {
                                        dataList.add(featureModels.get(i));
                                    }
                                }catch (ParseException e){
                                    System.out.println(e.getStackTrace());
                                }
                            }else{
                                if (featureModels.get(i).patientId.equals(pId)) {
                                    dataList.add(featureModels.get(i));
                                }
                            }
                        }
                        //If data to display
                        if(dataList.size()>0){
                            Collections.sort(dataList);
                            listView.setVisibility(View.VISIBLE);
                            emptyListTV.setVisibility(View.INVISIBLE);
                            noPatientTV.setVisibility(View.INVISIBLE);
                        }else{
                            listView.setVisibility(View.INVISIBLE);
                            emptyListTV.setVisibility(View.VISIBLE);
                            noPatientTV.setVisibility(View.GONE);
                        }
                    }else{
                        //No patient is selected
                        noPatientTV.setVisibility(View.VISIBLE);
                        emptyListTV.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        patientFilter = "";
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Should do nothing if nothing selected
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
        System.out.println(String.format("Start: %d-%d-%d End: %d-%d-%d", startYear, startMonth, startDay, endYear, endMonth, endDay));
        TextView startDateTV = getActivity().findViewById(R.id.tv_startDate);
        TextView endDateTV = getActivity().findViewById(R.id.tv_endDate);
        String startDateString = String.format("%d-%d-%d",startYear, startMonth, startDay);
        String endDateString = String.format("%d-%d-%d", endYear, endMonth, endDay);
        startDateTV.setText(String.format("Start of Date Range\n%d-%d-%d", startDay, startMonth, startYear));
        endDateTV.setText(String.format("End of Date Range\n%d-%d-%d", endDay, endMonth, endYear));

        try {

            //Gets previously selected date range
            Date startDate = format.parse(startDateString);
            Date endDate = format.parse(endDateString);
            start = startDate;
            end = endDate;
            dataList.clear();
            //Apply filter to dataset displayed in listview and update listview adapter with filtered data to refresh view
            if(noPatientTV.getVisibility()!=View.VISIBLE) {

                for (int i = 0; i < featureModels.size(); i++) {
                    String featureDateString = featureModels.get(i).getDate().substring(0, 10);
                    String featurePId = featureModels.get(i).getPatientId();
                    Date featureDate = format.parse(featureDateString);
                    if (featureDate.before(endDate) && featureDate.after(startDate) && featurePId.equals(patientFilter)) {
                        dataList.add(featureModels.get(i));
                    }
                }
                if (dataList.size() > 0) {
                    Collections.sort(dataList);
                    listView.setVisibility(View.VISIBLE);
                    emptyListTV.setVisibility(View.INVISIBLE);
                    noPatientTV.setVisibility(View.INVISIBLE);
                } else {
                    listView.setVisibility(View.INVISIBLE);
                    emptyListTV.setVisibility(View.VISIBLE);
                    noPatientTV.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (ParseException e){
            System.out.println("Dates failed to parse");
        }
    }


    //Parses previously selected dates to set initial values of date range picker
    public StringTuple parseDates(){
        View v = getView().findViewById(R.id.datePickerView);
        String s1 = ((TextView)v.findViewById(R.id.tv_startDate)).getText().toString();
        String startString = s1.substring(s1.indexOf("\n")+1).trim();
        String s2 = ((TextView)v.findViewById(R.id.tv_endDate)).getText().toString();
        String endString = s2.substring(s2.indexOf("\n")+1).trim();
        return new StringTuple(startString, endString);
    }

}

