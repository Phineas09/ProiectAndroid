package ro.mta.proiect;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ro.mta.proiect.ui.fragments.HomeFragment;
import ro.mta.proiect.ui.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mainDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.applicationName);
        setSupportActionBar(toolbar);
        mainDrawer = findViewById(R.id.drawer_layout);

        //Modify here for more options in the menu
        NavigationView navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggleActionBar = new ActionBarDrawerToggle(this, mainDrawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawer.addDrawerListener(toggleActionBar);
        toggleActionBar.syncState();


        //If we rotate the device
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

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
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_log_out:
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share toast", Toast.LENGTH_LONG).show();
                break;
        }

        mainDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
