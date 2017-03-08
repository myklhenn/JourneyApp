package edu.wwu.avilatstudents.journey;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText usernameET, emailET, passwordET, passwordConfirmationET;
    TextInputLayout usernameTIL, passwordConfirmationTIL;
    TextView orTV, loginErrorTV;
    ViewGroup editContainer;
    enum OrStatus {OR_SIGN_UP, OR_LOGIN}

    OrStatus orStatus;
    Button loginBtn;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_login);

        editContainer = (ViewGroup) findViewById(R.id.edit_container);
        usernameTIL = (TextInputLayout) findViewById(R.id.username_layout);
        usernameET = (EditText) findViewById(R.id.username);
        emailET = (EditText) findViewById(R.id.email);
        passwordET = (EditText) findViewById(R.id.password);
        passwordConfirmationTIL = (TextInputLayout) findViewById(R.id.password_confirmation_layout);
        passwordConfirmationET = (EditText) findViewById(R.id.password_confirmation);
        loginBtn = (Button) findViewById(R.id.login);
        orTV = (TextView) findViewById(R.id.or);
        loginErrorTV = (TextView) findViewById(R.id.login_error_msg);
        orStatus = OrStatus.OR_SIGN_UP;

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    String dbResponse;
                    JSONObject jsonResponse;
                    DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                    String url = (orStatus == OrStatus.OR_SIGN_UP ? "http://murmuring-taiga-37698.herokuapp.com/api/v1/sessions" : "http://murmuring-taiga-37698.herokuapp.com/api/v1/registrations");
                    if (orStatus == OrStatus.OR_SIGN_UP){
                        dbResponse = databaseManager.login(
                                emailET.getText().toString(),
                                passwordET.getText().toString());
                    } else {
                        dbResponse = databaseManager.signUp(
                                usernameET.getText().toString(),
                                emailET.getText().toString(),
                                passwordET.getText().toString(),
                                passwordConfirmationET.getText().toString());

                    }
                    jsonResponse = new JSONObject(dbResponse);
                    if (jsonResponse.has("error") || (jsonResponse.has("success") && !jsonResponse.getBoolean("success"))) {
                        loginErrorTV.setText(getErrorResponse(jsonResponse));
                        loginErrorTV.setVisibility(View.VISIBLE);
                    } else {
                        if (loginErrorTV.getVisibility() == View.VISIBLE) {
                            loginErrorTV.setVisibility(View.GONE);
                        }
                        finish();
                    }
                }catch(Exception e){
                    Log.e("loginBtn", "" + e);
                }
            }
        });

        orTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orStatus == OrStatus.OR_SIGN_UP) {
                    createSignUpPage();
                }
                else {
                    createLoginPage();
                }
            }
        });

        emailET.clearFocus();
    }

    private CharSequence getErrorResponse(JSONObject response) {
        CharSequence error = null;
        try {
            if (response.has("error")) {
                error = response.getString("error");
            } else {
                error = response.getString("info");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }

    private void createSignUpPage(){
        TransitionManager.beginDelayedTransition(editContainer);
        usernameTIL.setVisibility(View.VISIBLE);
        passwordConfirmationTIL.setVisibility(View.VISIBLE);
        loginBtn.setText("Sign up");
        orTV.setText("or Login");
        orStatus = OrStatus.OR_LOGIN;
    }

    private void createLoginPage(){
        TransitionManager.beginDelayedTransition(editContainer);
        usernameTIL.setVisibility(View.GONE);
        passwordConfirmationTIL.setVisibility(View.GONE);
        loginBtn.setText("Login");
        orTV.setText("or Sign up");
        orStatus = OrStatus.OR_SIGN_UP;
    }

    @Override
    public void onBackPressed() {
        Log.d("login", "pressed back");
        moveTaskToBack(true);
    }
}
