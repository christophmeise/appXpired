package de.winterapps.appxpired;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by D062332 on 24.04.2016.
 */
public class localDatabaseTest {

    @Mock
    Context context;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cleanseValuesAddFoodTest(){

        localDatabase db = new localDatabase(context);
        JSONObject foodEntryJSON = new JSONObject();
        try {
            foodEntryJSON.put("id",5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Integer id = 5;
        ContentValues foodEntryCV = db.cleanseValuesAddFood(foodEntryJSON);
        Assert.assertEquals(id,foodEntryCV.getAsInteger("backendId"));
    }
}
