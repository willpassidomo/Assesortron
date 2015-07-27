package test.SiteVisitFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.Fragments.SoftQuestionsFragment;
import test.ProjectFragments.NewSiteVisit;
import test.assesortron5.R;
import test.objects.FieldValue;
import test.objects.SiteVisit;
import test.persistence.Storage;

/**
 * Created by otf on 7/26/15.
 */
public class SiteVisitDashboard extends Fragment implements SiteVisitFieldValuesViewList.SiteVisitFieldValuesListListener, SoftQuestionsFragment.DataListener {
    private SiteVisit siteVisit;
    private int fragment;

    public SiteVisitDashboard() {}

    public static SiteVisitDashboard newInstance(SiteVisit siteVisit) {
        SiteVisitDashboard siteVisitDashboard = new SiteVisitDashboard();
        siteVisitDashboard.setSiteVisit(siteVisit);
        return siteVisitDashboard;
    }

    private void setSiteVisit(SiteVisit siteVisit) {
        this.siteVisit = siteVisit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_blank_manager, null);
        fragment = R.id.fragment_draw_request_items_manager_main;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        getFragmentManager()
                .beginTransaction()
                .add(fragment, SiteVisitFieldValuesViewList.newInstance(this, siteVisit.getFieldValues()))
                .commit();
    }

    @Override
    public void editList(List<FieldValue> fvs) {
        getFragmentManager()
                .beginTransaction()
                .replace(fragment, SoftQuestionsFragment.newInstance(this, siteVisit.getFieldValues()))
                .commit();
    }

    @Override
    public void finishFieldValues() {
        Storage.storeSiteVisitQuestions(getActivity(), siteVisit.getFieldValues());
        getFragmentManager()
                .beginTransaction()
                .replace(fragment, SiteVisitFieldValuesViewList.newInstance(this, siteVisit.getFieldValues()))
                .commit();
    }

    @Override
    public void setFieldValueData(List<FieldValue> fvs) {
        siteVisit.setFieldValues(fvs);
    }
}
