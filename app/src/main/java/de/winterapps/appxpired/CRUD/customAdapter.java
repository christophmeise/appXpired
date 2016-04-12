package de.winterapps.appxpired.CRUD;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;
import de.winterapps.appxpired.menuActivity;

/**
 * Created by D062400 on 30.11.2015.
 */
    public class customAdapter extends BaseAdapter implements ListAdapter {
        private JSONArray list = new JSONArray();
        private Context context;
        BaseAdapter self = this;
        responseClass stringRequest;


        public customAdapter(JSONArray list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.length();
        }

        @Override
        public Object getItem(int pos) {
            Object result = new JSONObject();
            try {
                result = list.get(pos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_show_list, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);

            try {
                JSONObject json_obj = list.getJSONObject(position);   //get the 3rd item
                String name = json_obj.getString("name");
                listItemText.setText(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Handle buttons and add onClickListeners
            final Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
            //Button addBtn = (Button)view.findViewById(R.id.add_btn);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    localDatabase database = new localDatabase(context);
                    JSONArray foodList = database.getFood();
                    int id;
                    String name;
                    for (int i = 0; i < foodList.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = foodList.getJSONObject(i);
                            id = row.getInt("backendId");
                            name = row.getString("name");
                            JSONObject food = (JSONObject) list.get(position);
                            String tester = food.get("name").toString();
                            if (name.equals(tester)){
                                database.deleteFood(id);
                                memberVariables members = memberVariables.sharedInstance;
                                String user = members.username;
                                String token = members.token;
                                backendRequestDelete(user, "", token, id+"");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        list.remove(position); //or some other task
                    }
                    notifyDataSetChanged();
                }
            });

            deleteBtn.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(context, testActivity.class);
                    intent.putExtra("index", position);
                    context.startActivity(intent);
                    return false; //false
                }
            });

            return view;
        }

    private boolean backendRequestDelete(final String user, final String pass, final String token, final String Wherevalues){

        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        String url ="http://www.appxpired.winterapps.de/api/api.php";
        final Boolean[] RequestResponse = {false};

        // Request a string response from the provided URL.
        stringRequest = new responseClass(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String,String> headers = null;
                        try {
                            Log.d("loginAc", customAdapter.this.stringRequest.toString());
                            headers = customAdapter.this.stringRequest.headers;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                  /*      String[] credentials = new String[3];
                        credentials[0] = headers.get("Success");
                        credentials[1] = headers.get("Token");
                        JSONArray jsonResponse = null;
                        try {
                            jsonResponse = new JSONArray(response);
                            credentials[2] = jsonResponse.getJSONObject(0).getString("id");
                            Log.d("Meine Id",credentials[2]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                       /* if(credentials[0].equals("true")){
                            RequestResponse[0] = true;
                            intent = new Intent(loginActivity.this, menuActivity.class);
                            startActivity(intent);
                        }else{
                            RequestResponse[0] = false;
                            Toast.makeText(loginActivity.this, "Bad credentials!", Toast.LENGTH_SHORT).show();
                        }*/
                        Log.d("Response from Backend: ", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error: ", error.toString());
                        //Toast.makeText(context.getClass(), "Server error", Toast.LENGTH_SHORT).show();
                    }

                }) {

            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>  params = new HashMap<String, String>();

               /* prefs = self.getSharedPreferences(
                        "de.winterapps.appxpired", Context.MODE_PRIVATE);*/

                params.put("Appxpired-Username", user);
                if(pass != null) {
                    params.put("Appxpired-Password", pass);
                }
                else{
                    params.put("Appxpired-Token", token);
                }

                params.put("Appxpired-Wherevalues", "id,"+Wherevalues);

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }
    }

