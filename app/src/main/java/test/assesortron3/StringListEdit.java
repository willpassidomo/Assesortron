package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import java.util.ArrayList;
import java.util.List;

import test.dynamicListView.DynamicListView;
import test.dynamicListView.StableArrayAdapter;
import test.persistence.Constants;
import test.persistence.Storage;

public class StringListEdit extends Activity {
    public static final String TRADES = "trades_list_sle";
    public static final String PROGRESSES = "progress_list_sle";
    public static final String TRADE_LIST_TITLE = "Trades list";
    public static final String PROGRESSES_LIST_TITLE = "Progress levels list";

    private Context context;
    private Button addField;
    private AutoCompleteTextView newItem;
    private ListAdapter adapter;
    ArrayAdapter<String> autoCompleteAdapter;

    List<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_list_edit);
        context = this;

        setVariables();

        switch (getIntent().getStringExtra(Constants.STRING_LIST_TYPE)) {
            case StringListEdit.TRADES:
                strings = Storage.getTradeList(this);
                setTradeButtons();

                break;
            case StringListEdit.PROGRESSES:
                strings = Storage.getProgressList(this);
                setProgressButtons();
                break;
            default:
                Log.e("StringListEdit",
                        "Problem, STING_LIST_TYPE not recognized: "
                                + getIntent().getStringExtra(Constants.STRING_LIST_TYPE));
        }

        newItem.setAdapter(autoCompleteAdapter);
        newItem.setThreshold(1);

        ListView listView = (ListView)findViewById(R.id.string_list_list);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);

        ArrayAdapter<String> aa = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, strings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.string_list_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setVariables() {
        addField = (Button)findViewById(R.id.string_list_add_button);
        newItem = (AutoCompleteTextView)findViewById(R.id.string_list_new_item);
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
        getActionBar().setTitle(TRADE_LIST_TITLE);

        List tradePossibilities = Storage.getMasterTradeList();
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tradePossibilities);

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
        getActionBar().setTitle(PROGRESSES_LIST_TITLE);
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
        if (getIntent().getStringExtra(Constants.STRING_LIST_TYPE).equals(TRADES)) {
            Storage.deleteTrade(this,s);
        }
        if (getIntent().getStringExtra(Constants.STRING_LIST_TYPE).equals(PROGRESSES)) {
            Storage.deleteProgress(this, s);
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
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
