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
        final EditText activeFrequency = layout.findViewById(R.id.edit_active_frequency);
        final EditText referenceFrequency = layout.findViewById(R.id.edit_reference_frequency);
        final EditText activeSampling = layout.findViewById(R.id.edit_active_sample);
        final EditText referenceSampling = layout.findViewById(R.id.edit_reference_sample);
        final EditText sweeps = layout.findViewById(R.id.edit_sweeps);
        final EditText samples = layout.findViewById(R.id.edit_samples);
        final EditText averages = layout.findViewById(R.id.edit_averages);
        final EditText txPower = layout.findViewById(R.id.edit_tx_pwr);
        final EditText txTime = layout.findViewById(R.id.edit_tx_t);
        final EditText rxDelay = layout.findViewById(R.id.edit_rx_delay);

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
        List<String> stringList = new ArrayList<>();
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
                    }
                }
                if(!empty){
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

    //Need to get view (from graph fragment) into constructor
    public void setHintValues(List<String> valueList, List<EditText> list){

        for(String s : valueList){
            s.replaceAll("[^0-9.]", "");
        }

        int index = 0;

        //Put in check to see if lists are of equal lengths so index errors can be caught
        System.out.println(list.size());
        System.out.println(valueList.size());
        if(valueList.size()==list.size()) {
            for (EditText e : list) {
                if (valueList.get(index).isEmpty()) {
                    e.setText("000");
                    System.out.println("In here");
                } else {
                    e.setText(valueList.get(index));
                    System.out.println("Actually here");
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
