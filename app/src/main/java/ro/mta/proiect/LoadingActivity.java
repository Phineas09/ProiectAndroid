package ro.mta.proiect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ro.mta.proiect.database.Chapter;
import ro.mta.proiect.database.ChapterDB;
import ro.mta.proiect.network.ExtractChaptersJSON;
import ro.mta.proiect.tables.Users;

public class LoadingActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ProgressBar spinner = (ProgressBar)findViewById(R.id.loadingScreenProgressBar);

        spinner.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);

        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
        Users.onStart();


        /**
         * Fetch chapters from web
         */

        ExtractChaptersJSON extractChaptersJSON = new ExtractChaptersJSON(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {

                /**
                 * Check into the database vor version
                 * Insert
                 */

                ChapterDB chapterDB = ChapterDB.getInstance(getApplicationContext());
                List<Chapter> chapters = chapterDB.getChapterDao().getAllChapters();
                if(chapters.size() == 0) {
                    for(Chapter chapter : chaptersListExtracted) {
                        chapterDB.getChapterDao().insertChapter(chapter);
                    }
                }

                /**
                 * Update in room
                 */

                if(chapters.get(0).getVersion() != jsonVersion) {
                    for(Chapter chapter : chaptersListExtracted)
                        chapterDB.getChapterDao().updateChapter(chapter);
                }
            }
        };
        try {
            extractChaptersJSON.execute(new URL("https://pastebin.com/raw/9Gjev1Na"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
        if(preferences.contains("userEmail") && preferences.contains("userPassword")) {
            String userEmail = preferences.getString("userEmail", "");
            String userPassword = preferences.getString("userPassword", "");

            firebaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long tableMaxId = (dataSnapshot.getChildrenCount());
                    for (int i = 0; i <= tableMaxId; i++) {
                        Users currentUser = dataSnapshot.child(Integer.toString(i)).getValue(Users.class);
                        if (currentUser != null) {
                            if (currentUser.getEmail().equals(userEmail) && userPassword.equals(currentUser.getPassword())) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra(GlobalFunctions.CURRENT_USER, currentUser);
                                intent.putExtra(GlobalFunctions.USER_ID, String.valueOf(i));
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        } else {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);// New activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            });
                        }
                    },
                    1000
            );
        }
    }

}