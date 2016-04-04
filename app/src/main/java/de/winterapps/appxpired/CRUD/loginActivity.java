package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;
import de.winterapps.appxpired.menuActivity;

/**
 * Created by D062332 on 18.10.2015.
 */
public class loginActivity extends Activity{

    Button buttonLogin;
    Button buttonRegister;
    final Activity self = this;
    responseClass stringRequest;
    CheckBox checkKeepLog;
    SharedPreferences prefs;
    String userInput;
    String passInput;
    Boolean keepLoggedIn;

    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if token is saved in sharedpreferences
        prefs = self.getSharedPreferences(
                "de.winterapps.appxpired", Context.MODE_PRIVATE);

        String prefToken = prefs.getString("Token", "");
        String prefUser = prefs.getString("Username", "");
        //send request and check whether token is still usable
        if (prefToken != "" && prefUser != "") {
            Boolean isSuccessful = backendRequest(prefUser, "", true);
            if (isSuccessful){
                this.setContentView(R.layout.activity_menu);
                Toast.makeText(loginActivity.this, "Hello "+userInput, Toast.LENGTH_SHORT).show();
            } else{
                this.setContentView(R.layout.activity_login);
                Toast.makeText(loginActivity.this, "Welcome back! "+prefUser, Toast.LENGTH_SHORT).show();
                buttonLogin  = (Button) findViewById(R.id.loginLoginButton);
                buttonRegister = (Button) findViewById(R.id.loginRegisterButton);
                checkKeepLog = (CheckBox) findViewById(R.id.loginCheckbox);

                buttonLogin.setOnClickListener(loginHandler);
                buttonRegister.setOnClickListener(registerHandler);
            }
        } else {
            this.setContentView(R.layout.activity_login);
            buttonLogin  = (Button) findViewById(R.id.loginLoginButton);
            buttonRegister = (Button) findViewById(R.id.loginRegisterButton);
            checkKeepLog = (CheckBox) findViewById(R.id.loginCheckbox);

            buttonLogin.setOnClickListener(loginHandler);
            buttonRegister.setOnClickListener(registerHandler);
        }

    }


    View.OnClickListener loginHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final EditText userEdit = (EditText) findViewById(R.id.loginUserEdit);
            final EditText passEdit = (EditText) findViewById(R.id.loginPassEdit);
            userInput = userEdit.getText().toString();
            passInput = passEdit.getText().toString();
            keepLoggedIn = checkKeepLog.isChecked();
            final String generatedToken;

            backendRequest(userInput, passInput, false);
            Log.d("vor dem", "request");
            backendRequestFetchData(userInput, passInput, false);

        }

    };

    View.OnClickListener registerHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Intent intentReg = new Intent(loginActivity.this, registerActivity.class);
            startActivity(intentReg);
        }
    };

    public boolean backendRequest(final String userInput, final String passInput, final Boolean token){

        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        String url ="http://www.appxpired.winterapps.de/api/userManagement.php";
        final Boolean[] RequestResponse = {false};

        if(keepLoggedIn != null){
            if (keepLoggedIn == false){
                prefs.edit().remove("Username").commit();
                prefs.edit().remove("Token").commit();
            }
        }

        // Request a string response from the provided URL.
        stringRequest = new responseClass(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String,String> headers = stringRequest.headers;
                        String[] credentials = new String[3];
                        credentials[0] = headers.get("Success");
                        credentials[1] = headers.get("Token");
                        JSONArray jsonResponse = null;
                        try {
                            jsonResponse = new JSONArray(response);
                            credentials[2] = jsonResponse.getJSONObject(0).getString("id");
                            Log.d("Meine Id",credentials[2]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (credentials[1] != null){
                            credentials[1] = headers.get("Token");
                            prefs = self.getSharedPreferences(
                                    "de.winterapps.appxpired", Context.MODE_PRIVATE);
                            prefs.edit().putString("Token",credentials[1]).apply();
                            prefs.edit().putString("Username",userInput).apply();
                        }

                        if(credentials[0].equals("true")){
                            RequestResponse[0] = true;
                            intent = new Intent(loginActivity.this, menuActivity.class);
                            startActivity(intent);
                        }else{
                            RequestResponse[0] = false;
                            Toast.makeText(loginActivity.this, "Bad credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(loginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }

                }) {

            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>  params = new HashMap<String, String>();

                prefs = self.getSharedPreferences(
                        "de.winterapps.appxpired", Context.MODE_PRIVATE);

                String prefToken = prefs.getString("Token", "");

                params.put("Appxpired-Username", userInput);
                params.put("Appxpired-Password", passInput);
                if (token == true){
                    keepLoggedIn = true;
                }
                if (keepLoggedIn == true && prefToken != ""){
                    params.put("Appxpired-Token", prefs.getString("Token", ""));
                }

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }
    public boolean backendRequestFetchData (final String userInput, final String passInput, final Boolean token){
        Log.d("in dem", "request");
        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        String url ="http://www.appxpired.winterapps.de/api/api.php";
        final Boolean[] RequestResponse = {false};
        memberVariables member = new memberVariables();
        member.initDatabase();
        final localDatabase database = member.getDatabase();

        if(keepLoggedIn != null){
            if (keepLoggedIn == false){
                prefs.edit().remove("Username").commit();
                prefs.edit().remove("Token").commit();
            }
        }

        // Request a string response from the provided URL.
        stringRequest = new responseClass(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("meine antwort", response);
                        Log.d("meine antwort", "HAHAHA DU BIST LUSTIG");
                        Map<String,String> headers = stringRequest.headers;
                        String[] credentials = new String[3];
                        credentials[0] = headers.get("Success");
                        credentials[1] = headers.get("Token");
                        if (credentials[1] != null){
                            credentials[1] = headers.get("Token");
                            prefs = self.getSharedPreferences(
                                    "de.winterapps.appxpired", Context.MODE_PRIVATE);
                            prefs.edit().putString("Token",credentials[1]).apply();
                            prefs.edit().putString("Username",userInput).apply();
                    }

                    JSONArray jsonResponse = null;
                        try {
                            jsonResponse = new JSONArray(response);
                            for (int i = 0; i < jsonResponse.length(); i++){
                                Log.d("datenbank eintraege", String.valueOf(jsonResponse.getJSONObject(i)));
                                database.addFood(jsonResponse.getJSONObject(i));

                            }

                            Log.d("datenbank eintraege", String.valueOf(database.getFood()));
                          /*  credentials[2] = jsonResponse.getString("name");
                            credentials[2] = jsonResponse.getString("entryDate");
                            credentials[2] = jsonResponse.getString("expireDate");
                            credentials[2] = jsonResponse.getString("");
                            credentials[2] = jsonResponse.getString("name");
                            credentials[2] = jsonResponse.getString("name");
                            credentials[2] = jsonResponse.getString("name");*/


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(credentials[0].equals("true")){
                            RequestResponse[0] = true;
                            intent = new Intent(loginActivity.this, menuActivity.class);
                            startActivity(intent);
                        }else{
                            RequestResponse[0] = false;
                            Toast.makeText(loginActivity.this, "Bad credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(loginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }

                }) {

            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>  params = new HashMap<String, String>();

                prefs = self.getSharedPreferences(
                        "de.winterapps.appxpired", Context.MODE_PRIVATE);

                String prefToken = prefs.getString("Token", "");
                params.put("Appxpired-Username", userInput);
                params.put("Appxpired-Password", passInput);
                params.put("Appxpired-Table", "groceries");
                params.put("Appxpired-Wherevalues", "household.id,7");
                params.put("Appxpired-Selectvalues", "*");
                if (token == true){
                    keepLoggedIn = true;
                }
                if (keepLoggedIn == true && prefToken != ""){
                    params.put("Appxpired-Token", prefs.getString("Token", ""));
                }

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }

}


