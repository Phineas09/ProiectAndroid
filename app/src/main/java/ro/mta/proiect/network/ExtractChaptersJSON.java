package ro.mta.proiect.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.mta.proiect.database.Chapter;

public class ExtractChaptersJSON extends AsyncTask<URL, Void, String> {

    public List<Chapter> chaptersListExtracted = new ArrayList<>();
    public int jsonVersion;

    JSONArray chaptersJSON = null;

    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            InputStream ist = conn.getInputStream();

            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader br = new BufferedReader(isr);
            String linie = null;
            String buffer = "";
            while((linie = br.readLine())!=null)
                buffer+=linie;
            parseJSON(buffer);

            return buffer;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void parseJSON(String jsonStr) {
        if (jsonStr != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
                chaptersJSON = jsonObject.getJSONArray("chapters");
                jsonVersion = jsonObject.getInt("version");

                JSONObject currentObjectJSON = null;
                for(int i = 0; i< chaptersJSON.length(); i++) {
                    currentObjectJSON = chaptersJSON.getJSONObject(i);

                    chaptersListExtracted.add(new Chapter(i+1, jsonVersion, currentObjectJSON.getString("chapterName"),
                            currentObjectJSON.getString("chapterImage"),
                            currentObjectJSON.getString("chapterContent")
                            ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("error","Invalid object given!");
        }

    }

}
