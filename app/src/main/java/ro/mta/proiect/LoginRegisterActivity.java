package ro.mta.proiect;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.security.auth.login.LoginException;

import ro.mta.proiect.ui.main.SectionsPagerAdapter;

public class LoginRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

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
        Toast.makeText(LoginRegisterActivity.this, "THIS", Toast.LENGTH_SHORT).show();
    }



}