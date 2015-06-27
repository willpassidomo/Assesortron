package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class VisitSite extends Activity {
    private Context context;
    private String projectId;
    private SiteVisit siteVisit;
    private Button goWalkThrough, goDrawRequest, continueSiteWalk, sync, email, finishSiteWalk,back;
    private ViewSwitcher vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit);
        context = this;

        projectId = getIntent().getStringExtra(Constants.PROJECT_ID);

        String type = getIntent().getStringExtra(Constants.NEW_OR_EDIT);
        siteVisit = Storage.getSiteWalkById(this, getIntent().getStringExtra(Constants.SITE_VISIT_ID));

        setVariables();
        setListeners();

        if(type.equals(Constants.NEW)) {
            vs.showNext();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site_visit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVariables() {
        vs = (ViewSwitcher)findViewById(R.id.site_visit_view_switcher);

        continueSiteWalk = (Button)findViewById(R.id.site_visit_continue);
        sync = (Button)findViewById(R.id.site_visit_sync);
        email = (Button)findViewById(R.id.site_visit_email);
        finishSiteWalk = (Button)findViewById(R.id.site_visit_finish);
        back = (Button)findViewById(R.id.site_visit_back_but);

        goDrawRequest = (Button) findViewById(R.id.site_visit_draw_request);
        goWalkThrough = (Button) findViewById(R.id.site_visit_progress);

        goDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setListeners() {
        View.OnClickListener showNext = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vs.showNext();
            }
        };

        continueSiteWalk.setOnClickListener(showNext);
        back.setOnClickListener(showNext);

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"This feature is not yet implemented", Toast.LENGTH_LONG);
            }
        });

        finishSiteWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siteVisit.setActive(false);
                Storage.storeSiteVisit(context, siteVisit.getProjectId(), siteVisit);
                NavUtils.navigateUpFromSameTask((Activity)context);
                //TODO
                //need to provide sync service here!
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendProject.class);
                intent.putExtra(Constants.PROJECT_ID, projectId);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                startActivity(intent);
            }
        });

        goDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeDraw.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                intent.putExtra(Constants.PROJECT_ID, projectId);
                startActivity(intent);
            }
        });

        goWalkThrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Going to new progress","");
                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
                intent.putExtra(Constants.PROJECT_ID, projectId);
                startActivity(intent);
            }
        });
    }

}
