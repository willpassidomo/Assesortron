package test.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import test.assesortron3.ProjectHomeScreen;
import test.assesortron3.R;
import test.objects.Project;


/**
 * Created by willpassidomo on 1/19/15.
 */
public class projectListAdapter implements ListAdapter {

    private List<Project> projects;
    private Context context;

    public projectListAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
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
        return projects.size() ;
    }

    @Override
    public Object getItem(int i) {
        return projects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long)i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_active_projects, null);
        }

        TextView name = (TextView) view.findViewById(R.id.project_name);
        TextView date = (TextView) view.findViewById(R.id.project_start_date);

        name.setText(projects.get(i).getName());
        date.setText(new SimpleDateFormat("MM/dd/yyyy").format(projects.get(i).getDateCreated()));
        final Project project = projects.get(i);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProjectHomeScreen.class);
                intent.putExtra("id", project.getId());
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
        return projects.isEmpty();
    }
}
