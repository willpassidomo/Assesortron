package test.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.assesortron5.History;
import test.assesortron5.R;

/**
 * Created by willpassidomo on 3/9/15.
 */
public class WalkThroughDateListAdapter implements ListAdapter {
    Context context;
    String projectId;
    Map<Date, ArrayList<String>> dates = new HashMap<>();
    List<Date> dateArray;

    public WalkThroughDateListAdapter(Context context, Map<Date, ArrayList<String>> dates, String projectId) {
        this.context = context;
        this.projectId = projectId;
        if (dates != null)
            this.dates = dates;
        dateArray = new ArrayList<Date>(dates.keySet());
        Collections.sort(dateArray, new Comparator<Date>() {
            @Override
            public int compare(Date lhs, Date rhs) {
                return lhs.compareTo(rhs);
            }
        });
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
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dateArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
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
            view = infalInflater.inflate(R.layout.list_walk_through_dates, null);
        }

        TextView dateView = (TextView)view.findViewById(R.id.walk_through_date_list_date);
        TextView entries = (TextView)view.findViewById(R.id.walk_through_ldate_list_entries);

        final Date date = dateArray.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        dateView.setText(sdf.format(date));
        entries.setText(dates.get(date).size() +"");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((History) context).setWalkThroughList(dates.get(date));
                }
            }
        );

        return view;
    }

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
        return dates.isEmpty();
    }
}
