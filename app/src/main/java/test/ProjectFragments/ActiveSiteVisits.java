package test.ProjectFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import test.UserFragments.ActiveProjects;
import test.adapters.SiteWalkListAdapter;
import test.assesortron5.R;
import test.objects.SiteVisit;
import test.persistence.Storage;

/**
 * Created by otf on 7/20/15.
 */
public class ActiveSiteVisits extends Fragment {
    String projectId;
    ListView listView;
    List<SiteVisit> siteVisits;

    public ActiveSiteVisits() {}

    public static ActiveSiteVisits newInstance(List<SiteVisit> siteVisits) {
        ActiveSiteVisits activeSiteVisits = new ActiveSiteVisits();
        activeSiteVisits.setSiteVisitList(siteVisits);
        return activeSiteVisits;
    }

    public static ActiveSiteVisits newInstance(String projectId) {
        ActiveSiteVisits activeSiteVisits = new ActiveSiteVisits();
        activeSiteVisits.setProjectId(projectId);
        return activeSiteVisits;
    }

    private void setSiteVisitList(List<SiteVisit> siteVisits) {
        this.siteVisits = siteVisits;
    }

    private void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);
        View view = inflater.inflate(R.layout.fragment_list, null);
        listView = (ListView)view.findViewById(R.id.list_fragment_list);
        setListView();
        return view;
    }

    @Override
    public void onResume() {
        Log.i("ActivesiteVisits", "onResume");
        siteVisits = null;
        setListView();
        super.onResume();
    }

    private void setListView() {
        AsyncTask<String, Void, List<SiteVisit>> task = new AsyncTask<String, Void, List<SiteVisit>>() {
            @Override
            protected List<SiteVisit> doInBackground(String... strings) {
                return Storage.getActiveSiteWalks(getActivity(), strings[0]);
            }

            @Override
            protected void onPostExecute(List<SiteVisit> siteVisitList) {
                siteVisits = siteVisitList;
                setListView();
            }
        };
        if (siteVisits == null) {
            task.execute(projectId);
        } else {
            SiteWalkListAdapter adapter = new SiteWalkListAdapter(getActivity(), siteVisits);
            listView.setAdapter(adapter);
        }
    }


}
