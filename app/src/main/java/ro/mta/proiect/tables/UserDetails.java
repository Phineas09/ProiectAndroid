package ro.mta.proiect.tables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ro.mta.proiect.GlobalFunctions;
import ro.mta.proiect.MainActivity;

public class UserDetails {

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserDetails");


    String userId;
    String phoneNumber;
    Map<String, Integer> readChapters;
    static final HashMap<String, UserDetails> allUsersDetails = new HashMap<>();

    public UserDetails(String userId) {
        this.userId = userId;
        phoneNumber = "";
        readChapters = new HashMap<>();

        // Add chapters here too
        readChapters.put("Bluetooth", 0);
        readChapters.put("Zedboard", 1);
        readChapters.put("Test123", 1);
    }

    public UserDetails() {
    }



    public static void onStart(String userID, Users currentUser) {
        //Make new instance

        //Add it to database if not exists
        //We have the id so get it

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Get all user details from database for stats
                 */

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserDetails users = ds.getValue(UserDetails.class);
                    if(users != null)
                        allUsersDetails.putIfAbsent(users.getUserId(), users);
                }

                UserDetails currentUserDetails = dataSnapshot.child(userID).getValue(UserDetails.class);
                if (currentUserDetails != null) {
                    //If it is not null then put ti somewhere
                    MainActivity.setCurrentUserDetails(currentUserDetails);
                } else {
                    UserDetails userDetails = new UserDetails(userID);
                    MainActivity.setCurrentUserDetails(userDetails);
                    databaseReference.child(userID).setValue(userDetails);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setReadChapters(Map<String, Integer> readChapters) {
        this.readChapters = readChapters;
    }

    public Map<String, Integer> getReadChapters() {
        return readChapters;
    }

    public void updateDetails() {
        databaseReference.child(this.userId).setValue(this);
    }

    public void addReadChapter(String chapter) {
        readChapters.put(chapter, 1);
        updateDetails();
    }

}
