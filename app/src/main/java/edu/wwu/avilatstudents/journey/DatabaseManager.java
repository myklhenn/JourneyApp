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
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
    public static final String JOURNEY_INFO_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/get_data/";
    public static final String USER_JOURNEYS_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/retrieve_user_journeys/";
    public static final String FINALIZE_TRAVELERS_URI = "https://murmuring-taiga-37698.herokuapp.com/api/" + API_VERSION + "/journeys/finalize_travelers";

    public DatabaseManager(Context context){
        this.dbResponse = new StringBuilder();
        this.context = context;
    }

    public String login(String email, String password){
        JSONObject jsonObjectUser = null;
        JSONObject jsonObjectInfo = null;
        String response = null;

        try{
            jsonObjectInfo = new JSONObject();
            jsonObjectInfo.put("email", email);
            jsonObjectInfo.put("password", password);

            jsonObjectUser = new JSONObject();
            jsonObjectUser.put("user", jsonObjectInfo);
        }catch (JSONException e){
            Log.e("database", "Error creating JSONObject: " + e);
        }

        try {
            response = new DownloadData().execute(SIGN_IN_OR_OUT_URI, "POST", jsonObjectUser.toString(), "login").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String signUp(String username, String email, String password, String passwordConfirmation){
        JSONObject jsonObjectUser = null;
        JSONObject jsonObjectInfo = null;
        String response = null;

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

        try {
            response = new DownloadData().execute(SIGN_UP_URI, "POST", jsonObjectUser.toString(), "signUp").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void signOut(String email, String auth_token) {
        new DownloadData().execute(SIGN_IN_OR_OUT_URI, "DELETE", "", "signUp", email, auth_token);
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
        new DownloadData().execute(ADD_STEPS_URI, "POST", jsonObjectInfo.toString(), "updateSteps", email, authentication);
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

    public JSONObject getJourneyInfo(String email, String authentication, String journeyID){
        try {
            String input = new DownloadData().execute(JOURNEY_INFO_URI, "GET", "", "getJourneyInfo", email, authentication, journeyID).get();
            return new JSONObject(input).getJSONObject("data").getJSONObject("journey");
        }catch (Exception e){
        Log.e("database", "Error retrieving journey info " + e);
        }
        return null;
    }

    public JSONObject getJourneys(String email, String authentication){
        try {
            String input = new DownloadData().execute(USER_JOURNEYS_URI, "GET", "", "getJourneys", email, authentication).get();
            return new JSONObject(input).getJSONObject("journeys");
        }catch (Exception e){
            Log.e("database", "Error retrieving journey info " + e);
        }
        return null;
    }

    public void finalizeTravelers(String email, String authentication, String journeyID){
            new DownloadData().execute(FINALIZE_TRAVELERS_URI, "PATCH", "", "finalizeTravelers", email, authentication, journeyID);
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
                if(method.equals("addTravelerToJourney") || method.equals("updateJourney") || method.equals("getJourneyInfo") || method.equals("finalizeTravelers")){
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
            Log.d("database", "Hello " + sessionManager.getUsername() + "!");
            Log.d("database", "Your email is " + sessionManager.getEmail() + "!");
            Log.d("database", "Authentication is " + sessionManager.getAuthentication() + "!");
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
            InputStreamReader inputStreamReader = null;
            int inputStreamData;
            boolean error = false;

            try {
                if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    input = connection.getInputStream();
                } else {
                    input = connection.getErrorStream();
                    error = true;
                }
                inputStreamReader = new InputStreamReader(input);
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e) {
                Log.e("database", "Input fail " + e);
            }

            try {
                while ((inputStreamData = inputStreamReader.read()) != -1) {
                    inputData += (char) inputStreamData;
                }
            } catch(IOException e){
                Log.e("database", "Input fail: " + e);
            }
            Log.d("database", "Input success");
        }
    }
}
