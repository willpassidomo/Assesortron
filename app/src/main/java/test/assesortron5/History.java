package test.assesortron5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import test.adapters.DrawRequestListAdapter;
import test.adapters.WalkThroughDateListAdapter;
import test.adapters.WalkThroughListAdapter;
import test.objects.DrawRequest;
import test.objects.WalkThrough;
import test.persistence.Constants;
import test.persistence.Storage;

public class History extends Activity {
    String projectId;
    String siteWalkId;

    Map<Date, ArrayList<String>> walkThroughDates;
    List<WalkThrough> walkThroughs;
    List<DrawRequest> drawRequests;

    Queue<ListPair> backStack = new LinkedList<ListPair>();

    ListView walkThroughList;
    ListView drawRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        projectId = intent.getStringExtra(Constants.PROJECT_ID);
        siteWalkId = intent.getStringExtra(Constants.SITE_VISIT_ID);

        setListAdapters();
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
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

    private void setListAdapters() {

        drawRequests = Storage.getDrawRequestByProjectId(this, projectId);

        walkThroughList = (ListView) this.findViewById(R.id.list_walk_through);
        walkThroughDates = Storage.getSWalkThroughDates(this, siteWalkId);
        WalkThroughDateListAdapter wtdla = new WalkThroughDateListAdapter(this, walkThroughDates, projectId);
        walkThroughList.setAdapter(wtdla);

        DrawRequestListAdapter drla = new DrawRequestListAdapter(this, drawRequests, projectId);
        drawRequestList = (ListView) this.findViewById(R.id.list_draw_request);
        drawRequestList.setAdapter(drla);
    }

    public void setWalkThroughList(List<String> walkThroughIds) {
        walkThroughList = (ListView) this.findViewById(R.id.list_walk_through);
        walkThroughs = Storage.getWalkThroughs(this, walkThroughIds);
        backStack.offer(new ListPair(walkThroughList, walkThroughList.getAdapter()));
        WalkThroughListAdapter wtla = new WalkThroughListAdapter(this, walkThroughs, projectId);
        walkThroughList.setAdapter(wtla);
    }

    @Override
    public void onBackPressed() {
        if (!backStack.isEmpty()) {
            ListPair pair = backStack.poll();
            pair.getListView().setAdapter(pair.getListAdapter());
        } else {
            super.onBackPressed();
        }
    }

    private static class ListPair {
        private ListView listView;
        private ListAdapter listAdapter;

        public ListPair(ListView listView, ListAdapter listAdapter) {
            this.listView = listView;
            this.listAdapter = listAdapter;
        }

        public ListView getListView() {
            return this.listView;
        }

        public ListAdapter getListAdapter() {
            return this.listAdapter;
        }

        public void setListView(ListView listView) {
            this.listView = listView;
        }

        public void setListAdapter(ListAdapter listAdapter) {
            this.listAdapter = listAdapter;
        }
    }
}
