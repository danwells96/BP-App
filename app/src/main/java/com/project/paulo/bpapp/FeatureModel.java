package com.project.paulo.bpapp;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Class to handle abnormal feature values in feature fragment or receiving database data
public class FeatureModel implements Comparable<FeatureModel>{
    String date;
    String feature;
    Double value;
    String patientId;

    public FeatureModel(String date, String feature, Double value, String patientId){
        this.date = date;
        this.feature = feature;
        this.value = value;
        this.patientId = patientId;
    }

    public String getDate(){
        return date;
    }

    public String getFeature() {
        return feature;
    }

    public Double getValue() {
        return value;
    }

    public String getPatientId() {
        return patientId;
    }

    //Function to order featuremodels by date in descending order
    @Override
    public int compareTo(@NonNull FeatureModel featureModel) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date objectDate = format.parse(featureModel.date);
            Date thisDate = format.parse(this.date);
            return -thisDate.compareTo(objectDate);
        }catch (ParseException e){
            System.out.println(e.getStackTrace());
            return 0;
        }
    }
}
