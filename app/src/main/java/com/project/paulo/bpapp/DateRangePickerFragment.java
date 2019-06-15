package com.project.paulo.bpapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;


/**
 * A DialogFragment that show 2 date pickers.
 * from https://github.com/MedAmineTazarki/DateRangePicker
 *
 */
public class DateRangePickerFragment extends DialogFragment implements View.OnClickListener{

    private OnDateRangeSelectedListener onDateRangeSelectedListener;

    private TabHost tabHost;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button butSetDateRange;
    boolean is24HourMode;
    String startDate;
    String endDate;

    public DateRangePickerFragment() {
        // Required empty public constructor
    }

    public static DateRangePickerFragment newInstance(OnDateRangeSelectedListener callback, boolean is24HourMode, StringTuple stringTuple) {
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.initialize(callback, is24HourMode, stringTuple);
        return dateRangePickerFragment;
    }

    public void initialize(OnDateRangeSelectedListener callback,
                           boolean is24HourMode, StringTuple st) {
        onDateRangeSelectedListener = callback;
        startDate = st.startDate;
        endDate = st.endDate;
        this.is24HourMode = is24HourMode;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.date_range_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        tabHost = (TabHost) root.findViewById(R.id.tabHost);
        butSetDateRange = (Button) root.findViewById(R.id.but_set_time_range);

        //Sets the start date initially to date previously selected
        startDatePicker = (DatePicker) root.findViewById(R.id.start_date_picker);
        if(!startDate.equals("-")) {
            String startDay = startDate.substring(0, startDate.indexOf("-"));
            String startMonthYear = startDate.substring(startDate.indexOf("-")+1);
            String startMonth = startMonthYear.substring(0, startMonthYear.indexOf("-"));
            String startYear = startMonthYear.substring(startMonthYear.indexOf("-")+1);
            startDatePicker.updateDate(Integer.valueOf(startYear), Integer.valueOf(startMonth)-1, Integer.valueOf(startDay));
        }

        //Sets the end date initially to date previously selected
        endDatePicker = (DatePicker) root.findViewById(R.id.end_date_picker);
        if(!endDate.equals("-")) {
            String endDay = endDate.substring(0, endDate.indexOf("-"));
            String endMonthYear = endDate.substring(endDate.indexOf("-")+1);
            String endMonth = endMonthYear.substring(0, endMonthYear.indexOf("-"));
            String endYear = endMonthYear.substring(endMonthYear.indexOf("-")+1);
            startDatePicker.updateDate(Integer.valueOf(endYear), Integer.valueOf(endMonth)-1, Integer.valueOf(endDay));
        }

        butSetDateRange.setOnClickListener(this);
        tabHost.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec startDatePage = tabHost.newTabSpec("start");
        startDatePage.setContent(R.id.start_date_group);
        startDatePage.setIndicator(getString(R.string.title_tab_start_date));

        TabHost.TabSpec endDatePage = tabHost.newTabSpec("end");
        endDatePage.setContent(R.id.end_date_group);
        endDatePage.setIndicator(getString(R.string.ttile_tab_end_date));

        tabHost.addTab(startDatePage);
        tabHost.addTab(endDatePage);
        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener callback) {
        this.onDateRangeSelectedListener = callback;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        onDateRangeSelectedListener.onDateRangeSelected(startDatePicker.getDayOfMonth(),startDatePicker.getMonth()+1,startDatePicker.getYear(),
                endDatePicker.getDayOfMonth(),endDatePicker.getMonth()+1,endDatePicker.getYear());
    }

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear);
    }

}