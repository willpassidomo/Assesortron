package test.assesortron3;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.objects.FieldValue;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class NewSiteVisit extends Activity implements SoftQuestionsFragment.DataListener {

    Context context;
    Button submit;
    SiteVisit siteWalk;
    LinearLayout questionView;
    List<FieldValue> questions;
    boolean save;
    ScheduledThreadPoolExecutor timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
            if (fv.getOwnerId() == null || fv.getOwnerId().equals("")) {
                fv.setOwnerId(siteWalk.getId());
            }
            Log.i("FV ownerId", fv.getField() + " - " + fv.getOwnerId());

        }
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
        questionView = (LinearLayout)findViewById(R.id.new_site_visit_fv_list);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.new_site_visit_fv_list, SoftQuestionsFragment.newInstance(this, questions));
        ft.commit();

        submit = (Button)findViewById(R.id.new_site_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage.storeSiteVisitQuestions(context, questions);
                Intent intent = new Intent(context, VisitSite.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteWalk.getId());
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
                startActivity(intent);
            }
        });
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

