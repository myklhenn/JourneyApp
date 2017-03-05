package edu.wwu.avilatstudents.journey;

/**
 * Created by brendanbaalke on 2/23/17.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

class JSONPARSER {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    JSONPARSER() {
    }

    String getJSONFromUrl(String url) {

        // Making HTTP request
        try {

            URL urlnew = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlnew.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // shows log of json data
        //Log.d("JSON_RUTA", json);
        return json;

    }
}