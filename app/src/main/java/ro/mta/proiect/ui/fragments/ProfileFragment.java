package ro.mta.proiect.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ro.mta.proiect.R;
import ro.mta.proiect.tables.UserDetails;
import ro.mta.proiect.tables.Users;

public class ProfileFragment extends Fragment {

    private Users currentUser;
    private String currentUserID;
    private UserDetails currentUserDetails = null;
    private boolean adminPage;

    public ProfileFragment(Users currentUser, String currentUserID, UserDetails currentUserDetails, boolean adminPage) {
        this.currentUser = currentUser;
        this.currentUserID = currentUserID;
        this.currentUserDetails = currentUserDetails;
        this.adminPage = adminPage;
    }

    public ProfileFragment(Users currentUser, String currentUserID, UserDetails currentUserDetails) {
        this.currentUser = currentUser;
        this.currentUserID = currentUserID;
        this.currentUserDetails = currentUserDetails;
        this.adminPage = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Here we set everything
        ((EditText) getView().findViewById(R.id.fragment_profile_user_username)).setText(currentUser.getUsername());
        ((TextView) getView().findViewById(R.id.fragment_profile_user_email)).setText(currentUser.getEmail());
        if(currentUserDetails == null || currentUserDetails.getPhoneNumber().equals("")) {
            ((TextView) getView().findViewById(R.id.fragment_profile_user_phone_number)).setText("Unset");
        } else {
            ((TextView) getView().findViewById(R.id.fragment_profile_user_phone_number)).setText(currentUserDetails.getPhoneNumber());
        }

        ((TextView) getView().findViewById(R.id.fragment_profile_user_gender)).setText(currentUser.getGender());
        //TextView textView = (TextView) getView().findViewById(R.id.testUserEmail);
        //textView.setText(currentUser.getEmail());

        if(adminPage) {
            ((Button)getView().findViewById(R.id.fragment_profile_edit_personal_info_button)).setVisibility(View.GONE);
            ((Button)getView().findViewById(R.id.fragment_profile_show_my_location_button)).setVisibility(View.GONE);
        }
        //Assign listners here too i think

    }

}

