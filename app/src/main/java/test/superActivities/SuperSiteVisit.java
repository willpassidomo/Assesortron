package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import test.SiteVisitFragments.DrawRequestFragment;
import test.SiteVisitFragments.DrawRequestItemsManagerFragment;
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
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(R.layout.header_site_walk_drawer, this);
        IconHeaderRecyclerAdapter.IconHeaderObject[] items = {
                adapter.newItem(this, "Site Visit Dashboard", R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Walk Through" ,R.drawable.ic_idk, newWalkthroughListener()),
                adapter.newItem(this, "Draw Request",R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Draw Request Items",R.drawable.ic_idk, DrawRequestItemsManagerFragment.newInstance(siteVisit.getId(), Storage.getDrawRequestBySiteWalkId(this, siteVisit.getId()))),
                adapter.newItem(this, "Complete Walk Throughs", R.drawable.ic_idk,new Fragment()),
                adapter.newItem(this, "Finish Site Walk",R.drawable.ic_check_thick, finishSiteWalkListner()),
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
        String siteVisitId = intent.getStringExtra(Constants.SITE_VISIT_ID);
        siteVisit = Storage.getSiteWalkById(this, siteVisitId);
        siteVisit.setDrawRequest(Storage.getDrawRequestBySiteWalkId(this, siteVisitId));
        siteVisit.setWalkThroughs(Storage.getWalkThroughBySiteWalkId(this, siteVisitId));
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
                Intent intent = new Intent(context, SuperProject.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getProjectId());
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
