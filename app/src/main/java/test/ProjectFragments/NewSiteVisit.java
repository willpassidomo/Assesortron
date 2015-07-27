package test.ProjectFragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import test.assesortron5.R;
import test.Fragments.SoftQuestionsFragment;
import test.assesortron5.VisitSite;
import test.objects.FieldValue;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;
import test.superActivities.FragmentDrawerListener;


public class NewSiteVisit extends Fragment implements SoftQuestionsFragment.DataListener {

    Button submit;
    SiteVisit siteWalk;
    NewSiteVisitListener parentListener;
    LinearLayout questionView;
    List<FieldValue> questions;
    boolean save;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        super.onCreateView(inflater, vg, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_site_visit, null);
        questionView = (LinearLayout)view.findViewById(R.id.new_site_visit_fv_list);
        if (questions != null) {
            setQuestions(questions);
        } else {
            getQuestions();
        }
        return view;
    }

    public NewSiteVisit() {}

    public static NewSiteVisit newInstance(NewSiteVisitListener listener, String projectId) {
        NewSiteVisit newSiteVisit = new NewSiteVisit();
        newSiteVisit.setParentListener(listener);
        newSiteVisit.setSiteVisit(projectId);
        return newSiteVisit;
    }

    public static NewSiteVisit newInstance(NewSiteVisitListener listener, List<FieldValue> fvs) {
        NewSiteVisit newSiteVisit = new NewSiteVisit();
        newSiteVisit.setParentListener(listener);
        newSiteVisit.setFieldValueData(fvs);
        return newSiteVisit;
    }

    private void setParentListener(NewSiteVisitListener listener) {
        this.parentListener = listener;
    }

    private void setSiteVisit(String projectId) {
        this.siteWalk = new SiteVisit(projectId);
    }

//        if (getIntent().getStringExtra(Constants.NEW_OR_EDIT).equals(Constants.EDIT)) {
//            String siteWalkId = getIntent().getStringExtra(Constants.SITE_VISIT_ID);
//            siteWalk = Storage.getSiteWalkById(this, siteWalkId);
//        } else {
//            siteWalk = new SiteVisit(getIntent().getStringExtra(Constants.PROJECT_ID));
//            Storage.storeSiteVisit(this, siteWalk.getProjectId(), siteWalk);
//        }
//
//        Constants.checkSiteWalkMinQuestions(this);
//        questions = Storage.getSiteWalkQuestions(this, siteWalk.getId());
//
//            Log.i("FV ownerId", fv.getField() + " - " + fv.getOwnerId());
//
//        }
//        setvariable();
//    }



    @Override
    public void onPause() {
        super.onPause();
        Storage.storeFieldValues(getActivity(), questions);
    }

    private void getQuestions() {
        AsyncTask<String, Void, List<FieldValue>> task = new AsyncTask<String, Void, List<FieldValue>>() {
            @Override
            protected List<FieldValue> doInBackground(String... strings) {
                return Storage.getSiteWalkQuestions(getActivity(), strings[0]);
            }

            @Override
            protected void onPostExecute(List<FieldValue> fieldValues) {
                setQuestions(fieldValues);
            }
        };
        task.execute(siteWalk.getId());
    }

    private void setQuestions(List<FieldValue> fieldValues) {
        questions = fieldValues;
        for (FieldValue fv: questions) {
            if (fv.getOwnerId() == null || fv.getOwnerId().equals("")) {
                fv.setOwnerId(siteWalk.getId());
            }
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.new_site_visit_fv_list, SoftQuestionsFragment.newInstance(this, questions));
        ft.commit();
    }

    @Override
    public void setFieldValueData(List<FieldValue> fvs) {
        questions = fvs;
//        Log.i("set field value data","NEWSITEVISIT");
//        questions = fvs;
//        if(save) {
//            Log.i("save data", "");
//            for (FieldValue fv: fvs) {
//                if (fv.getIn()) {
//                    Log.i("Field/V", fv.getField() + " - "+fv.getValue() + " " + fv.getId());
//                }
//            }
//            Storage.storeFieldValues(getActivity(), questions);
//            fvs = Storage.getSiteWalkQuestions(getActivity(), siteWalk.getId());
//            for (FieldValue fv: fvs) {
//                if (fv.getIn()) {
//                    Log.i("FV from database", fv.getField() + " - "+fv.getValue() + " " + fv.getId());
//                }
//            }
//        }
    }

    @Override
    public void finishFieldValues() {
        siteWalk.setFieldValues(questions);
        parentListener.newSiteVisit(siteWalk);
    }

    public interface NewSiteVisitListener {
        public void newSiteVisit(SiteVisit siteVisit);
    }
}

