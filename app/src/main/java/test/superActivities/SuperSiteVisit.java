package test.superActivities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.internal.widget.TintContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import test.Network.FullSyncService;
import test.SiteVisitFragments.DrawRequestFragment;
import test.SiteVisitFragments.DrawRequestItemsManagerFragment;
import test.SiteVisitFragments.FieldValueDashboard;
import test.SiteVisitFragments.FinishedWalkThroughs;
import test.assesortron5.R;
import test.SiteVisitFragments.SiteWalkthrough;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.DrawRequest;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.User;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperSiteVisit extends NavDrawerActivityPrototype implements DrawRequestFragment.DrawRequestFragmentListener {
    String userId;
    DrawRequestItemsManagerFragment drawRequestItemsManager;
    Context context = this;
    SiteVisit siteVisit;
    BReceiver broadcastReceiver = new BReceiver();


    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
       // mDrawerLayout.openDrawer(mRecylerView);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_new_item);
        DrawableCompat.setTint(drawable, Color.WHITE);

        addFab(drawable, newWalkthroughListener());
        IntentFilter intentFilter = new IntentFilter(FullSyncService.BROADCAST_SYNC_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        if (broadcastReceiver == null) {
            broadcastReceiver = new BReceiver();
        }
        registerReceiver(broadcastReceiver, intentFilter);
//        floatingActionButton.setElevation(8);


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
        if (id == R.id.action_delete) {
            deleteSiteWalk();
        }
        if (id == R.id.action_email) {
            //String email = Storage.getUserById(this, Storage.getProjectById(this, siteVisit.getProjectId()).getUser()).getEmail();
            String userEmail = Storage.getUserById(this, userId).getEmail();
            new MaterialDialog.Builder(this)
                    .title("Email Site Visit")
                    .content("enter email address:")
                    .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .input("stuff", userEmail != null ? userEmail : "" , new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                            Log.i("Input", charSequence.toString());
                        }
                    })
                    .positiveText("Send Email")
                    .negativeText("cancel")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            Intent intent = new Intent(context, FullSyncService.class);
                            String email = dialog.getInputEditText().getText().toString();
                            intent.putExtra(Constants.EMAIL_ADDRESS, email);
                            intent.putExtra(Constants.ID_TYPE, Constants.TYPE_SITEVISIT);
                            intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                            startService(intent);
                            dialog.dismiss();
                            done();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void addFab(Drawable drawable, View.OnClickListener listener) {
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setImageDrawable(drawable);
        floatingActionButton.setRippleColor(Color.RED);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params.setMargins(0, 0, 20, 20);
        floatingActionButton.setLayoutParams(params);
        floatingActionButton.setOnClickListener(listener);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.super_site_visit_main);
        frameLayout.addView(floatingActionButton);
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
        drawRequestItemsManager = DrawRequestItemsManagerFragment.newInstance(siteVisit.getId());
        IconHeaderRecyclerAdapter.ListItem[] items = {
                adapter.newItem(this, "Site Visit Dashboard", R.drawable.ic_info, FieldValueDashboard.newInstance(siteVisit.getFieldValues(),siteVisit.getId())),
                adapter.newItem(this, "Draw Request",R.drawable.ic_clipboard, DrawRequestFragment.newInstance(this, siteVisit.getDrawRequest())),
                adapter.newItem(this, "Items Manager",R.drawable.ic_file_folder, drawRequestItemsManager),
                adapter.newItem(this, "Complete Walk Throughs", R.drawable.ic_list_items, FinishedWalkThroughs.newInstance(siteVisit.getId())),
                adapter.newItem(this, "Finish Site Walk",R.drawable.ic_check_thick, finishSiteWalkListner()),
                adapter.newItem(this, "Leave Site Visit", R.drawable.ic_back_arrow, leaveSiteVisit())
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
        userId = intent.getStringExtra(Constants.USER_ID);
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

    private void deleteSiteWalk() {
        Toast.makeText(this, "Delete Site Walk Not Performed\nTODO", Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener leaveSiteVisit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
    }


    @Override
    public void submitDrawRequest(DrawRequest drawRequest) {
        Storage.storeDrawRequest(this, siteVisit.getId(), drawRequest);
        done();
    }


    public class BReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Toast.makeText(context, "message: " + intent.getStringExtra(FullSyncService.OUT_BOUND_MESSAGE), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "null message", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
