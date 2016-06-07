package de.winterapps.appxpired.CRUD.AddFiles;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;

/**
 * Created by D062400 on 30.05.2016.
 */
public class customAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private JSONArray list;
    public ListAdapter that = this;

    public customAdapter(String[] list, Context context) {
        this.list = new JSONArray();
        for (int i = 0; i < list.length; i++){
            this.list.put(list[i]);
        }
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_template_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);

        try {
            String name = list.getString(position);   //get the 3rd item
            //String name = json_obj.getString("name");
            listItemText.setText(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Handle buttons and add onClickListeners
        final Button useButton = (Button)view.findViewById(R.id.delete_btn);
        //Button addBtn = (Button)view.findViewById(R.id.add_btn);

        useButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                localDatabase database = new localDatabase(context);
                JSONArray templates = database.getTemplates();
                JSONObject food = null;
                String name;
                for (int i=0;i<templates.length();i++) {
                    try {
                        JSONObject template = (JSONObject) templates.get(i);
                        name = template.getString("name");
                        food = (JSONObject) list.get(position);
                        String tester = food.get("name").toString();
                        if (name.equals(tester)) {
                            //TODO: dann alles ausfüllen
                        }
                        ((memberVariables) ((Activity) context).getApplication()).setName(template.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /* ((memberVariables) ((Activity) that).getApplication()).setBrand(Brand);
                ((memberVariables) ((Activity) that).getApplication()).setSize(Amount);*/
                Intent intent = new Intent(context, addActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
