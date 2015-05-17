package test.assesortron3;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.adapters.swipeWalkThroughAdapter;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.WalkThrough;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by willpassidomo on 2/3/15.
 */
public class SiteWalkthrough extends FragmentActivity implements SiteWalkthroughNote.OnNoteFragListener, SiteWalkthrougInfo.OnInfoFragListener, SiteWalkthroughPictures.OnPictureFragListener{
    ActionBar actionBar;
    FragmentTransaction ft;
    Button action;

    Project project;
    SiteVisit siteVisit;
    WalkThrough walkThrough;
    Context context;
    ViewPager viewPager;
    TextView projectName;

    List<TabFragment> tabFragments = new ArrayList<TabFragment>();
    List<Fragment> fragments = new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabFragments.add(new SiteWalkthrougInfo());
        tabFragments.add(new SiteWalkthroughNote());
        tabFragments.add(new SiteWalkthroughPictures());

        for(TabFragment tf: tabFragments) {
            fragments.add(tf.getFragment());
        }

        context = this;

        Intent intent = getIntent();
        String id = intent.getStringExtra(Constants.SITE_VISIT_ID);
        siteVisit = Storage.getSiteWalkById(this, id);
        project = Storage.getProjectById(this, intent.getStringExtra(Constants.PROJECT_ID));

        String type = intent.getStringExtra(Constants.NEW_OR_EDIT);

        setVariables();

        if(type.equals(Constants.NEW)) {
            walkThrough = new WalkThrough();
            newWalkThrough();
        } else if (type.equals(Constants.EDIT)) {
            walkThrough = Storage.getWalkThroughById(this, intent.getStringExtra(Constants.WALK_THROUGH_ID));
            editWalkThrough();
        }

        setSwipeView();
        setTabs();
        setPictureList();

        projectName = (TextView) this.findViewById(R.id.walk_through_project_name);
        projectName.setText(project.getName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.site_walkthrough, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTabs() {
        for (final TabFragment tabFragment: tabFragments) {
            actionBar.addTab(actionBar.newTab().setText(tabFragment.getTabName()).setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
//                    newFragTrans();
                   tabFragment.setFields(walkThrough, project);
//                    Fragment fragment = tabFragment.getFragment();
//                    ft.replace(R.id.walk_through_fragment, fragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
                    viewPager.setCurrentItem(tab.getPosition(),true);

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                    tabFragment.getValues();
                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                    tabFragment.setFields(walkThrough, project);
                }
            }));
        }
    }

    private void setSwipeView() {
        swipeWalkThroughAdapter swta = new swipeWalkThroughAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) this.findViewById(R.id.walk_through_fragment);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        viewPager.setAdapter(swta);
    }

    private void newFragTrans() {
        ft = getSupportFragmentManager().beginTransaction();
    }

    private void setVariables() {
        action = (Button) findViewById(R.id.walk_through_action_button);

    }

    private void newWalkThrough() {
        setSubmitButton();
    }

    private void editWalkThrough() {
        setUpdateButton();
    }

    private void setPictureList() {
        //TODO
    }

    @Override
    public void onNoteEntered(String note) {
        walkThrough.setNote(note);
    }

    @Override
    public void getPictures(List<Uri> uri) {
        walkThrough.setSitePictures(uri);
    }

    @Override
    public void getInfo(String floor, String trade, String progress) {
        walkThrough.setFloor(floor);
        walkThrough.setTrade(trade);
        walkThrough.setProgress(progress);
    }

    public void setSubmitButton() {
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabFragments.get(actionBar.getSelectedTab().getPosition()).getValues();


                Storage.storeWalkThrough(context, siteVisit.getId(), walkThrough);

                Intent intent = new Intent(context, ProjectHomeScreen.class);
                intent.putExtra("id",project.getId());
                startActivity(intent);
            }
        });
    }

    public void setUpdateButton() {
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabFragments.get(actionBar.getSelectedTab().getPosition()).getValues();

                Storage.storeWalkThrough(context, siteVisit.getId(), walkThrough);

                Intent intent = new Intent(context, History.class);
                intent.putExtra("id",project.getId());
                startActivity(intent);
            }
        });
    }
}
