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
    public static final String TRADES = "trades_list_sle";
    public static final String PROGRESSES = "progress_list_sle";
    public static final String TRADE_LIST_TITLE = "Trades list";
    public static final String PROGRESSES_LIST_TITLE = "Progress levels list";

    private String STRING_LIST_TYPE;

    private Context context;
    private Button addField;
    private AutoCompleteTextView newItem;
    private ListAdapter adapter;
    ArrayAdapter<String> autoCompleteAdapter;

    List<String> strings;

    public StringListEdit() {}

    public static StringListEdit newInstance(String type) {
        StringListEdit stringListEdit = new StringListEdit();
        stringListEdit.setType(type);
        return stringListEdit;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        super.onCreateView(inflater, vg, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_string_list_edit, null);

        setVariables(view);

        switch (STRING_LIST_TYPE) {
            case StringListEdit.TRADES:
                strings = Storage.getTradeList(getActivity());
                setTradeButtons();
                break;
            case StringListEdit.PROGRESSES:
                strings = Storage.getProgressList(getActivity());
                setProgressButtons();
                break;
            default:
                Log.e("StringListEdit",
                        "Problem, STING_LIST_TYPE not recognized: "
                                + STRING_LIST_TYPE);
        }

        newItem.setAdapter(autoCompleteAdapter);
        newItem.setThreshold(1);

        ListView listView = (ListView)view.findViewById(R.id.string_list_list);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);

        return view;

    }


    private void setType(String type) {
        this.STRING_LIST_TYPE = type;
    }

    private void setVariables(View view) {
        addField = (Button)view.findViewById(R.id.string_list_add_button);
        newItem = (AutoCompleteTextView)view.findViewById(R.id.string_list_new_item);
    }


    private void setTradeButtons() {
        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> l = new ArrayList<>();
                l.add(getNewItem());
                Storage.storeTrades(context, l);
            }
        });
        getActivity().getActionBar().setTitle(TRADE_LIST_TITLE);

        List tradePossibilities = Storage.getMasterTradeList();
        autoCompleteAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tradePossibilities);

    }

    private void setProgressButtons() {
        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> l = new ArrayList<>();
                l.add(getNewItem());
                Storage.storeProgresses(context, l);
            }
        });
        getActivity().getActionBar().setTitle(PROGRESSES_LIST_TITLE);
        //TODO
        // decide if you want users to be able to change progresses
        // and if you are going to provide stock suggestions

        //List progressPossibilities = Storage.getMasterProgressList();
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
        if (STRING_LIST_TYPE.equals(TRADES)) {
            Storage.deleteTrade(context,s);
        }
        if (STRING_LIST_TYPE.equals(PROGRESSES)) {
            Storage.deleteProgress(context, s);
        }
    }

    class ListAdapter extends BaseAdapter {

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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.list_values_strg, null);
            }


            TextView item = (TextView)view.findViewById(R.id.list_string_string);
            if (strings.get(i) == null || strings.get(i).equals("")) {
                item.setText("--");
            } else {
                item.setText(strings.get(i));
            }

            final Button delete = (Button) view.findViewById(R.id.string_list_delete_button);
            final LinearLayout dLayout = (LinearLayout) view.findViewById(R.id.string_list_d_layout);
            final ViewSwitcher vs = (ViewSwitcher)view.findViewById(R.id.string_list_view_switcher);

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
            return view;
        }
    }
}
