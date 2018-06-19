package com.project.paulo.bpapp.database;

public class FeatureValueDB {
    //private variables
    private String _time;
    private String _systolicPressure;
    private String _diastolicPressure;
    private String _meanPressure;
    private String _maxRateOfPressureChange;
    private String _heartRate;
    private String _dicroticNotchPressure;
    private String _dicroticPeakPressure;
    private String _abnormalities;

    // Empty constructor
    public FeatureValueDB(){

    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_systolicPressure() {
        return _systolicPressure;
    }

    public void set_systolicPressure(String _systolicPressure) {
        this._systolicPressure = _systolicPressure;
    }

    public String get_diastolicPressure() {
        return _diastolicPressure;
    }

    public void set_diastolicPressure(String _diastolicPressure) {
        this._diastolicPressure = _diastolicPressure;
    }

    public String get_meanPressure() {
        return _meanPressure;
    }

    public void set_meanPressure(String _meanPressure) {
        this._meanPressure = _meanPressure;
    }

    public String get_maxRateOfPressureChange() {
        return _maxRateOfPressureChange;
    }

    public void set_maxRateOfPressureChange(String _maxRateOfPressureChange) {
        this._maxRateOfPressureChange = _maxRateOfPressureChange;
    }

    public String get_heartRate() {
        return _heartRate;
    }

    public void set_heartRate(String _heartRate) {
        this._heartRate = _heartRate;
    }

    public String get_dicroticNotchPressure() {
        return _dicroticNotchPressure;
    }

    public void set_dicroticNotchPressure(String _dicroticNotchPressure) {
        this._dicroticNotchPressure = _dicroticNotchPressure;
    }

    public String get_dicroticPeakPressure() {
        return _dicroticPeakPressure;
    }

    public void set_dicroticPeakPressure(String _dicroticPeakPressure) {
        this._dicroticPeakPressure = _dicroticPeakPressure;
    }

    public String get_abnormalities() {
        return _abnormalities;
    }

    public void set_abnormalities(String _abnormalities) {
        this._abnormalities = _abnormalities;
    }

    // constructor
    public FeatureValueDB(String time, String systolicPressure, String diastolicPressure,
                          String meanPressure, String maxRateOfPressureChange,
                          String heartRate, String dicroticNotchPressure, String dicroticPeakPressure, String abnormalities){
        this._time = time;
        this._systolicPressure = systolicPressure;
        this._diastolicPressure = diastolicPressure;
        this._meanPressure = meanPressure;
        this._maxRateOfPressureChange = maxRateOfPressureChange;
        this._heartRate = heartRate;
        this._dicroticNotchPressure = dicroticNotchPressure;
        this._dicroticPeakPressure = dicroticPeakPressure;
        this._abnormalities = abnormalities;
    }
}
