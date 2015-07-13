package test.assesortron3;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import test.Fragments.SetListSelectionFragment;
import test.persistence.Storage;


public class ProjectDashboardTwo extends Activity {
    LinearLayout mFragmentContainer;
    Button mCancel, mSubmit;
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_dashboard_two);

        setVariables();
        testListSelectionFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_dashboard_two, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setVariables() {
        mFragmentContainer = (LinearLayout)findViewById(R.id.project_dashboard_fragment_container);
        mSubmit = (Button)findViewById(R.id.project_dashboard_submit);
        mCancel = (Button)findViewById(R.id.project_dashboard_cancel);
        mTitle = (TextView)findViewById(R.id.project_dashboard_fragment_title);
    }

    private void testListSelectionFragment() {
        mTitle.setText("Select Project's Trades");

        List<String> trades = Storage.getTradeList(this);
        final SetListSelectionFragment slsf = SetListSelectionFragment.getInstance(trades);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.project_dashboard_fragment_container,slsf);
        ft.commit();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("submit", "click");
                for (String s: slsf.getSelectedItems()) {
                    Log.i("Selected trades", s);
                }
            }
        });
    }
}
