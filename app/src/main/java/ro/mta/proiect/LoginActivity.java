package ro.mta.proiect;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.security.NoSuchAlgorithmException;

import javax.security.auth.login.LoginException;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




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
        Toast.makeText(LoginActivity.this, "THIS", Toast.LENGTH_SHORT).show();
    }

}