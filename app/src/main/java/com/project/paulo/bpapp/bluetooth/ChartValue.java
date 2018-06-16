package com.project.paulo.bpapp.bluetooth;

public class ChartValue {
    private String chartTime = null;
    private String chartValue = null;

    public String getChartTime2() {
        return chartTime2;
    }

    public void setChartTime2(String chartTime2) {
        this.chartTime2 = chartTime2;
    }

    public String getChartValue2() {
        return chartValue2;
    }

    public void setChartValue2(String chartValue2) {
        this.chartValue2 = chartValue2;
    }

    public String getChartTime3() {
        return chartTime3;
    }

    public void setChartTime3(String chartTime3) {
        this.chartTime3 = chartTime3;
    }

    public String getChartValue3() {
        return chartValue3;
    }

    public void setChartValue3(String chartValue3) {
        this.chartValue3 = chartValue3;
    }

    private String chartTime2 = null;
    private String chartValue2 = null;
    private String chartTime3 = null;
    private String chartValue3 = null;
    private ChangeListener listener;

    public String getChartTime() {
        return chartTime;
    }

    public void setChartTime(String chartTime) {
        this.chartTime = chartTime;
    }

    public String getChartValue() {
        return chartValue;
    }

    public void setChartValue(String chartValue) {
        this.chartValue = chartValue;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
