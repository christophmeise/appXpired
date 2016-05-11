package de.winterapps.appxpired.CRUD.ShowFiles;

import android.content.Context;
import android.widget.SearchView;

/**
 * Created by Shark919 on 11.05.2016.
 */
public class OnQueryTextListener implements SearchView.OnQueryTextListener {

    Context that;

    public OnQueryTextListener(Context that) {
        this.that = that;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ((showActivity) that).synchronizeItems(query);
        return false;
    }
}
