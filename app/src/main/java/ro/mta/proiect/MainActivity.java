package ro.mta.proiect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.SettingsClickListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;

import ro.mta.proiect.tables.UserDetails;
import ro.mta.proiect.tables.Users;
import ro.mta.proiect.ui.fragments.AdminFragment;
import ro.mta.proiect.ui.fragments.ChapterFragment;
import ro.mta.proiect.ui.fragments.GoogleMaps;
import ro.mta.proiect.ui.fragments.HomeFragment;
import ro.mta.proiect.ui.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mainDrawer;
    private ActionBarDrawerToggle toggleActionBar;
    private View.OnClickListener originalListener;
    private Users currentUser;
    private String currentUserID;
    private Toolbar toolbar;
    static private UserDetails currentUserDetails = null;
    static private boolean isPermissionGranted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.applicationName);
        setSupportActionBar(toolbar);
        mainDrawer = findViewById(R.id.drawer_layout);

        //Modify here for more options in the menu
        NavigationView navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Check for the user given by the other activity

        if (getIntent().hasExtra(GlobalFunctions.CURRENT_USER)) {
            currentUser = (Users) getIntent().getSerializableExtra(GlobalFunctions.CURRENT_USER);
            currentUserID = (String) getIntent().getSerializableExtra(GlobalFunctions.USER_ID);
            UserDetails.onStart(currentUserID, currentUser);

            //Set navigation head

            View headerView = navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(currentUser.getUsername());
            ((TextView) headerView.findViewById(R.id.nav_header_user_email)).setText(currentUser.getEmail());
            if (!currentUser.isAdmin())
                navigationView.getMenu().findItem(R.id.nav_admin).setVisible(false);

        }

        //If we rotate the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        if (toolbar != null) {
            toggleActionBar = new ActionBarDrawerToggle(
                    this, mainDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggleActionBar.syncState();
            mainDrawer.addDrawerListener(toggleActionBar);

            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                                toolbar.setTitle(R.string.applicationName);
                            }
                        });
                    } else {
                        //show hamburger
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggleActionBar.syncState();
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mainDrawer.openDrawer(GravityCompat.START);
                            }
                        });
                    }
                }
            });
        }

        checkMyPermission();
    }

    private void checkMyPermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "Location permission granted!", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Focus email on error
                            }
                        });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                isPermissionGranted = false;
                Snackbar snackbar = Snackbar
                        .make(getWindow().getDecorView().findViewById(android.R.id.content), "You will not be able to see your location", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Focus email on error
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                isPermissionGranted = false;
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    public static boolean isIsPermissionGranted() {
        return isPermissionGranted;
    }

    public static void setCurrentUserDetails(UserDetails currentUserDetails) {
        MainActivity.currentUserDetails = currentUserDetails;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /**
         * Modify here for more fragments
         */
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new ProfileFragment(currentUser, currentUserID, currentUserDetails)).commit();
                break;
            case R.id.nav_log_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CONFIRM
                                /**
                                 * Delete preferences and restart the app
                                 */
                                SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                if (preferences.contains("userEmail") && preferences.contains("userPassword")) {
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear().apply();
                                }
                                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);// New activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // CANCEL
                            }
                        });
                TextView myMsg = new TextView(this);
                builder.setCancelable(true);
                myMsg.setText("\nAre you sure that you want to log out?");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(myMsg);
                // Create the AlertDialog object and return it
                builder.create().show();

                break;
            case R.id.nav_admin:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.main_fragment_container,
                                new AdminFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
        }

        mainDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onMainFrameCardClick(View view) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right,
                            R.anim.exit_left_to_right)
                    .replace(R.id.main_fragment_container,
                            new ChapterFragment(view.getTag().toString()))
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } catch (Exception exception) {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().findViewById(android.R.id.content), "This chapter is currently not available!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Focus email on error
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Profile fragment event lisners!
     *
     * @param view
     */

    public void onShowMyLocationButtonClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right,
                        R.anim.exit_left_to_right)
                .replace(R.id.main_fragment_container,
                        new GoogleMaps())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void onEditPersonalInfoClick(View view) {
        EditText userName = (EditText) findViewById(R.id.fragment_profile_user_username);
        EditText phoneNumber = (EditText) findViewById(R.id.fragment_profile_user_phone_number);
        EditText editText = new EditText(this);
        userName.setEnabled(true);
        userName.setBackground(editText.getBackground());
        phoneNumber.setEnabled(true);
        phoneNumber.setBackground(editText.getBackground());
        changeIntoNewButtons();
    }

    public void resetFields() {
        ((EditText) findViewById(R.id.fragment_profile_user_username)).setText(currentUser.getUsername());
        if (currentUserDetails.getPhoneNumber().equals("")) {
            ((TextView) findViewById(R.id.fragment_profile_user_phone_number)).setText("Unset");
        } else {
            ((TextView) findViewById(R.id.fragment_profile_user_phone_number)).setText(currentUserDetails.getPhoneNumber());
        }
        EditText userName = (EditText) findViewById(R.id.fragment_profile_user_username);
        EditText phoneNumber = (EditText) findViewById(R.id.fragment_profile_user_phone_number);
        userName.setEnabled(false);
        userName.setBackgroundResource(android.R.color.transparent);
        phoneNumber.setEnabled(false);
        phoneNumber.setBackgroundResource(android.R.color.transparent);
        changeAfterCancelOrSave();
    }

    public void changeAfterCancelOrSave() {
        Button editPersonalInfo = (Button) findViewById(R.id.fragment_profile_edit_personal_info_button);
        editPersonalInfo.setText("Edit Personal Info");
        editPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditPersonalInfoClick(v);
            }
        });

        Button showMyLocationButton = (Button) findViewById(R.id.fragment_profile_show_my_location_button);
        showMyLocationButton.setText("Show My Location");
        showMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowMyLocationButtonClick(v);
            }
        });
    }

    public void changeIntoNewButtons() {
        Button editPersonalInfo = (Button) findViewById(R.id.fragment_profile_edit_personal_info_button);
        editPersonalInfo.setText("Cancel");
        editPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        Button showMyLocationButton = (Button) findViewById(R.id.fragment_profile_show_my_location_button);
        showMyLocationButton.setText("Save");
        showMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change structure and in database
                //Logic here
                EditText userName = (EditText) findViewById(R.id.fragment_profile_user_username);
                EditText phoneNumber = (EditText) findViewById(R.id.fragment_profile_user_phone_number);
                currentUserDetails.setPhoneNumber(phoneNumber.getText().toString());
                currentUserDetails.updateDetails();
                currentUser.setUsername(userName.getText().toString());
                currentUser.updateUser(currentUserDetails.getUserId());

                /**
                 * Activity gets reset anyway because of some firebase event not set
                 */
            }
        });
    }


}
