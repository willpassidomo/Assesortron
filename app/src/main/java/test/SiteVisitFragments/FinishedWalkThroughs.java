package test.SiteVisitFragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

import test.adapters.WalkThroughRecyclerAdapter;
import test.assesortron5.R;
import test.objects.WalkThrough;
import test.persistence.Storage;

/**
 * Created by otf on 7/29/15.
 */
public class FinishedWalkThroughs extends Fragment {
    private String siteWalkId;
    private List<WalkThrough> walkThroughs;
    private RecyclerView recyclerView;
    private boolean rendered = false;

    public FinishedWalkThroughs() {}

    public static FinishedWalkThroughs newInstance(String siteWalkId) {
        FinishedWalkThroughs finishedWalkThroughs = new FinishedWalkThroughs();
        finishedWalkThroughs.setSiteWalkId(siteWalkId);
        return finishedWalkThroughs;
    }

    private void setSiteWalkId(String siteWalkId) {
        this.siteWalkId = siteWalkId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_recycler_list, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.list_fragment_recycler);
        setListView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setListView();
    }

    private void setListView() {
        AsyncTask<String, Void, List<WalkThrough>> getWalkThroughs = new AsyncTask<String, Void, List<WalkThrough>>() {
            @Override
            protected List<WalkThrough> doInBackground(String... strings) {
                return Storage.getWalkThroughBySiteWalkId(getActivity(), strings[0]);
            }

            @Override
            protected void onPostExecute(List<WalkThrough> walkThroughs) {
                renderWalkThroughs(walkThroughs);
            }
        };

        if (siteWalkId != null) {
            getWalkThroughs.execute(siteWalkId);
        }
    }

    private void renderWalkThroughs(List<WalkThrough> walkThroughs) {
        Log.i("FINISHED WALK THROUGHS", "Rendering walkthorughs");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WalkThroughRecyclerAdapter(getActivity(), walkThroughs, siteWalkId));
    }
}
