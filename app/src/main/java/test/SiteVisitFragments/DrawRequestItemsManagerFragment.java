package test.SiteVisitFragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.Fragments.DrawRequestItemList;
import test.assesortron5.R;
import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.objects.WalkThrough;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/15/15.
 */
public class DrawRequestItemsManagerFragment extends Fragment implements DrawRequestItemOverview.DrawRequestItemOverviewListner, DrawRequestItemList.DrawRequestItemListListener, MakeDrawRequestItem.DrawRequestItemCreatorListner {
    private String siteWalkId;
    private DrawRequest drawRequest;
    boolean rendered = false;

    public DrawRequestItemsManagerFragment() {
        super();
    }

    public static DrawRequestItemsManagerFragment newInstance(String siteWalkId) {
        DrawRequestItemsManagerFragment drif = new DrawRequestItemsManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SITE_VISIT_ID, siteWalkId);
        drif.setArguments(bundle);
        return drif;
    }

    public static DrawRequestItemsManagerFragment newInstance(String sitetWalkId, DrawRequest dr) {
        DrawRequestItemsManagerFragment drif = new DrawRequestItemsManagerFragment();
        drif.setSiteWalkId(sitetWalkId);
        drif.setDrawRequestItems(dr);
        return drif;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setRetainInstance(true);
        siteWalkId = getArguments().getString(Constants.SITE_VISIT_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);
        View view = inflater.inflate(R.layout.fragment_blank_manager, null);
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
        if (drawRequest == null) {
            getDrawRequestItems.execute(siteWalkId);
        }
    }

    private void setDrawRequestItems(DrawRequest items) {
        drawRequest = items;
    }

    private void renderDrawRequestItems() {
            Log.i("DR Item manager", "Rendering Overview");
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_draw_request_items_manager_main, DrawRequestItemOverview.newInstance(this, drawRequest))
                    .commit();
    }

    @Override
    public void editEntry(String drawRequestItemId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_draw_request_items_manager_main, MakeDrawRequestItem.newInstance(this, drawRequestItemId));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void submitDrawRequestItem(DrawRequestItem drawRequestItem) {
        //TODO

        drawRequest.addDrawRequestItem(drawRequestItem);
        Storage.storeDrawRequestItem(getActivity(), drawRequest.getId(), drawRequestItem);
        getActivity().onBackPressed();
    }

    @Override
    public void addItem(String type) {
        Log.i("Add Item", type);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_draw_request_items_manager_main, MakeDrawRequestItem.newInstance(this, type));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void listType(String type) {
        Log.i("View Item List", type);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_draw_request_items_manager_main, DrawRequestItemList.newInstance(null, drawRequest.getItemList(type)));
        ft.addToBackStack(null);
        ft.commit();
    }


}
