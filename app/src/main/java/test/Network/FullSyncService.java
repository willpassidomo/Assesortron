package test.Network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import assesortron.assesortronTaskerAPI.model.StringWrapper;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FullSyncService extends IntentService {
    public static final String OUT_BOUND_MESSAGE = "outbound_message";
    public static final String ERROR_MESSAGE = "ERROR: sync was unsuccessful";

    Context context;

    public FullSyncService() {
        super("FullSyncService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        context = this;
        switch (intent.getStringExtra(Constants.ID_TYPE)) {
            case Constants.TYPE_PROJECT:
                syncProject(intent.getStringExtra(Constants.PROJECT_ID));
                break;
            case Constants.TYPE_SITEVISIT:
                syncSiteVisit(intent.getStringExtra(Constants.SITE_VISIT_ID));
                break;
            default:
                syncAll();
        }
    }

    private void syncProject(String projectId) {
        //TODO
        //see how this works with syncing sitevisits, in regards to the FieldValue objects,
        //look to change the project questions to FieldValues
    }

    private void syncSiteVisit(String siteVisitId) {
        Toast.makeText(context, "syncing with server...", Toast.LENGTH_SHORT).show();
        final SiteVisit siteVisit = Storage.getSiteWalkById(this, siteVisitId);
        StringWrapper string = null;
        try {
            string = AppConstants.getAssServiceHandle().taskerAPI().submitSiteWalk(siteVisit.getDTO(this)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Constants.BROADCAST_SYNC_SERVICE_RESPONSE)
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .putExtra(OUT_BOUND_MESSAGE, string != null ? string.getString() : ERROR_MESSAGE);
            sendBroadcast(broadcastIntent);
            Log.i("broadcast sent","");
        }
    }

    private void syncAll() {

    }

}