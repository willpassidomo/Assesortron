package test.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import test.adapters.DrawRequestItemManagerListAdapter;
import test.assesortron5.R;
import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.persistence.Storage;

/**
 * Created by otf on 7/15/15.
 */
public class DrawRequestItemsFragment extends Fragment {
    private String siteWalkId;
    private DrawRequest drawRequest;
    private ListView listView;
    boolean rendered = false;

    public DrawRequestItemsFragment() {
        super();
    }

    public static DrawRequestItemsFragment newInstance(String siteWalkId) {
        DrawRequestItemsFragment drif = new DrawRequestItemsFragment();
        drif.setSiteWalkId(siteWalkId);
        return drif;
    }

    public static DrawRequestItemsFragment newInstance(String sitetWalkId, DrawRequest dr) {
        DrawRequestItemsFragment drif = new DrawRequestItemsFragment();
        drif.setSiteWalkId(sitetWalkId);
        drif.setDrawRequestItems(dr);
        return drif;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);

        View view = inflater.inflate(R.layout.fragment_draw_request_items_manager, vg);
        listView = (ListView)view.findViewById(R.id.draw_request_manage_item_list);
        getDrawRequest();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        if (!rendered) {
            renderDrawRequestItems();
        }
    }


    private void setSiteWalkId(String siteWalkId) {
        this.siteWalkId = siteWalkId;
    }

    private void getDrawRequest() {
        AsyncTask<String, Void, DrawRequest> getDrawRequestItems = new AsyncTask<String, Void, DrawRequest>() {
            @Override
            protected DrawRequest doInBackground(String... strings) {
                return Storage.getDrawRequestBySiteWalkId(getActivity(), siteWalkId);
            }

            @Override
            protected void onPostExecute(DrawRequest dr) {
                setDrawRequestItems(dr);
                renderDrawRequestItems();
            }
        };
        getDrawRequestItems.execute(siteWalkId);
    }

    private void setDrawRequestItems(DrawRequest items) {
        drawRequest = items;
    }

    private void renderDrawRequestItems() {
        rendered = true;
        DrawRequestItemManagerListAdapter adapter = new DrawRequestItemManagerListAdapter(getActivity(),
                DrawRequestItem.DRAW_REQUEST_ITEM_TYPES,
                drawRequest.getDrawRequestItemLists());
        listView.setAdapter(adapter);
    }


}
