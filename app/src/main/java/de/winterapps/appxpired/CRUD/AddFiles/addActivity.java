package de.winterapps.appxpired.CRUD.AddFiles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.winterapps.appxpired.CRUD.responseClass;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;

/**
 * Created by Christoph on 12.10.2015.
 */
public class addActivity extends AppCompatActivity {

    final static Calendar myCalendar = Calendar.getInstance();
    static EditText dateEdit;
    Button addButton;
    EditText editName;
    responseClass stringRequest;
    Activity self = this;
    memberVariables members = memberVariables.sharedInstance;
    String backendid;
    Spinner oPositionSpinner;
    Spinner oUnitsSpinner;
    Spinner oCategorySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE); //turn off loading indicator
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initializeLayout();
    }

    private void initializeLayout() {
        loadLayoutElements();
        populateSpinners();
        addListeners();

        editName.setText(((memberVariables) ((Activity) this).getApplication()).getName());
    }

    private void addListeners() {
        final DatePickerDialog.OnDateSetListener date = new OnDateSetListener(self);

        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();

                final String user = members.getUsername();
                //String pass = members.getPassword();
                final String token = members.getToken();
                final String Wherevalues = "";
                backendRequestAdd(user, "", token, Wherevalues);
            }
        };

        addButton.setOnClickListener(addListener);
    }

    private void populateSpinners() {
        localDatabase database = new localDatabase(this);
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
        oCategorySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoriesSpinnerFormat));

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oUnitsSpinner.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oPositionSpinner.setAdapter(adapter5);
    }

    private void loadLayoutElements() {
        addButton = (Button) findViewById(R.id.addAddButton);
        editName = (EditText) findViewById(R.id.editName);
        oPositionSpinner = (Spinner) findViewById(R.id.addPositionSpinner);
        oUnitsSpinner = (Spinner) findViewById(R.id.addAmountSpinner);
        oCategorySpinner = (Spinner) findViewById(R.id.addCategorySpinner);
        dateEdit = (EditText)findViewById(R.id.addDateEdit);
    }

    private void showPopup(){
        AddDialogFragment popup = new AddDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = AddDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }

    public static void updateLabel(Context context) {
        dateEdit = (EditText)((Activity) context).findViewById(R.id.addDateEdit);

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        dateEdit.setText(sdf.format(myCalendar.getTime()));
    }

    public void addToDatabase(String backendid){
        String name = editName.getText().toString();
        localDatabase database = new localDatabase(this);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        long expireDateMillis = 0;
        try {
            Date expireDate = format.parse(String.valueOf(dateEdit.getText()));
            expireDateMillis = expireDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject food = new JSONObject();
        try {
            food.put("name", name);
            food.put("expire_date", expireDateMillis);
            food.put("backendId", Integer.parseInt(backendid));
            food.put("category", database.getCategory(oCategorySpinner.getSelectedItem().toString()));
            //food.put("entry_date", )
            // add more values
        } catch (JSONException e) {
            e.printStackTrace();
        }
        database.addFood(food);
    }

    private boolean backendRequestAdd(final String user, final String pass, final String token, final String Wherevalues){

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

                        String[] credentials = new String[1];
                        try {
                            Log.d("loginAc", addActivity.this.stringRequest.toString());
                            headers = addActivity.this.stringRequest.headers;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        credentials[0] = headers.get("LastId");
                        backendid = credentials[0];
                        addToDatabase(backendid);
                        Log.d("Hier bekomme ich me", backendid);
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
                    // TODO
                   // params.put("Appxpired-Password", pass);
                }
                else{
                    params.put("Appxpired-Token", token);
                }
                params.put("Appxpired-Table", "groceries");
                String userid = members.userid;
                String name = editName.getText().toString();
                params.put("Appxpired-Setvalues", "name,"+name+";household.id,7;createuser.id,"+userid);

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }
}
