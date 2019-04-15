package com.project.paulo.bpapp;

public class FeatureModel {
    String date;
    String feature;
    Double value;

    public FeatureModel(String date, String feature, Double value){
        this.date = date;
        this.feature = feature;
        this.value = value;
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
}
