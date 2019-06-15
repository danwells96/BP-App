package com.project.paulo.bpapp;

public class FeatureModel {
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
}
