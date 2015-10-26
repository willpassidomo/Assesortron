package test.assesortron5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import test.Network.FullSyncService;
import test.ProjectFragments.NewSiteVisit;
import test.SiteVisitFragments.SiteWalkthrough;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;


public class VisitSite extends Activity {
    SyncBroadcastReciever receiver;
    private Context context;
    private String projectId;
    private SiteVisit siteVisit;
    private Button goWalkThrough, goSiteWalkDash, goSiteWalkWalkThroughHist, goDrawRequest, continueSiteWalk, sync, email, finishSiteWalk, back;
    private ViewSwitcher vs;
    private Toast startingProgressToast;

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
        setRecievers();

        if (type.equals(Constants.NEW)) {
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

    @Override
    public void onResume() {
        super.onResume();
        setRecievers();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ProjectHomeScreen", "reciever unregistered");
        unregisterReceiver(receiver);
    }

    private void setVariables() {
        vs = (ViewSwitcher) findViewById(R.id.site_visit_view_switcher);

        continueSiteWalk = (Button) findViewById(R.id.site_visit_continue);
        sync = (Button) findViewById(R.id.site_visit_sync);
        email = (Button) findViewById(R.id.site_visit_email);
        finishSiteWalk = (Button) findViewById(R.id.site_visit_finish);
        back = (Button) findViewById(R.id.site_visit_back_but);

        goDrawRequest = (Button) findViewById(R.id.site_visit_draw_request);
        goWalkThrough = (Button) findViewById(R.id.site_visit_progress);
        goSiteWalkDash = (Button) findViewById(R.id.site_visit_dashboard);
        goSiteWalkWalkThroughHist = (Button) findViewById(R.id.site_visit_progress_history);

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
                startingProgressToast = Toast.makeText(context, "syncing site visit", Toast.LENGTH_LONG);
                startingProgressToast.show();
                Intent intent = new Intent(context, FullSyncService.class);
                intent.putExtra(Constants.ID_TYPE, Constants.TYPE_SITEVISIT);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                startService(intent);
            }
        });

        finishSiteWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siteVisit.setActive(false);
                Storage.storeSiteVisit(context, siteVisit.getProjectId(), siteVisit);
                NavUtils.navigateUpFromSameTask((Activity) context);
                //TODO
                //need to provide sync service here!
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m_Text;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter Email address");

// Set up the input
                final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startingProgressToast = Toast.makeText(context, "Emailing site visit to " + input.getText().toString(), Toast.LENGTH_LONG);
                        startingProgressToast.show();
                        Intent intent = new Intent(context, FullSyncService.class);
                        intent.putExtra(Constants.ID_TYPE, Constants.TYPE_SITEVISIT);
                        intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                        intent.putExtra(Constants.EMAIL_ADDRESS, input.getText().toString());
                        startService(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        goDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, MakeDraw.class);
//                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
//                intent.putExtra(Constants.PROJECT_ID, projectId);
//                startActivity(intent);
            }
        });

        goWalkThrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Going to new progress", "");
                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.NEW);
                intent.putExtra(Constants.PROJECT_ID, projectId);
                startActivity(intent);
            }
        });

        goSiteWalkDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewSiteVisit.class);
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.EDIT);
                intent.putExtra(Constants.SITE_VISIT_ID, siteVisit.getId());
                startActivity(intent);
            }
        });

        goSiteWalkWalkThroughHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Not yet Implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecievers() {
        IntentFilter filter = new IntentFilter(FullSyncService.BROADCAST_SYNC_SERVICE_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        if (receiver == null) {
            receiver = new SyncBroadcastReciever();
        }
        registerReceiver(receiver, filter);
        Log.i("ProjectHomeScreen", "reciever Registered");

    }

    class SyncBroadcastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {
            Log.i("Broadcast recieved", "");
            String message = intent.getStringExtra(FullSyncService.OUT_BOUND_MESSAGE);
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                startingProgressToast.cancel();
                Toast.makeText(context, "blank broadcast recieved", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
