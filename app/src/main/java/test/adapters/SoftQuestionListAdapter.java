package test.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import test.assesortron3.R;
import test.objects.FieldValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;

/**
 * Created by willpassidomo on 6/5/15.
 */
public class SoftQuestionListAdapter implements ListAdapter {

    private boolean changed = false;
    private FieldValue fvLead = new FieldValue("select field..", false);
    private DataListener dl;
    private Context context;
    private ArrayList<FieldValue> in;
    private ArrayList<FieldValue> out;
    private TextView field,value;


    public SoftQuestionListAdapter(Context context, List<FieldValue> fvs) {
        this.context = context;
        in = new ArrayList<>();
        out = new ArrayList<>();

        for (FieldValue fv : fvs) {
            if (fv.getIn()) {
                in.add(fv);
            } else {
                out.add(fv);
            }
        }
    }

    public SoftQuestionListAdapter(Context context, List<FieldValue> fvs, DataListener dl) {
        this(context, fvs);
        this.dl = dl;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return in.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position >= in.size()) {
            return new String("last position");
        } else {
            return in.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        boolean controlBlock = (position >= in.size());
        boolean matches = true;

        if (view != null && !controlBlock && !changed ) {
        /*gets the "field" value of the view passed in */
            field = (TextView) view.findViewById(R.id.soft_q_field);
            value = (TextView) view.findViewById(R.id.soft_q_value);
        /*is the corresponding field's view from the "in" array equal
         * to the field value of the view */
            FieldValue fv = in.get(position);
            boolean fieldMatches = fv.getField().equals(field.getText());
            boolean valueEmpty = value.getText() == null && fv.getField().equals("");
            boolean valueMatched;
            if (fv.getValue() != null) { valueMatched = fv.getValue().equals(value.getText());} else {valueMatched = false;}
            matches = fieldMatches && (valueEmpty | valueMatched);
        } else {
            matches = true;
        }
        /*resets view if the ^ doesnt match */
        if (view == null || (changed && position == in.size() - 1) || !matches) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (controlBlock) {
                view = infalInflater.inflate(R.layout.soft_q_control_block, null);
            } else {
                view = infalInflater.inflate(R.layout.soft_q_field_val, null);
            }
            if (controlBlock) {
                return controlBlockView(view);
            } else {
                return listItemView(view, position);
            }
        }
        return view;
    }

    private View controlBlockView(final View view) {
        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.soft_q_view_switcher);
        Button addQ = (Button) view.findViewById(R.id.soft_q_add_q);
        final Spinner newQuestion = (Spinner) view.findViewById(R.id.soft_q_selector);

        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
            }
        });
        ArrayAdapter<FieldValue> sa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, out);
        if (out.size() >= 1) {
            if (!sa.getItem(0).equals(fvLead)) {
                sa.insert(fvLead, 0);
            }

        }
        newQuestion.setAdapter(sa);
        newQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean click = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View viewI, int position, long id) {
                if (click) {
                    newFieldValueSelected(newQuestion.getSelectedItemPosition());

                    ///TODO
                    //this makes the view update in the emulator
//                    vs.removeView(vs.getNextView());
//                    vs.addView(getView(in.size() - 1,null,null),0);
//                    vs.showNext();
                    //TODO
                    //this does not work in the emulator, but works on a physical device
                    view.invalidate();
                    vs.showNext();
                    changed = true;
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

    private View listItemView(final View view, final int position) {
        final FieldValue fv = in.get(position);

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
                    removeFieldValue(position);
                    vs.showNext();
                    view.invalidate();
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

    private void newFieldValueSelected(int position) {
        in.add(out.remove(position));
        notifyDataListener();
    }

    private void removeFieldValue(int position) {
        out.add(in.remove(position));
        notifyDataListener();
    }

    public ArrayList<FieldValue> getFieldValues() {
        return allList();
    }

    private void notifyDataListener() {
        if (dl != null) {
            dl.setFieldValueData(allList());
        }
    }

    private ArrayList<FieldValue> allList() {
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= in.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public interface DataListener {
        void setFieldValueData(List<FieldValue> fvs);
    }
}


