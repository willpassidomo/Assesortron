package test.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import test.assesortron3.R;

/**
 * Created by otf on 7/13/15.
 */
public class SetListSelectionFragment extends Fragment {
    StringSelectorAdapter adapter;
    List<String> stringList;
    Button editEntries;
    ListView selectionList;

    public SetListSelectionFragment() {
        super();
    }

    public static SetListSelectionFragment getInstance(List<String> stringList) {
        SetListSelectionFragment sptf = new SetListSelectionFragment();
        sptf.setStringList(stringList);
        return sptf;
    }

    private void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstance) {
        super.onCreateView(inflater, v, savedInstance);

        View view = inflater.inflate(R.layout.fragment_set_project_trades, null);
        editEntries = (Button)view.findViewById(R.id.set_project_trades_edit_options);
        selectionList = (ListView)view.findViewById(R.id.set_trades_list);
        adapter = new StringSelectorAdapter(stringList);
        selectionList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);


    }



    public List<String> getSelectedItems() {
        return adapter.getCheckedStrings();
    }



    class StringSelectorAdapter extends BaseAdapter {

        List<String> strings;
        Map<Integer, View> views = new TreeMap<>();

        public StringSelectorAdapter (List<String> strings) {
            this.strings = strings;
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int i) {
            return strings.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_string_selection, null);
            }

            TextView stringVal = (TextView)view.findViewById(R.id.list_string_stringval);
            final RadioButton button = (RadioButton)view.findViewById(R.id.list_string_radio_button);
            LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.list_string_click_space);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setChecked(!button.isChecked());
                }
            });
            stringVal.setText(strings.get(i));

            views.put(new Integer(i), view);
            return view;
        }

        public List<String> getCheckedStrings() {
            List<String> selected = new ArrayList<>();
            for(int i = 0; i < strings.size(); i++) {
                View view = views.get(new Integer(i));
                final RadioButton button = (RadioButton)view.findViewById(R.id.list_string_radio_button);
                if (button.isChecked()) {
                    TextView stringVal = (TextView)view.findViewById(R.id.list_string_stringval);
                    selected.add(stringVal.getText().toString());
                }
            }
            return selected;
        }
    }
}
