package de.winterapps.appxpired.CRUD.ShowFiles;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by D062400 on 02.05.2016.
 */
public class Positions {

    JSONObject positionList = new JSONObject();

    public Positions() {
        try {
            positionList.put("0", "All");
            positionList.put("1", "Kühlschrank");
            positionList.put("2", "Wandschrank");
            positionList.put("3", "Lagerraum");
            positionList.put("4", "Keller");
            positionList.put("5", "Regal");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String parseValue(int i){
        String result = "";
        try {
            result = positionList.get(String.valueOf(i)).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
