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

public class Features extends Fragment {

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

        adapter = new FeatureAdapter(featureModels, getContext());
        listView.setAdapter(adapter);

        return rootView;
    }
}
