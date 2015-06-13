package test.assesortron3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import test.adapters.projectListAdapter;
import test.objects.Project;
import test.persistence.Constants;
import test.persistence.Storage;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        ///TEST
//        DataBaseStorage db = new DataBaseStorage(this);
//        db.deleteProjects();
//        Storage.storeTestProject(this, new Project());
//        ///

        populateActiveProjects();

//        Button yourBtn = (Button) this.findViewById.(R.id.new_project);
//
//        int btnSize=yourBtn.getLayoutParams().width;
//        yourBtn.setLayoutParams(new LayoutParams(btnSize, btnSize));
    }

    @Override
    public void onResume() {
        super.onResume();
        populateActiveProjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
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

    public void goNewProject(View view) {
        Intent intent = new Intent(this, ProjectDashboard.class);
        intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
        startActivity(intent);
    }

    public void populateActiveProjects() {
        //TODO

        ListView projectList = (ListView) this.findViewById(R.id.project_list);

        List<Project> projects = Storage.getActiveProjects(this);
        projectListAdapter pla = new projectListAdapter(this, projects);
        projectList.setAdapter(pla);
        for(Project project:projects) {
            Log.i("Project found", project.getName() + " " + project.getId());
        }
    }

}
