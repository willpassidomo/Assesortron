package test.assesortron3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.dynamicListView.DynamicListView;
import test.dynamicListView.StableArrayAdapter;
import test.persistence.Storage;

public class StringListEdit extends Activity {

    List<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_list_edit);

        List<String> trades = new ArrayList<String>();
        trades.add("Demo");
        trades.add("Framing");
        trades.add("Plumbing- rough");
        trades.add("Plumbing- finish");
        trades.add("Electical");
        trades.add("HVAC");
        Toast.makeText(this, trades.size()+"", Toast.LENGTH_LONG).show();
        Storage.storeTrades(this, trades);


        strings = Storage.getTradeList(this);

        ArrayAdapter<String> aa = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, strings);

        DynamicListView stringList = (DynamicListView) findViewById(R.id.string_list_list);
        stringList.setAdapter(aa);
        stringList.setCheeseList((ArrayList <String>)strings);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.string_list_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
