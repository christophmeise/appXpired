package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.winterapps.appxpired.CRUD.responseClass;
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
    ArrayAdapter<CharSequence> unitSpinnerAdapter;
    ArrayAdapter<CharSequence> positionSpinnerAdapter;
    ArrayAdapter<CharSequence> categorySpinnerAdapter;
    Spinner oUnitsSpinner;
    Spinner oCategorySpinner;
    Spinner oPositionSpinner;
    localDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add);
        // intent given in customAdapter class
        index = getIntent().getExtras().getInt("index");
        Log.d("Mein Index lautet: ", String.valueOf(index));
        /*Intent intent = getIntent();
        index = intent.getIntExtra("index",0);*/

        oUnitsSpinner = (Spinner) findViewById(R.id.addAmountSpinner);

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
        populateSpinners();
    }

    private void populateSpinners() {
        database = new localDatabase(this);
        populateCategorySpinner();
        populateUnitsSpinner();
        populatePositionsSpinner();
    }

    private void populatePositionsSpinner() {
        JSONArray positions = database.getPositions();
        ArrayList positionsSpinnerFormat = new ArrayList();
        JSONObject positionElement;
        for (int i = 0; i < positions.length(); i++){
            try {
                positionElement = (JSONObject) positions.get(i);
                positionsSpinnerFormat.add(positionElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        positionSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, positionsSpinnerFormat);
        oPositionSpinner.setAdapter(positionSpinnerAdapter);
    }

    private void populateUnitsSpinner() {
        unitSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        unitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oUnitsSpinner.setAdapter(unitSpinnerAdapter);
    }

    private void populateCategorySpinner() {
        JSONArray categories = database.getCategories();
        ArrayList categoriesSpinnerFormat = new ArrayList();
        JSONObject categoryElement;
        for (int i = 0; i < categories.length(); i++){
            try {
                categoryElement = (JSONObject) categories.get(i);
                categoriesSpinnerFormat.add(categoryElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        categorySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesSpinnerFormat);
        oCategorySpinner.setAdapter(categorySpinnerAdapter);

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
