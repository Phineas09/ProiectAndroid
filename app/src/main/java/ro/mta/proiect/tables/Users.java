package ro.mta.proiect.tables;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Users {

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private static long tableMaxId;
    static final HashMap<String, Users> allUsers = new HashMap<>();

    String username;
    String password;
    String email;
    String gender;

    public Users(String username, String password, String email, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }

    public Users() {
    }

    public Users(int id, String username, String password, String email, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }

    public static void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tableMaxId = (dataSnapshot.getChildrenCount());
                for(int i = 0; i<= tableMaxId; i++) {
                    Users users = dataSnapshot.child(Integer.toString(i)).getValue(Users.class);
                    if(users != null)
                        allUsers.putIfAbsent(Integer.toString(i), users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    tableMaxId = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static HashMap<String, Users> getAllUsers() {
        return allUsers;
    }

    public void insert() {
        long nextId = getTableMaxId();
        databaseReference.child(String.valueOf(nextId)).setValue(this);
    }

    public static long getTableMaxId() {
        return tableMaxId + 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
