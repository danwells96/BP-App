package com.project.paulo.bpapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ReaderDialog extends DialogFragment {
    public List<EditText> etList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup_readerparams, null);

        //EditText objects from alert dialog so updated values can be saved
        EditText activeFrequency = layout.findViewById(R.id.edit_active_frequency);
        EditText referenceFrequency = layout.findViewById(R.id.edit_reference_frequency);
        EditText activeSampling = layout.findViewById(R.id.edit_active_sample);
        EditText referenceSampling = layout.findViewById(R.id.edit_reference_sample);
        EditText sweeps = layout.findViewById(R.id.edit_sweeps);
        EditText samples = layout.findViewById(R.id.edit_samples);
        EditText averages = layout.findViewById(R.id.edit_averages);
        EditText txPower = layout.findViewById(R.id.edit_tx_pwr);
        EditText txTime = layout.findViewById(R.id.edit_tx_t);
        EditText rxDelay = layout.findViewById(R.id.edit_rx_delay);

        etList.add(activeFrequency);
        etList.add(referenceFrequency);
        etList.add(activeSampling);
        etList.add(referenceSampling);
        etList.add(sweeps);
        etList.add(samples);
        etList.add(averages);
        etList.add(txPower);
        etList.add(txTime);
        etList.add(rxDelay);

        //Get stringlist from bundle savedinstancestate
        List<String> stringList;
        Bundle b = getArguments();
        stringList = b.getStringArrayList("values");
        this.setHintValues(stringList, etList);


        builder.setView(layout);
        builder.setPositiveButton("Save Values", null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog ad = (AlertDialog) getDialog();
        Button save = ad.getButton(AlertDialog.BUTTON_POSITIVE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean empty = false;
                for(EditText e : etList){
                    if(e.getText().toString().isEmpty()){
                        empty = true;
                        e.setError("This field can not be blank");
                    }else{
                        e.setError(null);
                    }
                }
                //checkParameterValues();
                if(!empty && !checkParameterValues()){
                    ad.dismiss();
                    //Put function storing values into graph fragment here
                    ParameterListener p = (ParameterListener) getTargetFragment();
                    p.onFinishDialog(etList);
                }else{
                    Toast.makeText(getContext(), "Empty fields must be completed before saving", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkParameterValues(){

        Boolean isThereAnErrorInParams = false;

        String activeResonatorFrequencyText = etList.get(0).getText().toString().replaceAll("[^0-9.]", "");
        String referenceResonatorFrequencyText = etList.get(1).getText().toString().replaceAll("[^0-9.]", "");
        String activeResonatorSamplingRateText = etList.get(2).getText().toString().replaceAll("[^0-9.]", "");
        String referenceResonatorSamplingRateText = etList.get(3).getText().toString().replaceAll("[^0-9.]", "");
        String sweepsText = etList.get(4).getText().toString().replaceAll("[^0-9.]", "");
        String samplesText = etList.get(5).getText().toString().replaceAll("[^0-9.]", "");
        String averagesText = etList.get(6).getText().toString().replaceAll("[^0-9.]", "");
        String txPowerText = etList.get(7).getText().toString().replaceAll("[^0-9.]", "");
        String txTimeText = etList.get(8).getText().toString().replaceAll("[^0-9.]", "");
        String rxDelayText = etList.get(9).getText().toString().replaceAll("[^0-9.]", "");



        if(Double.parseDouble(activeResonatorFrequencyText) > 920.0 ||
                Double.parseDouble(activeResonatorFrequencyText) < 915.0 ||
                Double.parseDouble(activeResonatorFrequencyText) > Double.parseDouble(referenceResonatorFrequencyText)) {

            isThereAnErrorInParams = true;

            etList.get(0).setError("Must be in range 915-920Mhz and less than reference resonator frequency.");
        }

        if(Double.parseDouble(referenceResonatorFrequencyText) > 920.0 ||
                Double.parseDouble(referenceResonatorFrequencyText) < 915.0) {

            isThereAnErrorInParams = true;

            etList.get(1).setError("Must be in range 915-920Mhz.");
        }

        if(Double.parseDouble(activeResonatorSamplingRateText) > 100 ||
                Double.parseDouble(activeResonatorSamplingRateText) < 5) {

            isThereAnErrorInParams = true;

            etList.get(2).setError("Must be in range 5-100ms.");
        }

        if(Double.parseDouble(referenceResonatorSamplingRateText) > 7 ||
                Double.parseDouble(referenceResonatorSamplingRateText) < 1) {

            isThereAnErrorInParams = true;

            etList.get(3).setError("Must be in range 1-7s.");
        }

        if(Double.parseDouble(sweepsText) > 2047 ||
                Double.parseDouble(sweepsText) < 1) {

            isThereAnErrorInParams = true;

            etList.get(4).setError("Must be in range 1-2047.");
        }

        if(Double.parseDouble(samplesText) > 1000 ||
                Double.parseDouble(samplesText) < 64) {

            isThereAnErrorInParams = true;

            etList.get(5).setError("Must be in range 64-1000.");
        }

        if(Double.parseDouble(averagesText) > 999 ||
                Double.parseDouble(averagesText) < 63 ||
                Double.parseDouble(averagesText) > Double.parseDouble(samplesText)) {

            isThereAnErrorInParams = true;

            etList.get(6).setError("Must be in range 63-999 and less than samples.");
        }

        if(Double.parseDouble(txPowerText) > 1023 ||
                Double.parseDouble(txPowerText) < 0) {

            isThereAnErrorInParams = true;

            etList.get(7).setError("Must be in range 0-1023.");
        }

        if(Double.parseDouble(txTimeText) > 1023 ||
                Double.parseDouble(txTimeText) < 0) {

            isThereAnErrorInParams = true;

            etList.get(8).setError("Must be in range 0-1023.");
        }

        if(Double.parseDouble(rxDelayText) > 1023 ||
                Double.parseDouble(rxDelayText) < 0) {

            isThereAnErrorInParams = true;

            etList.get(9).setError("Must be in range 0-1023.");
        }
        return isThereAnErrorInParams;
    }


    //Need to get view (from graph fragment) into constructor
    public void setHintValues(List<String> valueList, List<EditText> etList){
        List<String> tmpList = new ArrayList<>();

        for(String s : valueList){
            String tmp = s.replaceAll("[^0-9.]", "");
            tmpList.add(tmp);
        }

        int index = 0;

        //Put in check to see if lists are of equal lengths so index errors can be caught
        if(tmpList.size()==etList.size()) {
            for (EditText e : etList) {
                if (tmpList.get(index).isEmpty()) {
                    e.setText("");
                } else {
                    e.setText(tmpList.get(index));
                }
                index++;
            }
        }else{
            //Catch index out of bounds exception
        }


    }

    public interface ParameterListener{
        void onFinishDialog(List<EditText> etList);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
