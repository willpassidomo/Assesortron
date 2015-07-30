package test.SiteVisitFragments;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.adapters.swipeWalkThroughAdapter;
import test.assesortron5.R;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.WalkThrough;
import test.persistence.Constants;
import test.persistence.Storage;
import test.superActivities.SuperSiteVisit;

/**
 * Created by willpassidomo on 2/3/15.
 */
public class SiteWalkthrough extends AppCompatActivity implements SiteWalkthroughNote.OnNoteFragListener, SiteWalkthrougInfo.OnInfoFragListener, SiteWalkthroughPictures.OnPictureFragListener{
    ActionBar actionBar;
    FragmentTransaction ft;
    Button action;

    Project project;
    SiteVisit siteVisit;
    WalkThrough walkThrough;
    Context context = this;
    ViewPager viewPager;

    List<TabFragment> tabFragments = new ArrayList<TabFragment>();
    List<Fragment> fragments = new ArrayList<Fragment>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        siteVisit = Storage.getSiteWalkById(this, getIntent().getStringExtra(Constants.SITE_VISIT_ID));
        project = Storage.getProjectById(this, siteVisit.getProjectId());
        String walkthroughId = getIntent().getStringExtra(Constants.WALK_THROUGH_ID);
        if (walkthroughId != null) {
            walkThrough = Storage.getWalkThroughById(this, walkthroughId);
        } else {
            walkThrough = new WalkThrough();
        }

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tabFragments.add(new SiteWalkthrougInfo());
        tabFragments.add(new SiteWalkthroughNote());
        tabFragments.add(new SiteWalkthroughPictures());

        for(TabFragment tf: tabFragments) {
            fragments.add(tf.getFragment());
        }

        action = (Button) this.findViewById(R.id.walk_through_action_button);

        if (walkthroughId != null) {
            setEditButton();
        } else {
            setSubmitButton();
        }

        setSwipeView();
        setTabs();
        setPictureList();
    }

    private void setTabs() {
        for (final TabFragment tabFragment: tabFragments) {
            actionBar.addTab(actionBar.newTab().setText(tabFragment.getTabName()).setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    tabFragment.setFields(walkThrough, project);
                    viewPager.setCurrentItem(tab.getPosition(),true);
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    tabFragment.getValues();
                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    tabFragment.setFields(walkThrough, project);

                }
            }));
        }
    }

    private void setSwipeView() {
        swipeWalkThroughAdapter swta = new swipeWalkThroughAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.walk_through_fragment);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        viewPager.setAdapter(swta);
    }


    private void setPictureList() {
        //TODO
    }

    //TODO
    //this is breaking the back button...

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SuperSiteVisit.class);
        intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
        startActivity(intent);
    }

    @Override
    public void onNoteEntered(String note) {
        walkThrough.setNote(note);
    }

    @Override
    public void getPictures(List<String> uri) {
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
                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                startActivity(intent);
            }
        });
    }

    public void setEditButton() {
        Toast.makeText(context, "Edit Button not yet implemented",Toast.LENGTH_SHORT).show();

        //TODO
    }
}
