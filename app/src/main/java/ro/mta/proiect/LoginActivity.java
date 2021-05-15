package ro.mta.proiect;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import ro.mta.proiect.tables.Users;
import ro.mta.proiect.ui.fragments.LoginFragment;
import ro.mta.proiect.ui.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference firebaseReference;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BottomNavigationView bottomNavigationView = findViewById(R.id.loginRegisterBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //Set base container to be login
        getSupportFragmentManager().beginTransaction().replace(R.id.loginRegisterFrameContainer, new LoginFragment()).commit();

        firebaseReference = FirebaseDatabase.getInstance().getReference();
        Users.onStart();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_login:
                    fragment = new LoginFragment();
                    break;
                case R.id.navigation_register:
                    fragment = new RegisterFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.loginRegisterFrameContainer, fragment).commit();
            return true;
        }
    };

    /**
     * Event handlers
     */

    public void onLoginButtonClick(View view) throws NoSuchAlgorithmException {
        //Toast.makeText(MainActivity.this, "Clicked the login Button", Toast.LENGTH_LONG).show();
        try {
            EditText email = findViewById(R.id.userEmail);
            EditText password = findViewById(R.id.userPassword);

            if (GlobalFunctions.isEmailValid(email.getText().toString())) {
                //Perform password checking
                String passwordDigest = GlobalFunctions.getStringDigest(password.getText().toString());
                //Try to log in
                HashMap<String, Users> allUsers =  Users.getAllUsers();

                for (Map.Entry<String, Users> entry : allUsers.entrySet()) {
                    Users currentUser = entry.getValue();
                    if(currentUser.getEmail().equals(email.getText().toString()) && passwordDigest.equals(currentUser.getPassword())) {
                        //Make login and things
                        Snackbar snackbar = Snackbar
                                .make(getWindow().getDecorView().findViewById(android.R.id.content), "Logged in successfully!", Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.setActionTextColor(Color.GREEN);

                        ((EditText)findViewById(R.id.userPassword)).setText("");
                        ((EditText)findViewById(R.id.userEmail)).setText("");
                        snackbar.show();

                        //Change activity ?

                        //Always keep logged in

                        SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userEmail", currentUser.getEmail());
                        editor.putString("userPassword", currentUser.getPassword());
                        editor.apply();

                        //And destroy the current one!!@#!
                        Intent intent = new Intent(this, MainActivity.class);// New activity
                        intent.putExtra(GlobalFunctions.CURRENT_USER, currentUser);
                        intent.putExtra(GlobalFunctions.USER_ID, entry.getKey());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        return;
                    }
                }
                throw new LoginException("Invalid credentials!");

                //Toast.makeText(MainActivity.this, ), Toast.LENGTH_LONG).show();
            } else {
                throw new LoginException("Please enter a valid email address!");
            }
        } catch (LoginException exception) {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(android.R.id.content), exception.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Focus email on error
                            findViewById(R.id.userEmail).requestFocus();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            ((EditText)findViewById(R.id.userPassword)).setText("");
            snackbar.show();
        }

    }


    /**
     * Event listner for the TextView click
     * Action forgotPassword handling
     */
    public void onForgotPasswordTextViewClick(View view) {
        Toast.makeText(this, "THIS", Toast.LENGTH_SHORT).show();
    }

    public void onRegisterButtonClick(View view) throws NoSuchAlgorithmException {
        //Toast.makeText(MainActivity.this, "Clicked the login Button", Toast.LENGTH_LONG).show();
        try {
            EditText email = findViewById(R.id.userEmailRegister);
            EditText userName = findViewById(R.id.userNameRegister);
            EditText password = findViewById(R.id.userPasswordRegister);
            EditText passwordVerify = findViewById(R.id.userPasswordRegisterConfirm);

            // find the radiobutton by returned id
            String gender = null;
            try {
                gender = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.registerRadioGroup)).getCheckedRadioButtonId())).getText().toString();
            } catch (Exception exception) {
                throw new LoginException("Please select a gender!");
            }

            if (GlobalFunctions.isEmailValid(email.getText().toString())) {
                //Perform password checking

                //Password must be x len

                String passwordDigest = GlobalFunctions.getStringDigest(password.getText().toString());
                String passwordDigestVerify = GlobalFunctions.getStringDigest(passwordVerify.getText().toString());

                if (passwordDigest.equals(passwordDigestVerify)) {
                    //Try to log in
                    //Check unique email and username

                    HashMap<String, Users> allUsers =  Users.getAllUsers();

                    for (Map.Entry<String, Users> entry : allUsers.entrySet()) {
                        Users currentUser = entry.getValue();
                        if(currentUser.getEmail().equals(email.getText().toString())) {
                            throw new LoginException("This email is already used!");
                        }

                        if(currentUser.getUsername().equals(userName.getText().toString())) {
                            throw new LoginException("This username is already taken!");
                        }
                    }

                    //Make registration
                    Users newUser = new Users(userName.getText().toString(), passwordDigest, email.getText().toString(), gender);
                    newUser.insert();
                    // Do logic and add do database

                    Snackbar snackbar = Snackbar
                            .make(getWindow().getDecorView().findViewById(android.R.id.content), "Your account has been created!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Focus email on error
                                }
                            });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                    email.setText("");
                    userName.setText("");
                    password.setText("");
                    passwordVerify.setText("");
                    ((RadioGroup)findViewById(R.id.registerRadioGroup)).clearCheck();
                    //Toast.makeText(MainActivity.this, ), Toast.LENGTH_LONG).show();
                } else {
                    throw new LoginException("Passwords do not match!");
                }


            } else {
                throw new LoginException("Please enter a valid email address!");
            }
        } catch (LoginException exception) {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(android.R.id.content), exception.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Focus email on error
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            ((EditText)findViewById(R.id.userPasswordRegister)).setText("");
            ((EditText)findViewById(R.id.userPasswordRegisterConfirm)).setText("");
            snackbar.show();
        }
    }

}
