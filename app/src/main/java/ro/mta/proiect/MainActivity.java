package ro.mta.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.security.NoSuchAlgorithmException;

import javax.security.auth.login.LoginException;

import ro.mta.proiect.ui.fragments.LoginFragment;
import ro.mta.proiect.ui.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.loginRegisterBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //Set base container to be login
        getSupportFragmentManager().beginTransaction().replace(R.id.loginRegisterFrameContainer, new LoginFragment()).commit();
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
     *
     */

    public void  onLoginButtonClick(View view) throws NoSuchAlgorithmException {
        //Toast.makeText(MainActivity.this, "Clicked the login Button", Toast.LENGTH_LONG).show();
        try {
            EditText email = findViewById(R.id.userEmail);
            EditText password = findViewById(R.id.userPassword);

            if(GlobalFunctions.isEmailValid(email.getText().toString())) {
                //Perform password checking
                String passwordDigest = GlobalFunctions.getStringDigest(password.getText().toString());
                //Try to log in



                //Toast.makeText(MainActivity.this, ), Toast.LENGTH_LONG).show();
            }
            else {
                throw new LoginException("Please enter a valid email address!");
            }
        }
        catch (LoginException exception) {
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
}