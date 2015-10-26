package test.superActivities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import test.assesortron5.R;
import test.drawers.DrawerActivtyListener;
import test.drawers.IconHeaderRecyclerAdapter;
import test.persistence.Constants;

public abstract class NavDrawerActivityPrototype extends AppCompatActivity implements DrawerActivtyListener, FragmentDrawerListener {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecylerView;
    ActionBarDrawerToggle drawerToggle;
    public static String IS_FRAG_DISPLAYED = "is_frag_displayed";
    public static String FRAG_DISPLAYED_NAME = "frag_displayed_name";
    boolean FRAGMENT_DISPLAYED = false;
    String FRAGMENT_DISPLAYED_NAME = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_activity_site_visit);

        handleIntent(getIntent());
        initialize();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.super_site_visit_drawer_layout);
        mRecylerView = (RecyclerView)findViewById(R.id.super_site_visit_drawer);


        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(getRecyclerAdapter());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
                );

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        boolean fragDisplayed = false;
        String fragDisplayedName = null;
        if (savedInstanceState != null) {
            fragDisplayed = savedInstanceState.getBoolean(IS_FRAG_DISPLAYED);
            fragDisplayedName = savedInstanceState.getString(FRAG_DISPLAYED_NAME);
        }
        if (mRecylerView.getAdapter() instanceof IconHeaderRecyclerAdapter && !fragDisplayed) {
            IconHeaderRecyclerAdapter adapter = (IconHeaderRecyclerAdapter) mRecylerView.getAdapter();
            adapter.displayFirst();
            done();
        } else if (fragDisplayed && fragDisplayedName != null) {
            Log.i("Restart", "displaying : " + fragDisplayedName);
            IconHeaderRecyclerAdapter adapter = (IconHeaderRecyclerAdapter) mRecylerView.getAdapter();
            adapter.display(fragDisplayedName);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_super_site_visit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(IS_FRAG_DISPLAYED, FRAGMENT_DISPLAYED);
        bundle.putString(FRAG_DISPLAYED_NAME, FRAGMENT_DISPLAYED_NAME);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mRecylerView)) {
            super.onBackPressed();
        } else {
            mDrawerLayout.openDrawer(mRecylerView);
        }
    }

    @Override
    public void displayFragment(Fragment fragment, String title) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.super_site_visit_main, fragment);
        ft.commit();
        mDrawerLayout.closeDrawer(mRecylerView);
        FRAGMENT_DISPLAYED = true;
    }

    public void displayFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.super_site_visit_main, fragment)
                .commit();
    }

    @Override
    public void displayFragment(android.support.v4.app.Fragment fragment, String title) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.super_site_visit_main, fragment, title);
        ft.commit();
        mDrawerLayout.closeDrawer(mRecylerView);
        FRAGMENT_DISPLAYED = true;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        drawerToggle.onConfigurationChanged(config);
    }

    @Override
    public void onPostCreate(Bundle saved) {
        super.onPostCreate(saved);
        drawerToggle.syncState();
    }

    public abstract RecyclerView.Adapter getRecyclerAdapter();
    public abstract String getDrawerClosedHeader();
    public abstract String getDrawerOpenHeader();
    public abstract void initialize();
    public abstract void handleIntent(Intent intent);

    @Override
    public void done() {
        mDrawerLayout.openDrawer(mRecylerView);
        getSupportActionBar().setTitle(getDrawerClosedHeader());
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
        FRAGMENT_DISPLAYED_NAME = title;
    }

}
