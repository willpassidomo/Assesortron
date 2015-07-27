package test.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import test.assesortron5.R;
import test.objects.FieldValue;

/**
 * Created by otf on 7/9/15.
 */
public class SoftQuestionsFragment extends Fragment {
    private FieldValue fvLead = new FieldValue("select field..", false);
    DataListener dl;
    Button submit;

    List<FieldValue> in;
    List<FieldValue> out;

    IndexLinearLayout softFieldContainer;

    public SoftQuestionsFragment() {
        super();
    }

    public static SoftQuestionsFragment newInstance(DataListener dl, List<FieldValue> fieldValues) {
        SoftQuestionsFragment sqf = new SoftQuestionsFragment();
        sqf.setDataListener(dl);
        sqf.assignFieldValues(fieldValues);
        return sqf;
    }

    private void setDataListener(DataListener dl) {
        this.dl = dl;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedArgs) {
        View v = inflater.inflate(R.layout.fragment_soft_fields, null);
        softFieldContainer = new IndexLinearLayout((LinearLayout)v.findViewById(R.id.soft_fields_container));
        submit = (Button) v.findViewById(R.id.soft_field_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.finishFieldValues();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedArgs) {
        Log.i("OnViewCreated", "" + in.size() + ", " + out.size());
        for (FieldValue fv: in) {
            softFieldContainer.addFieldValue(listItemView(fv), fv);
        }
        softFieldContainer.addControlBlock(controlBlockView());
    }

    private void assignFieldValues(List<FieldValue> fvs) {
        in = new ArrayList<>();
        out = new ArrayList<>();

        for (FieldValue fv: fvs) {
            if (fv.getIn()) {
                in.add(fv);
            } else {
                out.add(fv);
            }
        }
    }

    private View listItemView(final FieldValue fv) {
        LayoutInflater infaltor = (LayoutInflater)getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = infaltor.inflate(R.layout.soft_q_field_val, null);
        TextView field = (TextView) view.findViewById(R.id.soft_q_field);
        EditText value = (EditText) view.findViewById(R.id.soft_q_value);
        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.soft_q_switch_remove);
        final Button remove = (Button) view.findViewById(R.id.soft_q_remove_f);
        RelativeLayout noRemove = (RelativeLayout) view.findViewById(R.id.soft_q_no_remove_f);

        field.setText(fv.getField());
        value.setText(fv.getValue());

        field.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!fv.getRequired()) {
                    vs.showNext();
                }
                return true;
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fv.getRequired()) {
                    removeFieldValue(fv);
                    vs.showNext();
                    view.invalidate();
                } else {
                    Toast.makeText(getActivity(), "This Field is required and may not be removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        noRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
            }
        });

        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                fv.setValue(s.toString());
                notifyDataListener();
            }
        });

        return view;
    }

    private View controlBlockView() {
        LayoutInflater infaltor = (LayoutInflater)getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = infaltor.inflate(R.layout.soft_q_control_block, null);
        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.soft_q_view_switcher);
        Button addQ = (Button) view.findViewById(R.id.soft_q_add_q);
        final Spinner newQuestion = (Spinner) view.findViewById(R.id.soft_q_selector);

        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
            }
        });
        ArrayAdapter<FieldValue> sa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, out);
//        if (out.size() >= 1) {
//            if (!sa.getItem(0).equals(fvLead)) {
//                sa.insert(fvLead, 0);
//            }
//
//        }
        newQuestion.setAdapter(sa);
        newQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean click = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View viewI, int position, long id) {
                if (click) {
                    newFieldValueSelected(newQuestion.getSelectedItemPosition());
                    //TODO
                    //this does not work in the emulator, but works on a physical device
                    view.invalidate();
                    vs.showNext();
                } else {
                    click = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void newFieldValueSelected(int position) {
        FieldValue fv = out.remove(position);
        in.add(fv);
        softFieldContainer.addFieldValue(listItemView(fv), fv);
        notifyDataListener();
    }

    private void removeFieldValue(FieldValue fv) {
        if(in.remove(fv)) {
            out.add(fv);
            softFieldContainer.removeFieldValue(fv);
        } else {
            Toast.makeText(getActivity(), "could not remove FieldValue " + fv.getField(), Toast.LENGTH_SHORT).show();
        }
        notifyDataListener();
    }

    private void notifyDataListener() {
        if (dl != null) {
            dl.setFieldValueData(allList());
        }
    }

    private List<FieldValue> allList() {
        ArrayList<FieldValue> all = new ArrayList<>();
        for (FieldValue fv : in) {
            if (!fv.getIn()) {
                fv.setIn(true);
            }
            all.add(fv);
        }
        for (FieldValue fv : out) {
            if (fv.getIn()) {
                fv.setIn(false);
            }
            all.add(fv);
        }
        return all;
    }

    public class IndexLinearLayout {
        LinearLayout layout;
        int currentIndex = 0;

        Map<String, View> views = new TreeMap<>();

        private IndexLinearLayout(LinearLayout layout) {
            this.layout = layout;
        }

        public void addFieldValue(View view, FieldValue fv) {
            views.put(fv.getId(), view);
            layout.addView(view, currentIndex);
            currentIndex++;
        }

        public void removeFieldValue(FieldValue fv) {
            currentIndex--;
            layout.removeView(views.get(fv.getId()));
        }

        public void addControlBlock(View view) {
            layout.addView(view, currentIndex);
        }

    }

    public interface DataListener {
        public void setFieldValueData(List<FieldValue> fvs);

        public void finishFieldValues();
    }
}
