package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import test.adapters.SiteWalkListAdapter;
import test.objects.Project;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class ProjectHomeScreen extends Activity {
    Context context;
    Project project;
    Button goProjectDash, goProjectHistory, goNewSiteVisit;
    ListView siteWalkInProgress;
    TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home_screen2);
        context = this;

        Intent intent = getIntent();
        String projectId = intent.getStringExtra(Constants.PROJECT_ID);
        project = Storage.getProjectById(this, projectId);

        Log.i("project ID", project.getId());

        setVariables();
        setValues();
        setListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_home_screen, menu);

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
        if (id == R.id.overflow_set_trades) {
            Intent intent = new Intent(context, StringListEdit.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }

    private void setVariables() {
        projectName = (TextView) findViewById(R.id.project_home_project_name);
        goProjectDash = (Button) findViewById(R.id.project_home_view_dashboard);
        goProjectHistory = (Button) findViewById(R.id.project_home_project_history);
        goNewSiteVisit = (Button) findViewById(R.id.project_home_new_site_visit);
        siteWalkInProgress = (ListView) findViewById(R.id.project_home_current_site_visits);
    }

    private void setValues() {
        projectName.setText(project.getName());
        List<SiteVisit> siteWalksInProgreses = Storage.getActiveSiteWalks(this, project.getId());
        Log.i("get sitewalks for", "project id- " + project.getId());
        SiteWalkListAdapter swla = new SiteWalkListAdapter(this, siteWalksInProgreses);
        siteWalkInProgress.setAdapter(swla);
    }

    private void setListeners() {
       goProjectDash.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, ProjectDashboard.class);
               intent.putExtra(Constants.NEW_OR_EDIT, Constants.EDIT);
               intent.putExtra(Constants.PROJECT_ID, project.getId());
               startActivity(intent);
           }
       });

       goProjectHistory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context,History.class);
               intent.putExtra(Constants.PROJECT_ID, project.getId());
               startActivity(intent);
           }
       });

       goNewSiteVisit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, NewSiteVisit.class);
               intent.putExtra(Constants.PROJECT_ID, project.getId());
               intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
               startActivity(intent);
           }
       });
    }

    public void goWalkthrough(View view) {
        Intent intent = new Intent(this, SiteWalkthrough.class);
        intent.putExtra("id", project.getId());
        intent.putExtra("new_or_edit", "new");
        startActivity(intent);
    }




    public void goEmailProject(View view) {
        Intent intent = new Intent(this, SendProject.class);
        intent.putExtra("id", project.getId());
        startActivity(intent);
    }
}
