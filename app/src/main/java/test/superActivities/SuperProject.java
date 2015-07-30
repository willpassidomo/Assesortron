package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import test.Fragments.SetListSelectionFragment;
import test.ProjectFragments.ActiveSiteVisits;
import test.ProjectFragments.FinishedSiteVisits;
import test.ProjectFragments.NewSiteVisit;
import test.assesortron5.R;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.Project;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperProject extends NavDrawerActivityPrototype implements SetListSelectionFragment.SetListSelectionInterface, NewSiteVisit.NewSiteVisitListener {
    public static final String SET_PROJECT_TRADES = "set_project_trades";
    String projectId;
    Project project;
    final Context context = this;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
    }

    private View headerView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.header_project_drawer, parent, false);
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
                adapter.newItem(this, "Active Site Vists" ,R.drawable.ic_list_items, ActiveSiteVisits.newInstance(projectId)),
                adapter.newItem(this, "New Site Visit",R.drawable.ic_new_item, NewSiteVisit.newInstance(this, projectId)),
                adapter.newItem(this, "Project Dashboard",R.drawable.ic_clipboard,new Fragment()),
                adapter.newItem(this, "Project Info",R.drawable.ic_info, new Fragment()),
                adapter.newItem(this, "Finished Site Visits", R.drawable.ic_file_folder, FinishedSiteVisits.newInstance(projectId)),
                adapter.newItem(this, "Set Trades",R.drawable.ic_idk, SetListSelectionFragment.getInstance(this, Storage.getTradeList(this), Storage.getProjectTradeList(this, projectId), SET_PROJECT_TRADES)),
                adapter.newItem(this, "Submit Project",R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Sync", R.drawable.ic_idk, snackBarTest()),
                adapter.newItem(this, "DELETE", R.drawable.ic_back_arrow, deleteProject())
        };
        adapter.setItems(items);
        return adapter;
    }

    @Override
    public String getDrawerClosedHeader() {
        if (project != null) {
            //TODO
            // make method Storage.getProjectName();
            // call it and return the project name;
            return project.getName();
        } else {
            return "Project \"X\"";
        }
    }

    @Override
    public String getDrawerOpenHeader() {
        if (project != null) {
            //TODO
            // make method Storage.getProjectName();
            // call it and return the project name;
            return project.getName() + " Menu";
        } else {
            return "Project \"X\" Menu";
        }
    }

    @Override
    public void initialize() {
        project =  Storage.getProjectById(context, projectId);
    }

    @Override
    public void handleIntent(Intent intent) {
        projectId = intent.getStringExtra(Constants.PROJECT_ID);
    }

    private View.OnClickListener deleteProject() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("Alert")
                        .content("Are you sure you would like to delete Project " + project.getName() + "?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Storage.deleteProject(context, projectId);
                                goBack();
                            }
                        })
                        .show();
            }
        };
        return listener;
    }

    private void goBack() {
        Log.i("bavigation", "up");
        this.onNavigateUp();
    }


    @Override
    public void setList(String id, List<String> strings) {
        if (id == SET_PROJECT_TRADES) {
            Storage.storeProjectTradeList(this, strings, projectId);
            done();
        }
    }

    @Override
    public void newSiteVisit(SiteVisit siteVisit) {
        Storage.storeSiteVisit(context,projectId, siteVisit);
        Storage.storeSiteVisitQuestions(context, siteVisit.getFieldValues());
        done();
    }

    private View.OnClickListener snackBarTest() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout layout  = (FrameLayout)findViewById(R.id.super_site_visit_main);
                Snackbar snackbar = Snackbar.make(layout, "project synced", Snackbar.LENGTH_SHORT);
                snackbar.setAction("undo", syncUndo());
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        };
    }

    private View.OnClickListener syncUndo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "sync undone, somehow", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
