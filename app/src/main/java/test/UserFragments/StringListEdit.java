package test.UserFragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import java.util.ArrayList;
import java.util.List;

import test.assesortron5.R;
import test.persistence.Constants;
import test.persistence.Storage;

public class StringListEdit extends Fragment {
    public static final String STRINGS = "stringList";

    private StringListEditCallback parent;
    private int type;

    private String STRING_LIST_TYPE;

    private Context context;
    private Button addField;
    private AutoCompleteTextView newItem;
    private ListAdapter adapter;
    ArrayAdapter<String> autoCompleteAdapter;

    List<String> strings = new ArrayList<>();

    public StringListEdit() {}

    public static StringListEdit newInstance(StringListEditCallback parent, int type, List<String> strings) {
        StringListEdit stringListEdit = new StringListEdit();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ID_TYPE, type);
        bundle.putStringArrayList(STRINGS, new ArrayList<>(strings));
        stringListEdit.setArguments(bundle);
        stringListEdit.setParent(parent);
        return stringListEdit;
    }

    private void setParent(StringListEditCallback parent) {
        this.parent = parent;
    }

    private void setList(List<String> strings) {
        if (strings != null) {
            this.strings = strings;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StringListEditCallback && parent == null) {
            parent = (StringListEditCallback)activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        super.onCreateView(inflater, vg, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_string_list_edit, null);

        strings = getArguments().getStringArrayList(STRINGS);
        type = getArguments().getInt(Constants.ID_TYPE);

        setVariables(view);



        return view;

    }


    private void setVariables(View view) {
        addField = (Button)view.findViewById(R.id.string_list_add_button);
        newItem = (AutoCompleteTextView)view.findViewById(R.id.string_list_new_item);
        ListView listView = (ListView)view.findViewById(R.id.string_list_list);

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.addString(type, getNewItem());
            }
        });

        newItem.setAdapter(autoCompleteAdapter);
        newItem.setThreshold(1);

        adapter = new ListAdapter();
        listView.setAdapter(adapter);
    }

    private String getNewItem() {
        String newEntry = newItem.getText().toString();
        newItem.setText("");
        if (!strings.contains(newEntry)) {
            strings.add(newEntry);
            adapter.notifyDataSetChanged();
        }
        return newEntry;
    }

    private void deleteItem(int i) {
        String s = strings.remove(i);
        parent.deleteString(type, s);
        adapter.notifyDataSetChanged();
    }

    class ListAdapter extends BaseAdapter {
        Boolean EMPTY = false;

        private void setEMPTY(boolean empty) {
            this.EMPTY = empty;
        }

        @Override
        public int getCount() {
            if (strings != null && strings.size() > 0) {
                setEMPTY(false);
                return strings.size();
            } else {
                EMPTY = true;
                return 1;
            }
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null || (!EMPTY && i == 0)) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                if (EMPTY) {
                    view = inflater.inflate(R.layout.list_empty, null);
                } else {
                    view = inflater.inflate(R.layout.list_values_strg, null);
                }
            }

            if (EMPTY) {
                TextView message = (TextView)view.findViewById(R.id.list_empty_message);
                message.setText("no items, please add an item");
            } else {
                TextView item = (TextView) view.findViewById(R.id.list_string_string);
                if (strings.get(i) == null || strings.get(i).equals("")) {
                    item.setText("--");
                } else {
                    item.setText(strings.get(i));
                }

                final Button delete = (Button) view.findViewById(R.id.string_list_delete_button);
                final LinearLayout dLayout = (LinearLayout) view.findViewById(R.id.string_list_d_layout);
                final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.string_list_view_switcher);

                vs.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vs.showNext();
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteItem(i);
                                clearListeners();
                                vs.showNext();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        dLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clearListeners();
                                vs.showNext();
                            }
                        });
                        return true;
                    }

                    private void clearListeners() {
                        dLayout.setOnClickListener(null);
                        delete.setOnClickListener(null);
                    }
                });
            }
            return view;
        }
    }

    public interface StringListEditCallback {
        public void deleteString(int type, String string);
        public void addString(int type, String string);
    }
}
