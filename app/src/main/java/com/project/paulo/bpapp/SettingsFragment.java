package com.project.paulo.bpapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingsFragment extends Fragment {
    Button login;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        login = v.findViewById(R.id.button_login);
        login.setOnClickListener(loginListener);
        return v;
    }

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageView imgView = (ImageView)(getActivity().findViewById(R.id.imageView_profile));
            TextView nameView = getActivity().findViewById(R.id.navigation_name);
            TextView emailView = getActivity().findViewById(R.id.navigation_email);
            imgView.setVisibility(View.VISIBLE);

            EditText emailText = view.getRootView().findViewById(R.id.et_email);
            EditText passwordText = view.getRootView().findViewById(R.id.et_password);

            //Perform database check for email/password combination here and if successful, load in data to navigation menu

            emailView.setText(emailText.getText());
            emailView.setVisibility(View.VISIBLE);
        }
    };
}
