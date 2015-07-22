package test.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.SimpleDateFormat;
import java.util.List;

import test.assesortron5.R;
import test.assesortron5.SiteWalkthrough;
import test.persistence.Constants;
import test.persistence.Storage;
import test.objects.WalkThrough;

/**
 * Created by willpassidomo on 1/27/15.
 */
public class WalkThroughListAdapter implements ListAdapter {
    Context context;
    List<WalkThrough> walkThroughs;
    String projectId;

    public WalkThroughListAdapter(Context context, List<WalkThrough> walkThroughs, String projectId) {
        this.walkThroughs = walkThroughs;
        this.context = context;
        this.projectId = projectId;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return walkThroughs.size();
    }

    @Override
    public Object getItem(int i) {
        return walkThroughs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return new Long(i);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_walk_throughs, null);
        }

        TextView floor = (TextView) view.findViewById(R.id.walk_through_floor);
        TextView subContractor = (TextView) view.findViewById(R.id.walk_through_contractor);
        TextView numPictures = (TextView) view.findViewById(R.id.walk_through_num_pics);
        TextView date = (TextView) view.findViewById(R.id.walk_through_list_date);

        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.walk_through_list_switcher);

        Button editButton = (Button) view.findViewById(R.id.walk_through_list_edit_button);
        Button deleteButton = (Button) view.findViewById(R.id.walk_through_list_delete_button);

        floor.setText(walkThroughs.get(i).getFloor());
        subContractor.setText(walkThroughs.get(i).getTrade());
        numPictures.setText(walkThroughs.get(i).getPictures().size()+"");
        date.setText(new SimpleDateFormat("MM/dd/yyyy").format(walkThroughs.get(i).getDate()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vs.showNext();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalkThrough wt = walkThroughs.get(i);
                Storage.deleteWalkThrough(context, wt.getId());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalkThrough wt = walkThroughs.get(i);

                Intent intent = new Intent(context, SiteWalkthrough.class);
                intent.putExtra(Constants.NEW_OR_EDIT, Constants.EDIT);
                intent.putExtra(Constants.WALK_THROUGH_ID,wt.getId());
                intent.putExtra(Constants.PROJECT_ID, projectId);
                context.startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return walkThroughs.isEmpty();
    }
}
