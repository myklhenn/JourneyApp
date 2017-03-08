package edu.wwu.avilatstudents.journey;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avila_000 on 3/2/2017.
 */

public class DatabaseManager {
    Context context;
    private StringBuilder dbResponse;
    private static final String API_VERSION = "v1";

    public static final String SIGN_UP_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/registrations/";
    public static final String SIGN_IN_OR_OUT_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/sessions/";
    public static final String CREATE_JOURNEY_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/create/";
    public static final String UPDATE_JOURNEY_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/update/";
    public static final String ADD_TRAVELER_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/add_traveler/";
    public static final String ADD_STEPS_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/steps/update_steps/";

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
            Log.e("database", "Error creating JSONObject: " + e);
        }

        new DownloadData().execute(url, "POST", jsonObjectUser.toString(), "login");
        String dbResponseToReturn = dbResponse.toString();
        dbResponse.delete(0, dbResponse.length());
        return dbResponseToReturn;
    }

    public String signUp(String username, String email, String password, String passwordConfirmation){
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
            Log.e("database", "Error creating JSONObject: " + e);
        }

        new DownloadData().execute(SIGN_UP_URI, "POST", jsonObjectUser.toString(), "signUp");
        String dbResponseToReturn = dbResponse.toString();
        dbResponse.delete(0, dbResponse.length());
        return dbResponseToReturn;
    }

    public String signOut(String email, String auth_token) {
        new DownloadData().execute(SIGN_IN_OR_OUT_URI, "", "signOut", email, auth_token, "DELETE");
        String dbResponseToReturn = dbResponse.toString();
        dbResponse.delete(0, dbResponse.length());
        return dbResponseToReturn;
    }

    private void updateSession(String outputData, String inputData){
        SessionManager sessionManager = new SessionManager(context);
        JSONObject input = null;
        JSONObject output = null;
        try {
            output = new JSONObject(outputData).getJSONObject("user");
            input = new JSONObject(inputData).getJSONObject("data");
            Log.d("database", "input: " + input.toString());
            sessionManager.saveUsername((output.has("username") ? output.getString("username") : input.getString("username")));
            Log.d("database", "Username: " + sessionManager.getUsername());
            sessionManager.saveAuthentication(input.getString("authentication_token"));
            Log.d("database", "Authentication: " + sessionManager.getAuthentication());
            sessionManager.saveEmail(output.getString("email"));
            Log.d("database", "Email: " + sessionManager.getEmail());
        }catch(JSONException e){
            Log.e("database", "Error converting server output to JSON: " + e);
        }
    }

    public void updateSteps(String email, String authentication, String steps){
        JSONObject jsonObjectInfo = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("steps", steps);
        }catch (JSONException e){
            Log.e("database", "Error creating JSONObject: " + e);
        }

        Log.d("database", "JSON: " + jsonObjectInfo.toString() + "\tEmail: " + email + "\tSteps: " + steps + "\tAuth: " + authentication);
        new DownloadData().execute(ADD_STEPS_URI, "PATCH", jsonObjectInfo.toString(), "updateSteps", email, authentication);
    }

    public String createJourney(String email, String authentication, String journeyTitle){
        JSONObject jsonObjectJourney = null;
        JSONObject jsonObjectInfo = null;
        JSONObject jsonObjectInput = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("title", journeyTitle);

            jsonObjectJourney = new JSONObject();
            jsonObjectJourney.put("journey", jsonObjectInfo);
        }catch (JSONException e){
            Log.e("database", "Error creating JSONObject: " + e);
        }

        try {
            String input = new DownloadData().execute(CREATE_JOURNEY_URI, "POST", jsonObjectJourney.toString(), "createJourney", email, authentication).get();
            jsonObjectInput = new JSONObject(input).getJSONObject("data").getJSONObject("journey");
            return jsonObjectInput.getString("id");

        }catch(Exception e){
            Log.e("database", "Error with DownloadData().execute: " + e);
        }
        return null;
    }

    public void updateJourney(String email, String authentication, String journeyID,
                              String title, String startLat, String startLong,
                              String endLat, String endLong, String startLoc,
                              String endLoc, String totalSteps){
        JSONObject jsonObjectJourney = null;
        JSONObject jsonObjectInfo = null;

        try{
            jsonObjectInfo = new JSONObject();
            if(!title.isEmpty())
                jsonObjectInfo.put("title", title);
            if(!startLat.isEmpty())
                jsonObjectInfo.put("start_latitude", startLat);
            if(!startLong.isEmpty())
                jsonObjectInfo.put("start_longitude", startLong);
            if(!endLat.isEmpty())
                jsonObjectInfo.put("end_latitude", endLat);
            if(!endLong.isEmpty())
                jsonObjectInfo.put("end_longitude", endLong);
            if(!startLoc.isEmpty())
                jsonObjectInfo.put("start_location", startLoc);
            if(!endLoc.isEmpty())
                jsonObjectInfo.put("end_location", endLoc);
            if(!totalSteps.isEmpty())
                jsonObjectInfo.put("total_steps_required", totalSteps);

            jsonObjectJourney = new JSONObject();
            jsonObjectJourney.put("journey", jsonObjectInfo);

            new DownloadData().execute(UPDATE_JOURNEY_URI, "PATCH", jsonObjectJourney.toString(), "updateJourney", email, authentication, journeyID);
        }catch(Exception e){
            Log.e("database", "Error with DownloadData().execute: " + e);
        }
    }

    public void addTravelerToJourney(String email, String authentication, String journeyID){
        JSONObject jsonObjectInfo = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("email", email);
        }catch (JSONException e){
            Log.e("database", "Error creating JSONObject: " + e);
        }

        new DownloadData().execute(ADD_TRAVELER_URI, "POST", jsonObjectInfo.toString(), "addTravelerToJourney", email, authentication, journeyID);
    }



    //PARAMETERS: (url, requestMethod, output, method, email, authentication, journeyID)
    private class DownloadData extends AsyncTask<String, Void, String> {
        HttpURLConnection connection = null;
        String inputData = "";

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url = new URL(strings[0]);
                String requestMethod = strings[1];
                String outputData = strings[2];
                String method = strings[3];
                connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(true);
                connection.setDoOutput(true);
                connection.setRequestMethod(requestMethod);
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "curl/7.47.0");

                if(!(method.equals("signUp")) && !(method.equals("login"))){
                    String email = strings[4];
                    String authentication = strings[5];
                    connection.setRequestProperty("X-User-Email", email);
                    connection.setRequestProperty("X-User-Token", authentication);
                }
                if(method.equals("addTravelerToJourney") || method.equals("updateJourney")){
                    String journeyID = strings[6];
                    connection.setRequestProperty("X-Journey-Id", journeyID);
                }

                connection.connect();
                sendOutput(outputData);
                receiveInput();

                if((method.equals("signUp")) || (method.equals("login"))) {
                    updateSession(outputData, inputData);
                }

            } catch(Exception e){
                Log.e("database", "Connection fail: " + e);
            } finally{
                if(connection != null) connection.disconnect();
                Log.d("database", "Connection disconnected");
            }
            return inputData;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("database", "Input: " + s);
            SessionManager sessionManager = new SessionManager(context);
            Toast.makeText(context, "Hello " + sessionManager.getUsername() + "!", Toast.LENGTH_LONG).show();
            Toast.makeText(context, "Your email is " + sessionManager.getEmail() + "!", Toast.LENGTH_LONG).show();
            Toast.makeText(context, "Authentication is " + sessionManager.getAuthentication() + "!", Toast.LENGTH_LONG).show();
        }


        private void sendOutput(String message) {
            try {
                Writer output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                output.write(message);
                Log.d("database", "Output: " + message);
                output.close();
            }catch (IOException e){
                Log.e("database", "Output fail: " + e);
            }
            Log.d("database", "Output success");
        }


        private void receiveInput(){
            InputStream input = null;

            try {
                input = connection.getInputStream();
            }catch(Exception e){
                Log.e("database", "Input fail " + e);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(input);
            int inputStreamData;

            try {
                while ((inputStreamData = inputStreamReader.read()) != -1) {
                    inputData += (char) inputStreamData;
/*                    Log.d("database", "Still reading from input stream");
                    Log.d("database", "inputStreamData: " + inputStreamData);*/
                }
            }catch(IOException e){
                Log.e("database", "Input fail: " + e);
            }
            Log.d("database", "Input success");
        }
    }
}
