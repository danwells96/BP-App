package com.project.paulo.bpapp;


public class StringTuple{
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
