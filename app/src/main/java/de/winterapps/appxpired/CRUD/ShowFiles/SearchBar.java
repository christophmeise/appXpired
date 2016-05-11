package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.widget.SearchView;

import de.winterapps.appxpired.R;

/**
 * Created by Shark919 on 11.05.2016.
 */
public class SearchBar extends LayoutObject {

    public SearchBar() {
        super();
    }

    public static void buildLayout(Context that) {
        showActivity.oSearch = (SearchView)((Activity) that).findViewById(R.id.searchView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) ((Activity) that).getSystemService(Context.SEARCH_SERVICE);
            showActivity.oSearch.setSearchableInfo(manager.getSearchableInfo(((Activity) that).getComponentName()));
            showActivity.oSearch.setOnQueryTextListener(new OnQueryTextListener(that));
        }
    }
}
