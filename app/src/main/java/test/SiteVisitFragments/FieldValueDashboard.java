package test.SiteVisitFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.Fragments.SoftQuestionsFragment;
import test.assesortron5.R;
import test.objects.FieldValue;
import test.persistence.Storage;

/**
 * Created by otf on 7/26/15.
 */
public class FieldValueDashboard extends Fragment implements SiteVisitFieldValuesViewList.SiteVisitFieldValuesListListener, SoftQuestionsFragment.DataListener {
    private List<FieldValue> fieldValues;
    String id;
    private int fragment;

    public FieldValueDashboard() {}

    public static FieldValueDashboard newInstance(List<FieldValue> fieldValues, String id) {
        FieldValueDashboard fieldValueDashboard = new FieldValueDashboard();
        fieldValueDashboard.setFieldValues(fieldValues);
        fieldValueDashboard.setId(id);
        return fieldValueDashboard;
    }

    private void setFieldValues(List<FieldValue> fieldValues) {
        this.fieldValues = fieldValues;
    }

    private void setId(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
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
                .add(fragment, SiteVisitFieldValuesViewList.newInstance(this, fieldValues))
                .commit();
    }

    @Override
    public void editList(List<FieldValue> fvs) {
        getFragmentManager()
                .beginTransaction()
                .replace(fragment, SoftQuestionsFragment.newInstance(this, fieldValues, id))
                .commit();
    }

    @Override
    public void finishFieldValues(List<FieldValue> fvs) {
        fieldValues = fvs;
        Storage.storeFieldValues(getActivity(), fieldValues);
        getFragmentManager()
                .beginTransaction()
                .replace(fragment, SiteVisitFieldValuesViewList.newInstance(this, fieldValues))
                .commit();
    }

    @Override
    public void setFieldValueData(List<FieldValue> fvs) {
        fieldValues = fvs;
    }
}
