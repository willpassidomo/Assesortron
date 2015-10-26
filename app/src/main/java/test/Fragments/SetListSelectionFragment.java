package test.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import test.assesortron5.R;
import test.persistence.Constants;

/**
 * Created by otf on 7/13/15.
 */
public class SetListSelectionFragment extends Fragment {
    private static final String IN_LIST = "in_list";
    private static final String STRINGS_LIST = "string_list";
    SetListSelectionInterface parent;
    StringSelectorAdapter adapter;
    List<String> inList;
    List<String> stringList;
    Button submit;
    ListView selectionList;
    String id;

    public SetListSelectionFragment() {
        super();
    }

    public static SetListSelectionFragment getInstance(SetListSelectionInterface parent, List<String> stringList, List<String> inList, String id) {
        SetListSelectionFragment sptf = new SetListSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(STRINGS_LIST, new ArrayList<>(stringList));
        bundle.putStringArrayList(IN_LIST, inList != null ? new ArrayList<>(inList) : new ArrayList<String>());
        bundle.putString(Constants.ID_TYPE, id);
        sptf.setArguments(bundle);
        sptf.setParent(parent);
        return sptf;
    }


    private void setParent(SetListSelectionInterface parent) {
        this.parent = parent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SetListSelectionInterface && parent == null) {
            parent = (SetListSelectionInterface)activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        stringList = getArguments().getStringArrayList(STRINGS_LIST);
        inList = getArguments().getStringArrayList(IN_LIST);
        id = getArguments().getString(Constants.ID_TYPE);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstance) {
        super.onCreateView(inflater, v, savedInstance);

        View view = inflater.inflate(R.layout.fragment_set_list_selection, null);
        submit = (Button)view.findViewById(R.id.set_list_selection_submit);
        selectionList = (ListView)view.findViewById(R.id.set_list_selection_list);
        adapter = new StringSelectorAdapter(stringList, inList);
        selectionList.setAdapter(adapter);
        submit.setOnClickListener(submitListener());
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
        Set<String> inList;
        List<String> strings;

        public StringSelectorAdapter(List<String> strings, List<String> inList) {
            if (inList != null) {
                this.inList = new TreeSet<>(inList);
            } else {
                this.inList = new TreeSet<>();
            }
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
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_string_selection, null);
            }
            final String string = strings.get(i);
            TextView stringVal = (TextView) view.findViewById(R.id.list_string_stringval);
            final RadioButton button = (RadioButton) view.findViewById(R.id.list_string_radio_button);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_string_click_space);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linearLayout.setSelected(!linearLayout.isSelected());
                    button.setChecked(!button.isChecked());
                }
            });
            if (this.inList.contains(string)) {
                button.setChecked(true);
            }

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        inList.add(string);
                    } else {
                        inList.remove(string);
                    }
                }
            });
            stringVal.setText(string);
            return view;
        }

        private List<String> getCheckedStrings() {
            return new ArrayList<>(inList);
        }
    }
        private View.OnClickListener submitListener() {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        parent.setList(id, adapter.getCheckedStrings());
                }
            };
            return listener;
        }

    /**
     *     Interface if should be used if parent activity only has ONE instance
     *     of SetListSelectionFragment that it will be interacting with. Since parent
     *     only has one instance, it will know what to do with the list sent to
     *     SetListSelectionInterface and who it is from
     *
     */
    public interface SetListSelectionInterface {
        public void setList(String id, List<String> strings);
    }

}
