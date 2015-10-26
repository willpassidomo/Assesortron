package test.ProjectFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import test.adapters.SiteWalkListAdapter;
import test.assesortron5.R;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/27/15.
 */
public class FinishedSiteVisits extends Fragment {
    String projectId;
    ListView listView;

    public FinishedSiteVisits() {}

    public static FinishedSiteVisits newInstance(String projectId) {
        FinishedSiteVisits finishedSiteVisits = new FinishedSiteVisits();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PROJECT_ID, projectId);
        finishedSiteVisits.setArguments(bundle);
        return finishedSiteVisits;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        projectId = getArguments().getString(Constants.PROJECT_ID);
        listView = (ListView)view.findViewById(R.id.list_fragment_list);

        setListView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setListView();
    }

    private void setListView() {
        List<SiteVisit> siteVisits = Storage.getFinishedSiteWalks(getActivity(), projectId);
        SiteWalkListAdapter siteWalkListAdapter = new SiteWalkListAdapter(getActivity(), siteVisits, "no finished Site Visits") {
            @Override
            public View.OnClickListener itemClickListener(final SiteVisit sv) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(getActivity())
                                .title("Reopen SiteVisit " + sv.getStartDate() + "?")
                                .content("press YES to make Site Visit active, press NO to cancel")
                                .positiveText("Yes")
                                .negativeText("No")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        sv.setActive(true);
                                        Storage.storeSiteVisit(getActivity(), projectId, sv);
                                        setListView();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                };
            }
        };
        listView.setAdapter(siteWalkListAdapter);
    }
}
