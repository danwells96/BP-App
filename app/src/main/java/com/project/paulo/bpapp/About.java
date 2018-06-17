package com.project.paulo.bpapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("About");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences aboutPrefs;
        aboutPrefs = getActivity().getSharedPreferences("aboutPrefID", Context.MODE_PRIVATE);

        String clinicianName = aboutPrefs.getString("clinianName","");
        if(!clinicianName.isEmpty()) {
            EditText clinicianNameField = getActivity().findViewById(R.id.clinician_name);
            clinicianNameField.setText(clinicianName);
        }

        String clinicianEmail = aboutPrefs.getString("clinianEmail","");
        if(!clinicianEmail.isEmpty()) {
            EditText clinicianEmailField = getActivity().findViewById(R.id.clinician_email);
            clinicianEmailField.setText(clinicianEmail);
        }

        String clinicianPhone = aboutPrefs.getString("clinianPhone","");
        if(!clinicianPhone.isEmpty()) {
            EditText clinicianPhoneField = getActivity().findViewById(R.id.clinician_phone);
            clinicianPhoneField.setText(clinicianPhone);
        }

        String clinicianPractice = aboutPrefs.getString("clinianPractice","");
        if(!clinicianPractice.isEmpty()) {
            EditText clinicianPracticeField = getActivity().findViewById(R.id.clinician_practice);
            clinicianPracticeField.setText(clinicianPractice);
        }

        String patientName = aboutPrefs.getString("patientName","");
        if(!patientName.isEmpty()) {
            EditText patientNameField = getActivity().findViewById(R.id.patient_name);
            patientNameField.setText(patientName);
        }

        String patientEmail = aboutPrefs.getString("patientEmail","");
        if(!patientEmail.isEmpty()) {
            EditText patientEmailField = getActivity().findViewById(R.id.patient_email);
            patientEmailField.setText(patientEmail);
        }

        String patientPhone = aboutPrefs.getString("patientPhone","");
        if(!patientPhone.isEmpty()) {
            EditText patientPhoneField = getActivity().findViewById(R.id.patient_phone);
            patientPhoneField.setText(patientPhone);
        }

        String patientAddress = aboutPrefs.getString("patientAddress","");
        if(!patientAddress.isEmpty()) {
            EditText patientAddressField = getActivity().findViewById(R.id.patient_address);
            patientAddressField.setText(patientAddress);
        }

        String patientPostCode = aboutPrefs.getString("patientPostCode","");
        if(!patientPostCode.isEmpty()) {
            EditText patientPostCodeField = getActivity().findViewById(R.id.patient_post_code);
            patientPostCodeField.setText(patientPostCode);
        }

        String patientCity = aboutPrefs.getString("patientCity","");
        if(!patientCity.isEmpty()) {
            EditText patientCityField = getActivity().findViewById(R.id.patient_city);
            patientCityField.setText(patientCity);
        }

        String patientCountry = aboutPrefs.getString("patientCountry","");
        if(!patientCountry.isEmpty()) {
            EditText patientCountryField = getActivity().findViewById(R.id.patient_country);
            patientCountryField.setText(patientCountry);
        }

        String patientReaderSerial = aboutPrefs.getString("patientReaderSerial","");
        if(!patientReaderSerial.isEmpty()) {
            EditText patientReaderSerialField = getActivity().findViewById(R.id.patient_reader_serial);
            patientReaderSerialField.setText(patientReaderSerial);
        }

        Button button = getActivity().findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences aboutPrefs;
                aboutPrefs = getActivity().getSharedPreferences("aboutPrefID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = aboutPrefs.edit();

                EditText clinicianNameField = getActivity().findViewById(R.id.clinician_name);
                if(!clinicianNameField.getText().toString().isEmpty()) {
                    editor.putString("clinicianName", clinicianNameField.getText().toString());
                }

                EditText clinicianEmailField = getActivity().findViewById(R.id.clinician_email);
                if(!clinicianEmailField.getText().toString().isEmpty()) {
                    editor.putString("clinicianEmail", clinicianEmailField.getText().toString());
                }

                EditText clinicianPhoneField = getActivity().findViewById(R.id.clinician_phone);
                if(!clinicianPhoneField.getText().toString().isEmpty()) {
                    editor.putString("clinicianPhone", clinicianPhoneField.getText().toString());
                }

                EditText clinicianPracticeField = getActivity().findViewById(R.id.clinician_practice);
                if(!clinicianPracticeField.getText().toString().isEmpty()) {
                    editor.putString("clinicianPractice", clinicianPracticeField.getText().toString());
                }

                EditText patientNameField = getActivity().findViewById(R.id.patient_name);
                if(!patientNameField.getText().toString().isEmpty()) {
                    editor.putString("patientName", patientNameField.getText().toString());
                    TextView navigationName = getActivity().findViewById(R.id.navigation_name);
                    navigationName.setText(patientNameField.getText().toString());
                }

                EditText patientEmailField = getActivity().findViewById(R.id.patient_email);
                if(!patientEmailField.getText().toString().isEmpty()) {
                    editor.putString("patientEmail", patientEmailField.getText().toString());
                    TextView navigationEmail = getActivity().findViewById(R.id.navigation_email);
                    navigationEmail.setText(patientEmailField.getText().toString());
                }

                EditText patientPhoneField = getActivity().findViewById(R.id.patient_phone);
                if(!patientPhoneField.getText().toString().isEmpty()) {
                    editor.putString("patientPhone", patientPhoneField.getText().toString());
                }

                EditText patientAddressField = getActivity().findViewById(R.id.patient_address);
                if(!patientAddressField.getText().toString().isEmpty()) {
                    editor.putString("patientAddress", patientAddressField.getText().toString());
                }

                EditText patientPostCodeField = getActivity().findViewById(R.id.patient_post_code);
                if(!patientPostCodeField.getText().toString().isEmpty()) {
                    editor.putString("patientPostCode", patientPostCodeField.getText().toString());
                }

                EditText patientCityField = getActivity().findViewById(R.id.patient_city);
                if(!patientCityField.getText().toString().isEmpty()) {
                    editor.putString("patientCity", patientCityField.getText().toString());
                }

                EditText patientCountryField = getActivity().findViewById(R.id.patient_country);
                if(!patientCountryField.getText().toString().isEmpty()) {
                    editor.putString("patientCountry", patientCountryField.getText().toString());
                }

                EditText patientReaderSerialField = getActivity().findViewById(R.id.patient_reader_serial);
                if(!patientReaderSerialField.getText().toString().isEmpty()) {
                    editor.putString("patientReaderSerial", patientReaderSerialField.getText().toString());
                }

                editor.apply();

                Toast.makeText(getActivity(), "Changes saved.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
