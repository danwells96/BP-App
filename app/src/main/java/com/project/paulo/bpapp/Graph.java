package com.project.paulo.bpapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.paulo.bpapp.bluetooth.BluetoothChatFragment;
import com.project.paulo.bpapp.bluetooth.ChartValue;
import com.project.paulo.bpapp.common.CSVWriter;
import com.project.paulo.bpapp.common.logger.Log;
import com.project.paulo.bpapp.common.logger.LogWrapper;
import com.project.paulo.bpapp.database.ChartValueDB;
import com.project.paulo.bpapp.database.DatabaseHandler;
import com.project.paulo.bpapp.database.FeatureValueDB;
import com.project.paulo.bpapp.featureextraction.DiastolicPressure;
import com.project.paulo.bpapp.featureextraction.DicroticNotch;
import com.project.paulo.bpapp.featureextraction.DicroticPeak;
import com.project.paulo.bpapp.featureextraction.MaxPressureChangeRate;
import com.project.paulo.bpapp.featureextraction.SystolicPressure;
import com.project.paulo.bpapp.featureextraction.WabpJAVA;
import com.project.paulo.bpapp.mathematics.ArrayIndex;
import com.project.paulo.bpapp.mathematics.ArrayMean;
import com.project.paulo.bpapp.mathematics.Filter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph extends Fragment implements OnChartValueSelectedListener, ReaderDialog.ParameterListener {

    public static final String TAG = "GraphFragment";

    DatabaseHandler databaseHandler;

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    ChartValue chartValue = new ChartValue();
    BluetoothChatFragment fragment = new BluetoothChatFragment();

    // Reader parameters
    int arsr = 25; // ms

    // Feature extraction parameters
    ArrayList<Double> pap = new ArrayList<>();
    int windowLength = 20; // seconds

    // CHART CODE
    private LineChart mChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    // C++ LIBRARY DECLARATION
    // Used to load the C++ library on application startup.
    static {
        System.loadLibrary("mult");
        System.loadLibrary("wabp");
    }

    public native double mult(double a, double b);
    public native void wabp(double[] abp, double[] onsets_data, int[] onsets_size);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph, container, false);
    }


    ArrayList<String> tvList = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Need to get these values from memory to fill out
        final View readerParams = getActivity().findViewById(R.id.readerParams);


        //Need to add rest of strings into list
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_activeFrequencyValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_referenceFrequencyValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_activeSampleValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_referenceSampleValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_sweepsValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_samplesValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_averageValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_txPowerValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_txTimeValue)).getText().toString());
        tvList.add(((TextView)readerParams.findViewById(R.id.textView_rxDelayValue)).getText().toString());

        readerParams.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putStringArrayList("values", tvList);

                ReaderDialog rd = new ReaderDialog();
                rd.setArguments(b);

                rd.setCancelable(false);

                rd.show(getFragmentManager(), "ReaderDialog");

                rd.setTargetFragment(Graph.this, 1);
            }
        });

        getActivity().setTitle("Graph");
    }

    //Interface method to get reader parameters from ReaderDialog.class
    @Override
    public void onFinishDialog(List<EditText> etList) {
        View v = getActivity().findViewById(R.id.readerParams);

        ((TextView)v.findViewById(R.id.textView_activeFrequencyValue)).setText(etList.get(0).getText()+" MHz");
        ((TextView)v.findViewById(R.id.textView_referenceFrequencyValue)).setText(etList.get(1).getText()+" MHz");
        ((TextView)v.findViewById(R.id.textView_activeSampleValue)).setText(etList.get(2).getText()+" ms");
        ((TextView)v.findViewById(R.id.textView_referenceSampleValue)).setText(etList.get(3).getText()+" s");
        ((TextView)v.findViewById(R.id.textView_sweepsValue)).setText(etList.get(4).getText());
        ((TextView)v.findViewById(R.id.textView_samplesValue)).setText(etList.get(5).getText());
        ((TextView)v.findViewById(R.id.textView_averageValue)).setText(etList.get(6).getText());
        ((TextView)v.findViewById(R.id.textView_txPowerValue)).setText(etList.get(7).getText());
        ((TextView)v.findViewById(R.id.textView_txTimeValue)).setText(etList.get(8).getText());
        ((TextView)v.findViewById(R.id.textView_rxDelayValue)).setText(etList.get(9).getText());

        tvList.clear();
        tvList.add(((TextView)v.findViewById(R.id.textView_activeFrequencyValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_referenceFrequencyValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_activeSampleValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_referenceSampleValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_sweepsValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_samplesValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_averageValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_txPowerValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_txTimeValue)).getText().toString());
        tvList.add(((TextView)v.findViewById(R.id.textView_rxDelayValue)).getText().toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        initializeLogging();
    }

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
//        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
//        logWrapper.setNext(msgFilter);
//
//        // On screen logging via a fragment with a TextView.
//        LogFragment logFragment = (LogFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.log_fragment);
//        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
        Log.i(TAG, Double.toString(mult(2.0, 4.0)));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        // Reader parameter logic



        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activeResonatorFrequencyText = ((TextView)getActivity().findViewById(R.id.textView_activeFrequencyValue)).getText().toString().replaceAll("[^0-9.]", "");
                String referenceResonatorFrequencyText = ((TextView)getActivity().findViewById(R.id.textView_referenceFrequencyValue)).getText().toString().replaceAll("[^0-9.]", "");
                String activeResonatorSamplingRateText = ((TextView)getActivity().findViewById(R.id.textView_activeSampleValue)).getText().toString().replaceAll("[^0-9.]", "");
                String referenceResonatorSamplingRateText = ((TextView)getActivity().findViewById(R.id.textView_referenceSampleValue)).getText().toString().replaceAll("[^0-9.]", "");
                String sweepsText = ((TextView)getActivity().findViewById(R.id.textView_sweepsValue)).getText().toString().replaceAll("[^0-9.]", "");
                String samplesText = ((TextView)getActivity().findViewById(R.id.textView_samplesValue)).getText().toString().replaceAll("[^0-9.]", "");
                String averagesText = ((TextView)getActivity().findViewById(R.id.textView_averageValue)).getText().toString().replaceAll("[^0-9.]", "");
                String txPowerText = ((TextView)getActivity().findViewById(R.id.textView_txPowerValue)).getText().toString().replaceAll("[^0-9.]", "");
                String txTimeText = ((TextView)getActivity().findViewById(R.id.textView_txTimeValue)).getText().toString().replaceAll("[^0-9.]", "");
                String rxDelayText = ((TextView)getActivity().findViewById(R.id.textView_rxDelayValue)).getText().toString().replaceAll("[^0-9.]", "");


                fragment.sendMessage("arf" + activeResonatorFrequencyText +
                        "rrf" + referenceResonatorFrequencyText +
                        "ars" + activeResonatorSamplingRateText +
                        "rrs" + referenceResonatorSamplingRateText +
                        "swe" + sweepsText +
                        "sam" + samplesText +
                        "ave" + averagesText +
                        "txp" + txPowerText +
                        "txt" + txTimeText +
                        "rxd" + rxDelayText);

                Toast.makeText(getActivity(), "Parameters sent to reader.",
                        Toast.LENGTH_LONG).show();
            }
        });

        databaseHandler = new DatabaseHandler(getActivity());

        // DATABASE EXPORT CODE
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("Exporting database ...");

        // CHART CODE
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        chartValue.setListener(new ChartValue.ChangeListener() {
            @Override
            public void onChange() {
                if(chartValue.getChartValue().contains(".")) {
                    Log.e(TAG, chartValue.getChartTime() + " "+ chartValue.getChartValue());

                    String x = null;
                    String y = null;

                    Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
                    Matcher matcher = regex.matcher(chartValue.getChartTime());
                    while(matcher.find()){
                        x = matcher.group(1);
                    }
                    matcher = regex.matcher(chartValue.getChartValue());
                    while(matcher.find()){
                        y = matcher.group(1);
                    }

                    ChartValueDB newData = new ChartValueDB(getCurrentLocalDateTimeStamp(), y);

                    databaseHandler.addData(newData);

                    addEntry(Float.parseFloat(x)/1000, Float.parseFloat(y));

                    Log.e(TAG, chartValue.getChartTime2());
                    Log.e(TAG, chartValue.getChartValue2());

//                    matcher = regex.matcher(chartValue.getChartTime2());
//                    while(matcher.find()){
//                        x = matcher.group(1);
//                    }
//                    matcher = regex.matcher(chartValue.getChartValue2());
//                    while(matcher.find()){
//                        y = matcher.group(1);update
//                    }

//                    addEntry(Float.parseFloat(chartValue.getChartTime2())/1000, Float.parseFloat(chartValue.getChartValue2()));

//                    matcher = regex.matcher(chartValue.getChartTime3());
//                    while(matcher.find()){
//                        x = matcher.group(1);
//                    }
//                    matcher = regex.matcher(chartValue.getChartValue3());
//                    while(matcher.find()){
//                        y = matcher.group(1);
//                    }

//                    addEntry(Float.parseFloat(chartValue.getChartTime3())/1000, Float.parseFloat(chartValue.getChartValue3()));

//                    Log.w(TAG, Arrays.toString(databaseHandler.getAllData().toArray()));

                    // FEATURE EXTRACTION
                    if (pap.size() < windowLength*(1000/arsr)) {
                        pap.add(Double.parseDouble(y));
                    } else {
                        double[] abp = new double[pap.size()];
                        for(int i = 0; i < pap.size(); i++)
                        {
                            abp[i] = pap.get(i);
                        }

                        Log.e(TAG, "Async Start");

                        new FeatureExtraction().execute(abp);

                        pap.clear();
                    }
                }
            }
        });

        fragment.setChartValue(chartValue);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        // CHART CODE
        mChart = (LineChart) getActivity().findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // set description text
        mChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Time (s)");
        mChart.setDescription(description);
        TextView yAxisTitle = getActivity().findViewById(R.id.y_axis_title);
        yAxisTitle.setTextColor(Color.WHITE);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.BLACK);
        mChart.setDrawGridBackground(true);
       // mChart.setGridBackgroundColor(getResources().getColor(R.color.medicalGreen));
        mChart.setGridBackgroundColor(Color.BLACK);

        LineData data = new LineData();
        data.setValueTextColor(getResources().getColor(R.color.medicalGreen));
        // add empty data
        mChart.setData(data);
        mChart.setExtraOffsets(10, 10, 0, 10);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(mTfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setTextColor(getResources().getColor(R.color.medicalGreen));
        xl.setDrawGridLines(true);
        xl.setDrawAxisLine(true);
        xl.setGridColor(getResources().getColor(R.color.medicalGreen));
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(60f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.medicalGreen));
        leftAxis.setTextColor(getResources().getColor(R.color.medicalGreen));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private double previousSystolicPressure = -1;
    private double previousDiastolicPressure = -1;
    private double previousMeanPressure = -1;

    private boolean isSystolicHigh = false;
    private boolean isDiastolicLow = false;
    private boolean isMeanHigh = false;
    private boolean isHeartRateHigh = false;
    private boolean isMaxPressureLow = false;
    private boolean isNotchPressureAbnormal = false;
    private boolean isPeakPressureAbnoral = false;
    private boolean isSystolicDifferingMuch = false;
    private boolean isDiastolicDifferingMuch = false;
    private boolean isMeanDifferingMuch = false;

    private String isSystolicHighText = "Systolic pressure is above 70mmHg";
    private String isDiastolicLowText = "Diastolic pressure is below 25mmHg";
    private String isMeanHighText = "Mean pressure is above 40mmHg";
    private String isHeartRateHighText = "Heart rate is above 100bpm";
    private String isMaxPressureLowText = "Max dp/dt is below 25mmHg/s";
    private String isNotchPressureAbnormalText = "Dicrotic notch pressure is abnormal";
    private String isPeakPressureAbnoralText = "Dicrotic peak pressure is weak";
    private String isSystolicDifferingMuchText = "Systolic pressure rise is above 20mmHg";
    private String isDiastolicDifferingMuchText = "Diastolic pressure rise is above 20mmHg";
    private String isMeanDifferingMuchText = "Mean pressure rise is above 15mmHg";

    private class FeatureExtraction extends AsyncTask<double[], Integer, double[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected double[] doInBackground(double[]... params) {
            double[] abp = params[0];

//            double[] onsets1 = {};
//            int[] onsets_size = {};
//
//            wabp(abp, onsets1, onsets_size);
//
//            Log.e(TAG, "Calculated onsets");
//            Log.e(TAG, Double.toString(onsets1[0]));

            double[] temp = new double[abp.length];
            System.arraycopy(abp, 0, temp, 0, abp.length);

            // FILTERING
            double[] bFilter = {0.1509, 0.1509};
            double[] aFilter = {1, -0.6981};
            temp = Filter.filter(bFilter, aFilter, temp);

            // BEAT ONSET DETECTION
            long startTime = System.nanoTime();

            int[] onsets = WabpJAVA.wabpJAVA(temp);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000000;

            Log.e(TAG, "Onset indexes = " + Arrays.toString(onsets));
            Log.e(TAG, "Took in seconds: " + Long.toString(duration));

            double[][] beats = new double[onsets.length - 1][];

            for (int i = 0; i < onsets.length - 1; i++) {
                beats[i] = new double[onsets[i + 1] - onsets[i]];
                System.arraycopy(abp, onsets[i], beats[i], 0, onsets[i + 1] - onsets[i]);
            }

            Log.e(TAG,"");

            // SYSTOLIC

            double[] systolicPressures = new double[beats.length];

            for (int i = 0; i < systolicPressures.length; i++) {
                systolicPressures[i] = SystolicPressure.getSystolicPressure(beats[i]);
            }

            double systolicPressure = ArrayMean.getArrayMean(systolicPressures);
            Log.e(TAG, "Systolic Pressure = " + Double.toString(systolicPressure));

            // DIASTOLIC

            double[] diastolicPressures = new double[beats.length];

            for (int i = 0; i < diastolicPressures.length; i++) {
                int systolicIndex = ArrayIndex.getArrayIndex(beats[i], systolicPressures[i]);
                double[] tempBeat = new double[beats[i].length-systolicIndex];
                System.arraycopy(beats[i], systolicIndex, tempBeat, 0, beats[i].length-systolicIndex);

                diastolicPressures[i] = DiastolicPressure.getDiastolicPressure(tempBeat, systolicPressures[i]);
            }

            double diastolicPressure = ArrayMean.getArrayMean(diastolicPressures);
            Log.e(TAG, "Diastolic Pressure = " + Double.toString(diastolicPressure));

            // MEAN

            double[] meanPressures = new double[beats.length];

            for (int i = 0; i < meanPressures.length; i++) {
                meanPressures[i] = (systolicPressures[i] + 2*diastolicPressures[i])/3.0;
            }

            double meanPressure = ArrayMean.getArrayMean(meanPressures);
            Log.e(TAG, "Mean Pressure = " + Double.toString(meanPressure));

            // HEART RATE

            double[] heartRates = new double[beats.length];

            for (int i = 0; i < heartRates.length; i++) {
                heartRates[i] = 60.0/(beats[i].length*arsr/1000.0);
            }

            double heartRate = ArrayMean.getArrayMean(heartRates);
            Log.e(TAG, "Heart Rate = " + Double.toString(heartRate));

            // DICROTIC NOTCH

            double[] dicroticNotches = new double[beats.length];

            for (int i = 0; i < dicroticNotches.length; i++) {
                dicroticNotches[i] = DicroticNotch.getDicroticNotch(beats[i], systolicPressures[i]);

            }

            double dicroticNotch = ArrayMean.getArrayMean(dicroticNotches);
            Log.e(TAG, "Dicrotic Notch = " + Double.toString(dicroticNotch));

            // DICROTIC PEAK

            double[] dicroticPeaks = new double[beats.length];

            for (int i = 0; i < dicroticPeaks.length; i++) {
                dicroticPeaks[i] = DicroticPeak.getDicroticPeak(beats[i], systolicPressures[i]);
            }

            double dicroticPeak = ArrayMean.getArrayMean(dicroticPeaks);
            Log.e(TAG, "Dicrotic Peak = " + Double.toString(dicroticPeak));

            // MAX DP/DT

            double[] maxPressureChangeRates = new double[beats.length];

            for (int i = 0; i < maxPressureChangeRates.length; i++) {
                maxPressureChangeRates[i] = MaxPressureChangeRate.getMaxPressureChangeRate(beats[i], systolicPressures[i], arsr);
            }

            double maxPressureChangeRate = ArrayMean.getArrayMean(maxPressureChangeRates);
            Log.e(TAG, "Max dp/dt = " + Double.toString(maxPressureChangeRate));

            double[] features = {systolicPressure, diastolicPressure, meanPressure, heartRate, maxPressureChangeRate, dicroticNotch, dicroticPeak};

            // ABNORMALITY DETECTION

            if(previousSystolicPressure == -1){
                previousSystolicPressure = systolicPressure;
            }

            if(previousDiastolicPressure == -1){
                previousDiastolicPressure = diastolicPressure;
            }

            if(previousMeanPressure == -1){
                previousMeanPressure = meanPressure;
            }

            isSystolicHigh = systolicPressure > 70;
            isDiastolicLow = diastolicPressure < 25;
            isMeanHigh = meanPressure > 40;
            isHeartRateHigh = heartRate > 100;
            isMaxPressureLow = maxPressureChangeRate < 25;
            isNotchPressureAbnormal = Math.abs(dicroticNotch - diastolicPressure) < 1 || Math.abs(dicroticNotch - systolicPressure) < 1;
            isPeakPressureAbnoral = Math.abs(dicroticPeak - dicroticNotch) < 1;
            isSystolicDifferingMuch = Math.abs(systolicPressure - previousSystolicPressure) > 20;
            isDiastolicDifferingMuch = Math.abs(diastolicPressure - previousDiastolicPressure) > 20;
            isMeanDifferingMuch = Math.abs(meanPressure - previousMeanPressure) > 15;

            previousSystolicPressure = systolicPressure;
            previousDiastolicPressure = diastolicPressure;
            previousMeanPressure = meanPressure;

            return features;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(double[] features) {
            super.onPostExecute(features);

            String abnormalities = "";

            // UPDATE UI

            TextView systolicTextView = getActivity().findViewById(R.id.systolicValue);
            //systolicTextView.setError(null);
            systolicTextView.setText(String.format("%.1f", features[0]));

            /*if(isSystolicHigh && !isSystolicDifferingMuch){
                systolicTextView.setError(isSystolicHighText);
                abnormalities = abnormalities + isSystolicHigh;
            } else if(!isSystolicHigh && isSystolicDifferingMuch){
                systolicTextView.setError(isSystolicDifferingMuchText);
                abnormalities = abnormalities + isSystolicDifferingMuchText;
            } else if(isSystolicHigh && isSystolicDifferingMuch) {
                systolicTextView.setError(isSystolicHighText + " and " + isSystolicDifferingMuchText);
                abnormalities = abnormalities + isSystolicHighText + " and " + isSystolicDifferingMuchText;
            }

*/
            TextView diastolicTextView = getActivity().findViewById(R.id.diastolicValue);
            //diastolicTextView.setError(null);
            diastolicTextView.setText(String.format("%.1f", features[1]));

            /*if(isDiastolicLow && !isDiastolicDifferingMuch){
                diastolicTextView.setError(isDiastolicLowText);
                abnormalities = abnormalities + " and " + isDiastolicLowText;
            } else if(!isDiastolicLow && isDiastolicDifferingMuch){
                diastolicTextView.setError(isDiastolicDifferingMuchText);
                abnormalities = abnormalities + " and " + isDiastolicDifferingMuchText;
            } else if(isDiastolicLow && isDiastolicDifferingMuch) {
                diastolicTextView.setError(isDiastolicLowText + " and " + isDiastolicDifferingMuchText);
                abnormalities = abnormalities + isDiastolicLowText + " and " + isDiastolicDifferingMuchText;
            }

            TextView meanTextView = getActivity().findViewById(R.id.mean);
            meanTextView.setError(null);
            meanTextView.setText(Double.toString(features[2]));

            if(isMeanHigh && !isMeanDifferingMuch){
                meanTextView.setError(isMeanHighText);
                abnormalities = abnormalities + " and " + isMeanHighText;
            } else if(!isMeanHigh && isMeanDifferingMuch){
                meanTextView.setError(isMeanDifferingMuchText);
                abnormalities = abnormalities + " and " + isMeanDifferingMuchText;
            } else if(isMeanHigh && isMeanDifferingMuch) {
                meanTextView.setError(isMeanHighText + " and " + isMeanDifferingMuchText);
                abnormalities = abnormalities + isMeanHighText + " and " + isMeanDifferingMuchText;
            }

*/
            TextView heartRateTextView = getActivity().findViewById(R.id.heartRate);
            //heartRateTextView.setError(null);
            heartRateTextView.setText(String.format("%.0f bpm", features[3]));

            /*if(isHeartRateHigh){
                heartRateTextView.setError(isHeartRateHighText);
                abnormalities = abnormalities + " and " + isHeartRateHighText;
            }

            TextView maxPressureChangeRateTextView = getActivity().findViewById(R.id.max_pressure_change);
            maxPressureChangeRateTextView.setError(null);
            maxPressureChangeRateTextView.setText(Double.toString(features[4]));

            if(isMaxPressureLow){
                maxPressureChangeRateTextView.setError(isMaxPressureLowText);
                abnormalities = abnormalities + " and " + isMaxPressureLowText;
            }

            TextView dicroticNotchTextView = getActivity().findViewById(R.id.dicrotic_notch);
            dicroticNotchTextView.setError(null);
            dicroticNotchTextView.setText(Double.toString(features[5]));

            if(isNotchPressureAbnormal) {
                dicroticNotchTextView.setError(isNotchPressureAbnormalText);
                abnormalities = abnormalities + " and " + isNotchPressureAbnormalText;
            }

            TextView dicroticPeakTextView = getActivity().findViewById(R.id.dicrotic_peak);
            dicroticPeakTextView.setError(null);
            dicroticPeakTextView.setText(Double.toString(features[6]));

            if(isPeakPressureAbnoral) {
                dicroticPeakTextView.setError(isPeakPressureAbnoralText);
                abnormalities = abnormalities + " and " + isPeakPressureAbnoralText;
            }
            */
            FeatureValueDB newData = new FeatureValueDB(getCurrentLocalDateTimeStamp(),
                    Double.toString(features[0]), Double.toString(features[1]),
                            Double.toString(features[2]), Double.toString(features[4]),
                                    Double.toString(features[3]), Double.toString(features[5]), Double.toString(features[6]), abnormalities);

            databaseHandler.addFeatureData(newData);

            Log.e(TAG, "Async End");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actionClear: {
                mChart.clearValues();
                Toast.makeText(getActivity(), "Chart cleared", Toast.LENGTH_SHORT).show();
                return true;
//                break;
            }
            case R.id.exportData: {
                /** Show the progress dialog window */
                mProgressDialog.show();
                new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            }
        }

        return false;
//        return super.onOptionsItemSelected(item);
    }

    // CHART CODE
    private void addEntry(float x, float y) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(x, y), 0);

            data.notifyDataChanged();


            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(5);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // adjust y axis
            // mChart.getAxisLeft().resetAxisMinimum();
            mChart.getAxisLeft().resetAxisMaximum();

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Pressure (mmHg)");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(getResources().getColor(R.color.medicalGreen));
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(R.color.medicalGreen);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        android.util.Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        android.util.Log.i("Nothing selected", "Nothing selected.");
    }

    private int mProgressStatus = 0;
    private ProgressDialog mProgressDialog ;

    public class ExportDatabaseCSVTask extends AsyncTask<String, Integer, Boolean> {

        DatabaseHandler databaseHandler;
        int numberOFDatabaseRows;
        double mProgressDouble;

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "Exporting database", Toast.LENGTH_SHORT).show();
            databaseHandler = new DatabaseHandler(getActivity());
            mProgressStatus = 0;
            mProgressDouble = 0.0;
        }

        protected Boolean doInBackground(final String... args) {

            numberOFDatabaseRows = databaseHandler.getDataCount() + databaseHandler.getDataCount2();

            File exportDirectory = new File(Environment.getExternalStorageDirectory(), "/bpapp/");
            if (!exportDirectory.exists()) { exportDirectory.mkdirs(); }

            File file = new File(exportDirectory, "blood_pressure_data.csv");
            try {
                file.createNewFile();
                CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
                Cursor cursor = databaseHandler.raw();
                csvWriter.writeNext(cursor.getColumnNames());
                while(cursor.moveToNext()) {
                    String[] newRow = new String[cursor.getColumnNames().length];
                    for(int i = 0; i < cursor.getColumnNames().length; i++)
                    {
                        newRow[i] = cursor.getString(i);
                    }
                    csvWriter.writeNext(newRow);

                    mProgressDouble = mProgressDouble + 1.0/((double) numberOFDatabaseRows);
                    mProgressStatus = (int) (100*mProgressDouble);
                    publishProgress(mProgressStatus);
                }
                csvWriter.close();
                cursor.close();
            } catch (IOException e) {
                return false;
            }

                File file2 = new File(exportDirectory, "blood_pressure_features.csv");
                try {
                    file2.createNewFile();
                    CSVWriter csvWriter2 = new CSVWriter(new FileWriter(file2));
                    Cursor cursor2 = databaseHandler.raw2();
                    csvWriter2.writeNext(cursor2.getColumnNames());
                    while(cursor2.moveToNext()) {
                        String[] newRow = new String[cursor2.getColumnNames().length];
                        for(int i = 0; i < cursor2.getColumnNames().length; i++)
                        {
                            newRow[i] = cursor2.getString(i);
                        }
                        csvWriter2.writeNext(newRow);

                        mProgressDouble = mProgressDouble + 1.0/((double) numberOFDatabaseRows);
                        mProgressStatus = (int) (100*mProgressDouble);
                        publishProgress(mProgressStatus);
                    }
                    csvWriter2.close();
                    cursor2.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressDialog.setProgress(mProgressStatus);
        }

        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(getActivity(), "Export successful", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Export failed", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        }
    }

    public String getCurrentLocalDateTimeStamp() {
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);

        return strDate;
    }
}
