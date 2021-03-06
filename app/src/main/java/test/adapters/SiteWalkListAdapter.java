package test.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import test.assesortron5.R;
import test.assesortron5.VisitSite;
import test.objects.SiteVisit;
import test.persistence.Constants;
import test.persistence.Storage;
import test.superActivities.SuperSiteVisit;

/**
 * Created by willpassidomo on 4/1/15.
 */
public abstract class SiteWalkListAdapter implements ListAdapter {

    Context context;
    List<SiteVisit> siteVisits;
    String emptyText;

    public SiteWalkListAdapter(Context context, List<SiteVisit> siteVisits, String emptyText) {
        this.emptyText = emptyText;
        this.context = context;
        this.siteVisits = siteVisits;
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
        if (siteVisits != null && siteVisits.size() > 0) {
            return siteVisits.size();
        } else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return siteVisits.get(position);
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

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (siteVisits == null || siteVisits.isEmpty()) {
                view = infalInflater.inflate(R.layout.list_empty, null);
            } else {
                view = infalInflater.inflate(R.layout.list_site_walks_active, null);
            }
        }

        if (siteVisits == null || siteVisits.isEmpty()) {
            TextView message = (TextView)view.findViewById(R.id.list_empty_message);
            message.setText(emptyText);
        } else {
            TextView date = (TextView) view.findViewById(R.id.list_site_walk_date);
            TextView drawRequestItemCount = (TextView) view.findViewById(R.id.list_site_walks_dr_item_count);
            TextView progressesCount = (TextView) view.findViewById(R.id.list_site_walk_progresses_count);

            final SiteVisit sw = siteVisits.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Date tDate = sw.getStartDate();
            date.setText(sdf.format(sw.getStartDate()));
            String progresses = Storage.getWalkThroughEntryCount(context, sw.getId()) + "";
            Log.i("progresses", progresses);
            progressesCount.setText(progresses);
            drawRequestItemCount.setText(Storage.getDrawRequestItemCount(context, sw.getId()) + "");

            view.setOnClickListener(itemClickListener(sw));
        }
        return view;
    }

    abstract public View.OnClickListener itemClickListener(final SiteVisit sv);

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
