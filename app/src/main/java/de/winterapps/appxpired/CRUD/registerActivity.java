package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 20.10.2015.
 */
public class registerActivity extends AppCompatActivity{
    responseClass stringRequest;
    Button buttonRegister;
    EditText prenameEdit;
    EditText nameEdit;
    EditText passEdit;
    EditText confirmEdit;
    EditText emailEdit;
    EditText userEdit;
    final Activity self = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = (Button) findViewById(R.id.regRegisterButton);
        prenameEdit = (EditText) findViewById(R.id.regPrenameEdit);
        nameEdit = (EditText) findViewById(R.id.regNameEdit);
        passEdit = (EditText) findViewById(R.id.regPassEdit);
        confirmEdit = (EditText) findViewById(R.id.regConfirmEdit);
        emailEdit = (EditText) findViewById(R.id.regMailEdit);
        userEdit = (EditText) findViewById(R.id.regUserEdit);
        //View.OnClickListener registerHandler = null;


        buttonRegister.setOnClickListener(registerHandler);
    };

        View.OnClickListener registerHandler = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                {
                    final Intent intentSuccess = new Intent(registerActivity.this, loginActivity.class);
                    final String prename;
                    final String lastname;
                    final String password;
                    final String confirmpass;
                    final String email;
                    final String username;

                    buttonRegister = (Button) findViewById(R.id.regRegisterButton);
                    prenameEdit = (EditText) findViewById(R.id.regPrenameEdit);
                    nameEdit = (EditText) findViewById(R.id.regNameEdit);
                    passEdit = (EditText) findViewById(R.id.regPassEdit);
                    confirmEdit = (EditText) findViewById(R.id.regConfirmEdit);
                    emailEdit = (EditText) findViewById(R.id.regMailEdit);
                    userEdit = (EditText) findViewById(R.id.regUserEdit);

                    prename = prenameEdit.getText().toString();
                    lastname = nameEdit.getText().toString();
                    password = passEdit.getText().toString();
                    confirmpass = passEdit.getText().toString();
                    email = emailEdit.getText().toString();
                    username = userEdit.getText().toString();

                    // Instantiate the RequestQueue.
                    com.android.volley.RequestQueue queue;
                    queue = Volley.newRequestQueue(self);
                    String url = "http://www.appxpired.winterapps.de/api/userManagement.php";

                    // Request a string response from the provided URL.
                    stringRequest = new responseClass(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // split at ; first is true/false as server response
                                    System.out.println(response);
                                    System.out.println(stringRequest.headers.toString());
                                    Map<String,String> headers = stringRequest.headers;
                                    System.out.println(headers.get("Success"));
                                    String[] credentials = new String[1];
                                    credentials[0] = headers.get("Success");

                                    System.out.println(response);
                                    System.out.println(userEdit.toString()+" "+passEdit.toString());
                                    if(credentials[0].equals("true")){
                                        Toast.makeText(registerActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(intentSuccess);
                                    }else{
                                        Toast.makeText(registerActivity.this, "Ooops - something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(registerActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                                }

                            }) {

                        @Override
                        public HashMap<String, String> getHeaders() throws AuthFailureError { //getParams
                            HashMap<String, String>  params = new HashMap<String, String>();
                            //                        params.put("Appxpired-Username", "cmeise");
                            //                        params.put("Appxpired-Password", "CMeise12345");
                            params.put("Appxpired-Username", username);
                            params.put("Appxpired-Password", password);
                            params.put("Appxpired-Firstname", prename);
                            params.put("Appxpired-Lastname", lastname);
                            params.put("Appxpired-Token", "");
                            params.put("Appxpired-Email", email);
                            return params;
                        }
                    };
// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }

            }
        };
}