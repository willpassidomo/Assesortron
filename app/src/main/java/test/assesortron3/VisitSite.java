package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class VisitSite extends Activity {
    private Context context;
    private String projectId;
    private SiteVisit siteVisit;
    private Button goWalkThrough, goDrawRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit);
        context = this;

        projectId = getIntent().getStringExtra(Constants.PROJECT_ID);

        String type = getIntent().getStringExtra(Constants.NEW_OR_EDIT);

        if(type.equals(Constants.NEW)) {
            newSiteVisit();
        } else if (type.equals(Constants.EDIT)) {
            continueSiteVisit(getIntent().getStringExtra(Constants.SITE_VISIT_ID));
        } else {
            newSiteVisit();
            Toast.makeText(this, "*unrecognizeable type* \nnew site visit started", Toast.LENGTH_LONG);
        }

        setVariables();
        setListeners();

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
        goDrawRequest = (Button) findViewById(R.id.site_visit_draw_request);
        goWalkThrough = (Button) findViewById(R.id.site_visit_progress);

        goDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setListeners() {
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
                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
                intent.putExtra(Constants.PROJECT_ID, projectId);
                startActivity(intent);
            }
        });
    }


    private void newSiteVisit() {
        siteVisit = new SiteVisit(projectId);
        Storage.storeSiteVisit(this, projectId, siteVisit);
    }

    private void continueSiteVisit(String siteWalkId) {
        siteVisit = Storage.getSiteWalkById(this, siteWalkId);
    }
}
