package edu.wwu.avilatstudents.journey;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avila_000 on 3/2/2017.
 */

public class DatabaseManager {
    Context context;
    private StringBuilder dbResponse;


    public DatabaseManager(Context context){
        this.dbResponse = new StringBuilder();
        this.context = context;
    }


    public String login(String url, String email, String password){
        JSONObject jsonObjectUser = null;
        JSONObject jsonObjectInfo = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("email", email);
            jsonObjectInfo.put("password", password);

            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("user", jsonObjectInfo);
        }catch (JSONException e){
            Log.e("signUp", "Error creating JSONObject: " + e);
        }

        new DownloadData().execute(url, jsonObjectUser.toString());
        String dbResponseToReturn = dbResponse.toString();
        dbResponse.delete(0, dbResponse.length());
        return dbResponseToReturn;
    }


    public String signUp(String url, String username, String email, String password, String passwordConfirmation) throws UnsupportedEncodingException{
        JSONObject jsonObjectUser = null;
        JSONObject jsonObjectInfo = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("username", username);
            jsonObjectInfo.put("email", email);
            jsonObjectInfo.put("password", password);
            jsonObjectInfo.put("password_confirmation", passwordConfirmation);

            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("user", jsonObjectInfo);
        }catch (JSONException e){
            Log.e("signUp", "Error creating JSONObject: " + e);
        }

        new DownloadData().execute(url, jsonObjectUser.toString(), username, email);
        String dbResponseToReturn = dbResponse.toString();
        dbResponse.delete(0, dbResponse.length());
        return dbResponseToReturn;
    }


    private void signUpHelper(String data, String username){
        SessionManager sessionManager = new SessionManager(context);
        JSONObject input = null;
        try {
            input = new JSONObject(data).getJSONObject("data");
            Log.d("signUpHelper", input.toString());
            sessionManager.createSession(username);
            Log.d("signUpHelper", username);
            sessionManager.saveAuthentication(input.getString("authentication_token"));
        }catch(JSONException e){
            Log.e("signUpHelper", "Error converting server output to JSON: " + e);
        }
        //Toast.makeText(context, "Hello " + sessionManager.getUsername() + "!" + "\nYour authentication token is " + sessionManager.getAuthentication(), Toast.LENGTH_LONG).show();
    }

    //PARAMS: (String url, String jsonData, String username, String email)
    private class DownloadData extends AsyncTask<String, Void, String> {
        String data = "";
        HttpURLConnection connection = null;

        @Override
        protected String doInBackground(String... strings) {

            InputStream inputStream = null;
            String request = "";

            try{
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "curl/7.47.0");
                connection.connect();
                Log.d("signUp", connection.getRequestMethod());

                sendOutput(strings[1]);
                receiveInput();

                signUpHelper(data, strings[2]);

            }catch(Exception e){
                Log.e("signUp", "Error connecting to server: " + e);
            }finally{
                connection.disconnect();
                Log.d("signUp", "Connection disconnected");
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("signUp", "Input: " + s);
        }


        private void sendOutput(String message) {
            try {
                Writer output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                output.write(message);
                Log.d("signUp", "Output: " + message);
                output.close();
            }catch (IOException e){
                Log.e("sendOutput", "" + e);
            }
        }


        private void receiveInput(){
            InputStream input = null;

            try {
                input = connection.getInputStream();
            }catch(Exception e){
                Log.e("signUp", "Error getting input stream: " + e);
            }
            Log.d("signUp", "hello");

            InputStreamReader inputStreamReader = new InputStreamReader(input);
            int inputStreamData;

            try {
                while ((inputStreamData = inputStreamReader.read()) != -1) {
                    data += (char) inputStreamData;
                    Log.d("signUp", "Still reading from input stream");
                    Log.d("signUp", "inputStreamData: " + inputStreamData);
                }
            }catch(IOException e){
                Log.e("receiveInput", "" + e);
            }
            Log.d("signUp", "Finished reading from input stream");
        }
    }
}
