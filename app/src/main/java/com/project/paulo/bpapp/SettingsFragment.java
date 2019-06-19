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
        getActivity().setTitle("Settings");
        login = v.findViewById(R.id.button_login);
        login.setOnClickListener(loginListener);
        return v;
    }

    //On click listener for login button which updates UI
    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageView imgView = getActivity().findViewById(R.id.imageView_profile);
            TextView nameView = getActivity().findViewById(R.id.navigation_name);
            TextView emailView = getActivity().findViewById(R.id.navigation_email);

            EditText emailText = view.getRootView().findViewById(R.id.et_email);
            EditText passwordText = view.getRootView().findViewById(R.id.et_password);
            Button login = (Button)view;
            if(login.getText().toString().equals("Log In")) {

                //Perform database check for email/password combination here and if successful, load in data to navigation menu

                //Log in process
                emailView.setText(emailText.getText());
                emailView.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.INVISIBLE);
                passwordText.setVisibility(View.INVISIBLE);
                login.setText("Log Out");

            }else{
                //Log out process
                emailView.setVisibility(View.INVISIBLE);
                imgView.setVisibility(View.INVISIBLE);
                emailText.setVisibility(View.VISIBLE);
                passwordText.setVisibility(View.VISIBLE);
                login.setText("Log In");
            }
        }
    };
}
