package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import test.Fragments.SetListSelectionFragment;
import test.Network.FullSyncService;
import test.ProjectFragments.ActiveSiteVisits;
import test.ProjectFragments.NewSiteVisit;
import test.assesortron5.R;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.Project;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperProject extends NavDrawerActivityPrototype implements SetListSelectionFragment.SetListSelectionInterface {
    String projectId;
    Project project;
    final Context context = this;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(R.layout.header_site_walk_drawer, this);
        IconHeaderRecyclerAdapter.IconHeaderObject[] items = {
                adapter.newItem(this, "Active Site Vists" ,R.drawable.ic_list_items, ActiveSiteVisits.newInstance(projectId)),
                adapter.newItem(this, "New Site Visit",R.drawable.ic_new_item, NewSiteVisit.newInstance(this, projectId)),
                adapter.newItem(this, "Project Dashboard",R.drawable.ic_clipboard,new Fragment()),
                adapter.newItem(this, "Project Info",R.drawable.ic_info, new Fragment()),
                adapter.newItem(this, "Set Trades",R.drawable.ic_idk, SetListSelectionFragment.getInstance(this, Storage.getTradeList(this), Storage.getProjectTradeList(this, projectId))),
                adapter.newItem(this, "Submit Project",R.drawable.ic_idk, new Fragment()),
                adapter.newItem(this, "Sync", R.drawable.ic_idk,new Fragment()),
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
        AsyncTask<String, Void, Project> task = new AsyncTask<String, Void, Project>() {
            @Override
            protected Project doInBackground(String... strings) {
                return Storage.getProjectById(context, strings[0]);
            }

            @Override
            protected void onPostExecute(Project theProject) {
                project = theProject;
            }
        };
        task.execute(projectId);
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
                        .positiveText("OK")
                        .negativeText("No")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Storage.deleteProject(context, projectId);
                                Intent intent = new Intent(context, SuperUser.class);
                                startIntent(intent);
                            }
                        })
                        .show();
            }
        };
        return listener;
    }


    @Override
    public void setList(List<String> strings) {
        Storage.storeTradeList(this, strings, projectId);
        done();
    }
}
