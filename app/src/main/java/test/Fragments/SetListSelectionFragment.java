package test.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import test.assesortron5.R;

/**
 * Created by otf on 7/13/15.
 */
public class SetListSelectionFragment extends Fragment {
    SetListSelectionInterface onlyChildParent;
    SetListSelectionVisitorInterface siblingsParent;
    StringSelectorAdapter adapter;
    List<String> inList;
    List<String> stringList;
    Button submit;
    ListView selectionList;
    String id;

    public SetListSelectionFragment() {
        super();
    }

    /**
     * see SetListSelectionInterface and SetListSelectionVisitorInterface docs for information
     * on when to use each of the two constructors
     * @param parent
     * @param stringList
     * @return
     */
    public static SetListSelectionFragment getInstance(SetListSelectionInterface parent, List<String> stringList, List<String> inList) {
        SetListSelectionFragment sptf = new SetListSelectionFragment();
        sptf.setStringList(stringList, inList);
        sptf.setParent(parent);
        return sptf;
    }

    /**
     * see SetListSelectionInterface and SetListSelectionVisitorInterface docs for information
     * on when to use each of the two constructors
     * @param parent
     * @param stringList
     * @param id
     * @return
     */

    public static SetListSelectionFragment getInstance(SetListSelectionVisitorInterface parent, List<String> stringList, List<String> inList, String id) {
        SetListSelectionFragment sptf = new SetListSelectionFragment();
        sptf.setParent(parent);
        sptf.setStringList(stringList, inList);
        sptf.setType(id);
        return sptf;
    }

    private void setType(String id) { this.id = id;}
    private void setParent(SetListSelectionInterface parent) {
        this.onlyChildParent = parent;
    }
    private void setParent(SetListSelectionVisitorInterface parent) {this.siblingsParent = parent;}

    private void setStringList(List<String> stringList, List<String> inList) {
        this.inList = inList;
        this.stringList = stringList;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
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
        List<String> inList;
        List<String> strings;
        Map<Integer, View> views = new TreeMap<>();

        public StringSelectorAdapter(List<String> strings, List<String> inList) {
            this.inList = inList;
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
            if (this.inList.contains(stringVal)) {
                button.setChecked(true);
            }
            stringVal.setText(strings.get(i));

            views.put(new Integer(i), view);
            return view;
        }

        public List<String> getCheckedStrings() {
            List<String> selected = new ArrayList<>();
            for (int i = 0; i < strings.size(); i++) {
                View view = views.get(new Integer(i));
                final RadioButton button = (RadioButton) view.findViewById(R.id.list_string_radio_button);
                if (button.isChecked()) {
                    TextView stringVal = (TextView) view.findViewById(R.id.list_string_stringval);
                    selected.add(stringVal.getText().toString());
                }
            }
            return selected;
        }

    }
        private View.OnClickListener submitListener() {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onlyChildParent != null) {
                        onlyChildParent.setList(adapter.getCheckedStrings());
                    }
                    if (siblingsParent != null) {
                        done();
                    }
                }
            };
            return listener;
        }

    private void done() { siblingsParent.done(this);}

    /**
     *     Interface if should be used if parent activity only has ONE instance
     *     of SetListSelectionFragment that it will be interacting with. Since parent
     *     only has one instance, it will know what to do with the list sent to
     *     SetListSelectionInterface and who it is from
     *
     */
    public interface SetListSelectionInterface {
        public void setList(List<String> strings);
    }

    /**
     * SetListSelectionVisitorInterface is a take on the visitor pattern in the case
     * that an activity may be interacting with multiple instances of SetListSelectionFragment
     * modifying different lists. To implement this interface, the parent should call fragment.getId(),
     * in order to identify which of it's SetListSelectionFragments is finished. to get the modified
     * list, parent should call fragment,getSelectedItems();
     */

    public interface SetListSelectionVisitorInterface {
        public void done(SetListSelectionFragment fragment);
    }
}
