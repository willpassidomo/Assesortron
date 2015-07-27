package test.SiteVisitFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import test.assesortron5.R;
import test.objects.FieldValue;

/**
 * Created by otf on 7/26/15.
 */
public class SiteVisitFieldValuesViewList extends Fragment {
    SiteVisitFieldValuesListListener parentListener;
    ListView listView;
    Button edit;
    List<FieldValue> fieldValueList;

    public SiteVisitFieldValuesViewList() {}

    public static SiteVisitFieldValuesViewList newInstance(SiteVisitFieldValuesListListener parentListener, List<FieldValue> fvs) {
        SiteVisitFieldValuesViewList siteVisitFieldValuesViewList = new SiteVisitFieldValuesViewList();
        siteVisitFieldValuesViewList.setParentListener(parentListener);
        siteVisitFieldValuesViewList.setFieldValues(fvs);
        return siteVisitFieldValuesViewList;
    }

    private void setParentListener(SiteVisitFieldValuesListListener parentListener) {
        this.parentListener = parentListener;
    }

    private void setFieldValues(List<FieldValue> fvs) {
        this.fieldValueList = fvs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        View view = inflater.inflate(R.layout.fragment_site_visit_questions_view, null);
        edit = (Button)view.findViewById(R.id.site_visit_question_list_edit);
        edit.setOnClickListener(editOnClickListener());
        listView = (ListView)view.findViewById(R.id.site_visit_question_view_list);
        listView.setAdapter(new QuestionsAdapter(getActivity(), fieldValueList));
        return view;
    }

    private View.OnClickListener editOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentListener.editList(fieldValueList);
            }
        };
    }

    public class QuestionsAdapter extends BaseAdapter{
        Context context;
        List<FieldValue> fvs;

        public QuestionsAdapter(Context context, List<FieldValue> fvs) {
            this.context = context;
            this.fvs = fvs;
        }


        @Override
        public int getCount() {
            return fvs.size();
        }

        @Override
        public Object getItem(int i) {
            return fvs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.list_field_value, null);
            }
            TextView field = (TextView)view.findViewById(R.id.field_value_list_field);
            TextView value = (TextView)view.findViewById(R.id.field_value_list_value);

            FieldValue fv = fvs.get(i);

            field.setText(fv.getField());
            value.setText(fv.getValue());
            return view;
        }
    }

    public interface SiteVisitFieldValuesListListener {
        public void editList(List<FieldValue> fvs);
    }
}
