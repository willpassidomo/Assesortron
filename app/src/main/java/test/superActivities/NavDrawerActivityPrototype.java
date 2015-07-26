package test.superActivities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import test.assesortron5.R;
import test.drawers.DrawerActivtyListener;

public abstract class NavDrawerActivityPrototype extends FragmentActivity implements DrawerActivtyListener, FragmentDrawerListener {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecylerView;
    ActionBarDrawerToggle drawerToggle;


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

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(getDrawerClosedHeader());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(getDrawerOpenHeader());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(drawerToggle);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_super_site_visit, menu);
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
    public void displayFragment(Fragment fragment) {
        Log.i("Displaying Fragmment", "");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.super_site_visit_main, fragment);
        ft.commit();
        mDrawerLayout.closeDrawer(mRecylerView);
    }

    @Override
    public void displayFragment(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.super_site_visit_main, fragment);
        ft.commit();
        mDrawerLayout.closeDrawer(mRecylerView);
    }

    @Override
    public void startIntent(Intent intent) {
        Log.i("Startig Intent", intent.getAction());
        startActivity(intent);
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
    }

}
