package de.winterapps.appxpired.CRUD;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 30.11.2015.
 */
    public class customAdapter extends BaseAdapter implements ListAdapter {
        private JSONArray list = new JSONArray();
        private Context context;
        BaseAdapter self = this;



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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        list.remove(position); //or some other task
                    }
                    notifyDataSetChanged();
                }
            });

            deleteBtn.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(context, showDetailedActivity.class);
                    intent.putExtra("index", position);
                    context.startActivity(intent);
                    return false; //false
                }
            });

            return view;
        }
    }
