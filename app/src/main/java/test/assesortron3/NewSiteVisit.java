package test.assesortron3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.adapters.SoftQuestionListAdapter;
import test.objects.FieldValue;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class NewSiteVisit extends Activity implements SoftQuestionListAdapter.DataListener {

    SiteVisit siteWalk;
    ListView listView;
    List<FieldValue> questions;
    SoftQuestionListAdapter sqla;
    boolean save;
    ScheduledThreadPoolExecutor timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_site_visit);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getStringExtra(Constants.NEW_OR_EDIT).equals(Constants.EDIT)) {
            String siteWalkId = getIntent().getStringExtra(Constants.SITE_VISIT_ID);
            siteWalk = Storage.getSiteWalkById(this, siteWalkId);
        } else {
            siteWalk = new SiteVisit(getIntent().getStringExtra(Constants.PROJECT_ID));
            Storage.storeSiteVisit(this, siteWalk.getProjectId(), siteWalk);
        }

        Constants.checkSiteWalkMinQuestions(this);
        questions = Storage.getSiteWalkQuestions(this, siteWalk.getId());
        for (FieldValue fv: questions) {
            if (fv.getIn()) {
                Log.i("FV from database", fv.getField() + " - "+fv.getValue());
            }
        }

        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        setSave();
                    }
                },
                0,
                5,
                TimeUnit.SECONDS);

        setvariable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_site_visit, menu);
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

        if(id == android.R.id.home) {
            Storage.storeSiteVisitQuestions(this, questions);
            Intent intent = new Intent(this, SiteWalkthrough.class);
            intent.putExtra(Constants.SITE_VISIT_ID, siteWalk.getId());
            intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
            startActivity(intent);
        }

            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        Storage.storeFieldValues(this, questions);
    }

    private void setvariable() {
        listView = (ListView)findViewById(R.id.new_site_visit_fv_list);
        sqla = new SoftQuestionListAdapter(this, questions, this);
        listView.setAdapter(sqla);
    }

    public void setSave() {
        save = true;
    }

    @Override
    public void setFieldValueData(List<FieldValue> fvs) {
        Log.i("set field value data","NEWSITEVISIT");
        questions = fvs;
        if(save) {
            Log.i("save data", "");
            for (FieldValue fv: fvs) {
                if (fv.getIn()) {
                    Log.i("Field/V", fv.getField() + " - "+fv.getValue() + " " + fv.getId());
                }
            }
            Storage.storeFieldValues(this, questions);
            fvs = Storage.getSiteWalkQuestions(this, siteWalk.getId());
            for (FieldValue fv: fvs) {
                if (fv.getIn()) {
                    Log.i("FV from database", fv.getField() + " - "+fv.getValue() + " " + fv.getId());
                }
            }
        }
    }
}

