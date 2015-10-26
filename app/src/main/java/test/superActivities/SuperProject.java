package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import test.Fragments.SetListSelectionFragment;
import test.ProjectFragments.ActiveSiteVisits;
import test.ProjectFragments.FinishedSiteVisits;
import test.ProjectFragments.NewSiteVisit;
import test.SiteVisitFragments.FieldValueDashboard;
import test.UserFragments.StringListEdit;
import test.assesortron5.R;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.FieldValue;
import test.objects.Project;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/16/15.
 */
public class SuperProject extends NavDrawerActivityPrototype implements SetListSelectionFragment.SetListSelectionInterface, NewSiteVisit.NewSiteVisitListener, StringListEdit.StringListEditCallback {
    public static final String SET_PROJECT_TRADES = "set_project_trades";
    private static final int SITEVISIT_QUESTIONS = 1;
    private String userId;
    String projectId;
    Project project;
    final Context context = this;
    private List<String> siteVisitQuestions;
    FieldValueDashboard fieldValueDashboard;

    NewSiteVisit newSiteVisit;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_super_project, menu);
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
            deleteProject();
            return true;
        }
        return false;
    }

    private View headerView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.header_project_drawer, parent, false);
        TextView name = (TextView)view.findViewById(R.id.header_project_drawer_name);
        TextView address = (TextView)view.findViewById(R.id.header_project_address);
        final ImageView image = (ImageView)view.findViewById(R.id.header_projec_image);
//        ViewTreeObserver vto = image.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.i("Image width and h" ,image.getWidth() + "w, " +image.getHeight() + "h");
//            }
//        });
        if (project.getPictures() != null && project.getPictures().size() > 0 && project.getPicture(0) != null) {
            Bitmap bitmap = Storage.getPictureByOwnerId(this, project.getPicture(0));

            if (bitmap.getHeight() > bitmap.getWidth()) {
                Toast.makeText(this, "Rotating image", Toast.LENGTH_SHORT).show();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            image.setImageBitmap(bitmap);
        }
        name.setText(project.getName());
        address.setText(project.getAddress().getFullAddress());
        Log.i("done with view","");
        return view;
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(this) {
            @Override
            public View inflateHeader(LayoutInflater inflater, ViewGroup parent) {
               return headerView(inflater, parent);
            }
        };
        newSiteVisit = NewSiteVisit.newInstance(this, projectId);
        fieldValueDashboard = FieldValueDashboard.newInstance(Storage.getQuestionsByOwnerId(this, projectId), projectId);
        IconHeaderRecyclerAdapter.ListItem[] items = {
                adapter.newItem(this, "Active Site Vists" ,R.drawable.ic_list_items, ActiveSiteVisits.newInstance(projectId, userId)),
                adapter.newItem(this, "New Site Visit",R.drawable.ic_new_item, newSiteVisit),
                adapter.newItem(this, "Project Dashboard",R.drawable.ic_clipboard, fieldValueDashboard),
                adapter.newItem(this, "Project Info", R.drawable.ic_info, new Fragment()),
                adapter.newItem(this, "Finished Site Visits", R.drawable.ic_file_folder, FinishedSiteVisits.newInstance(projectId)),
                adapter.newItem(this, "Set Trades",R.drawable.ic_idk, SetListSelectionFragment.getInstance(this, Storage.getTradeList(this), Storage.getProjectTradeList(this, projectId), SET_PROJECT_TRADES)),
                adapter.newItem(this, "SiteVisit Questions",R.drawable.ic_idk, StringListEdit.newInstance(this, SITEVISIT_QUESTIONS, siteVisitQuestions)),
                adapter.newItem(this, "Leave Project", R.drawable.ic_back_arrow, leaveProject())
        };
        adapter.setItems(items);
        return adapter;
    }

    @Override
    public String getDrawerClosedHeader() {
        if (project != null) {
            return project.getName() + " Project";
        } else {
            return "Project \"X\"";
        }
    }

    @Override
    public String getDrawerOpenHeader() {
        if (project != null) {
            return project.getName() + " Menu";
        } else {
            return "Project \"X\" Menu";
        }
    }

    @Override
    public void initialize() {
        project =  Storage.getProjectById(context, projectId);
        siteVisitQuestions = new ArrayList<>();
        List<FieldValue> fvs = Storage.getSiteWalkQuestions(this, projectId);
        for (FieldValue fv: fvs) {
            siteVisitQuestions.add(fv.getField());
        }
    }

    @Override
    public void handleIntent(Intent intent) {
        projectId = intent.getStringExtra(Constants.PROJECT_ID);
        userId = intent.getStringExtra(Constants.USER_ID);
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

    private View.OnClickListener leaveProject() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
    }

    @Override
    public void deleteString(int type, String string) {
        Storage.deleteSiteWalkQuestion(this, string);
    }

    @Override
    public void addString(int type, String string) {
        Storage.storeSiteVisitQuestion(context, new FieldValue(string, true));
    }
}
