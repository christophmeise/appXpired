package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;

/**
 * Created by D062400 on 03.12.2015.
 */
public class detailsActivity extends Activity{
    int index;
    String name;
    String date;
    String position;
    String description;
    String category;
    float amount;
    String amountType;
    Activity self = this;
    responseClass stringRequest;
    memberVariables members = memberVariables.sharedInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add);
        // intent given in customAdapter class
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);

        attachListeners();
        loadInitial();
    }

    private void loadInitial(){
        localDatabase database = new localDatabase(this);
        JSONObject food = database.getFood(index);

        TextView oName;
        oName = (TextView) findViewById(R.id.editName);
        // add other values
        try {
            oName.setText(food.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Handler
    public View.OnClickListener addButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            readFields();
            backendRequestUpdate(members.getUsername(), "", members.getToken());
        }
    };

    private void readFields() {
        TextView oName = (TextView) findViewById(R.id.editName);
        TextView oAmount = (TextView) findViewById(R.id.addAmountEdit);
        Spinner oAmountType = (Spinner) findViewById(R.id.addAmountSpinner);
        TextView oDate = (TextView) findViewById(R.id.addDateEdit);
        Spinner oCategory = (Spinner) findViewById(R.id.addCategorySpinner);
        TextView oDescription = (TextView) findViewById(R.id.addDescEdit);
        Spinner oPosition = (Spinner) findViewById(R.id.addPositionSpinner);

        name = (String) oName.getText();
        description = (String) oDescription.getText();
        date = (String) oDate.getText();
        position = oPosition.getSelectedItem().toString();
        category = oCategory.getSelectedItem().toString();
        amount = Float.valueOf((String) oAmount.getText());
        amountType = oAmountType.getSelectedItem().toString();
    }

    private boolean backendRequestUpdate(final String user, final String pass, final String token){

        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        String url ="http://www.appxpired.winterapps.de/api/api.php";
        final Boolean[] RequestResponse = {false};

        // Request a string response from the provided URL.
        stringRequest = new responseClass(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String,String> headers = null;
                        try {
                            Log.d("loginAc", detailsActivity.this.stringRequest.toString());
                            headers = detailsActivity.this.stringRequest.headers;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String[] credentials = new String[1];
                       /* credentials[0] = headers.get("LastId");
                        backendid = credentials[0];
                        addToDatabase(backendid);
                        Log.d("Hier bekomme ich me", backendid);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();
                    }

                }) {

            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>  params = new HashMap<String, String>();

                params.put("Appxpired-Username", user);
                if(pass != null) {
                    params.put("Appxpired-Token", token);
                    // params.put("Appxpired-Password", pass);
                }
                else{
                    params.put("Appxpired-Token", token);
                }
                params.put("Appxpired-Table", "groceries");
                String userid = members.userid;
                params.put("Appxpired-Setvalues", "name,"+name+";household.id,7;createuser.id,"+userid);
                params.put("Appxpired-Wherevalues", "id,"+index);

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }

    //

    private void attachListeners(){
        Button addButton = (Button) findViewById(R.id.addAddButton);
        View.OnClickListener addButtonListener = addButtonHandler;
        addButton.setOnClickListener(addButtonListener);
    }
}
