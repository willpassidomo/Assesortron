package test.superActivities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.internal.widget.TintContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import test.SiteVisitFragments.DrawRequestFragment;
import test.SiteVisitFragments.DrawRequestItemsManagerFragment;
import test.SiteVisitFragments.FieldValueDashboard;
import test.SiteVisitFragments.FinishedWalkThroughs;
import test.assesortron5.R;
import test.SiteVisitFragments.SiteWalkthrough;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.DrawRequest;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperSiteVisit extends NavDrawerActivityPrototype implements DrawRequestFragment.DrawRequestFragmentListener {

    Context context = this;
    SiteVisit siteVisit;


    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_new_item);
        DrawableCompat.setTint(drawable, Color.WHITE);
        floatingActionButton.setImageDrawable(drawable);
        floatingActionButton.setRippleColor(Color.RED);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params.setMargins(0,0,20,20);
        floatingActionButton.setLayoutParams(params);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.super_site_visit_main);
        frameLayout.addView(floatingActionButton);
//        floatingActionButton.setElevation(8);
    }

    private View headerView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.header_site_walk_drawer, parent, false);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(this) {
            @Override
            public View inflateHeader(LayoutInflater inflater, ViewGroup parent) {
                return headerView(inflater, parent);
            }
        };
        IconHeaderRecyclerAdapter.ListItem[] items = {
                adapter.newItem(this, "Site Visit Dashboard", R.drawable.ic_idk, FieldValueDashboard.newInstance(siteVisit.getFieldValues())),
                adapter.newItem(this, "Walk Through" ,R.drawable.ic_idk, newWalkthroughListener()),
                adapter.newItem(this, "Draw Request",R.drawable.ic_idk, DrawRequestFragment.newInstance(this, siteVisit.getDrawRequest())),
                adapter.newItem(this, "Draw Request Items",R.drawable.ic_idk, DrawRequestItemsManagerFragment.newInstance(siteVisit.getId())),
                adapter.newItem(this, "Complete Walk Throughs", R.drawable.ic_idk, FinishedWalkThroughs.newInstance(siteVisit.getId())),
                adapter.newItem(this, "Finish Site Walk",R.drawable.ic_check_thick, finishSiteWalkListner()),
                adapter.newItem(this, "DELETE SITE WALK", R.drawable.ic_red_x, new Fragment())
        };
        adapter.setItems(items);
        return adapter;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
        String siteVisitId = intent.getStringExtra(Constants.SITE_VISIT_ID);
        siteVisit = Storage.getSiteWalkById(this, siteVisitId);
        siteVisit.setDrawRequest(Storage.getDrawRequestBySiteWalkId(this, siteVisitId));
        if (siteVisit.getDrawRequest() == null) {
            siteVisit.setDrawRequest(new DrawRequest());
            Storage.storeDrawRequest(this, siteVisit.getId(), siteVisit.getDrawRequest());
        }
    }


    private View.OnClickListener newWalkthroughListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener finishSiteWalkListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siteVisit.setActive(false);
                Storage.storeSiteVisit(context, siteVisit.getProjectId(), siteVisit);
                Intent intent = new Intent(context, SuperProject.class);
                intent.putExtra(Constants.PROJECT_ID, siteVisit.getProjectId());
                startActivity(intent);
            }
        };
    }

    @Override
    public void submitDrawRequest(DrawRequest drawRequest) {
        Storage.storeDrawRequest(this, siteVisit.getId(), drawRequest);
        done();
    }


}
