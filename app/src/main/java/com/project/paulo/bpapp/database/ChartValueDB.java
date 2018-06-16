package com.project.paulo.bpapp.database;


public class ChartValueDB {

    //private variables
    private int _id;
    private String _time;
    private String _pressure;

    // Empty constructor
    public ChartValueDB(){

    }
    // constructor
    public ChartValueDB(int id, String time, String _pressure){
        this._id = id;
        this._time = time;
        this._pressure = _pressure;
    }

    // constructor
    public ChartValueDB(String time, String _pressure){
        this._time = time;
        this._pressure = _pressure;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getTime(){
        return this._time;
    }

    // setting name
    public void setTime(String time){
        this._time = time;
    }

    // getting phone number
    public String getPressure(){
        return this._pressure;
    }

    // setting phone number
    public void setPressure(String pressure){
        this._pressure = pressure;
    }

    @Override
    public String toString() {
        return "ChartValueDB{" +
                "_id=" + _id +
                ", _time='" + _time + '\'' +
                ", _pressure='" + _pressure + '\'' +
                '}';
    }
}
