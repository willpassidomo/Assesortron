package test.superActivities;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import test.Fragments.DrawRequestItemList;
import test.Fragments.DrawRequestItemsFragment;
import test.Fragments.SetListSelectionFragment;
import test.assesortron5.R;
import test.drawers.IconHeaderRecyclerAdapter;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperSiteVisit extends NavDrawerActivityPrototype {

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(R.layout.header_site_walk_drawer, this);
        IconHeaderRecyclerAdapter.IconHeaderObject[] items = {
                adapter.newItem(this, "Site Visit Dashboard", R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Walk Through" ,R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Draw Request",R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Draw Request Items",R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Finish Site Walk",R.drawable.ic_check_thick, new Fragment()),
                adapter.newItem(this, "DELETE SITE WALK", R.drawable.ic_red_x, new Fragment())
        };
        adapter.setItems(items);
        return adapter;
    }

    @Override
    public String getDrawerClosedHeader() {
        return "Site Visits";
    }

    @Override
    public String getDrawerOpenHeader() {
        return "Site Visit Menu";
    }

    @Override
    public void initialize() {}

    @Override
    public void handleIntent(Intent intent) {

    }
}
