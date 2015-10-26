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

import test.assesortron5.R;
import test.objects.Project;
import test.persistence.Constants;
import test.superActivities.SuperProject;


/**
 * Created by willpassidomo on 1/19/15.
 */
public class ProjectListAdapter implements ListAdapter {
    private boolean EMPTY = false;
    private String userId;

    private List<Project> projects;
    private Context context;

    public ProjectListAdapter(Context context, List<Project> projects, String userId) {
        this.context = context;
        this.projects = projects;
        this.userId = userId;
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

        if (projects != null && projects.size() > 0) {
            return projects.size();
        } else {
            EMPTY = true;
            return 1;
        }
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
            if (EMPTY) {
                view = infalInflater.inflate(R.layout.list_empty, null);
            } else {
                view = infalInflater.inflate(R.layout.list_active_projects, null);
            }
        }

        if (EMPTY) {
            TextView message = (TextView)view.findViewById(R.id.list_empty_message);
            message.setText("no active Projects");
        } else {
            final Project project = projects.get(i);

            TextView name = (TextView) view.findViewById(R.id.project_name);
            TextView date = (TextView) view.findViewById(R.id.project_start_date);
            TextView streetAddress = (TextView) view.findViewById(R.id.list_active_projects_street_add);
            TextView cityStateZip = (TextView) view.findViewById(R.id.list_active_projects_city_state);


            name.setText(project.getName());
            date.setText(new SimpleDateFormat("MM/dd/yyyy").format(project.getDateCreated()));
            if (project.getAddress().getStreeAddress() != null && !project.getAddress().getStreeAddress().equals("")) {
                streetAddress.setText(project.getAddress().getStreeAddress());
            } else {
                streetAddress.setText("--");
            }
            cityStateZip.setText(project.getAddress().getCityStateZip());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SuperProject.class);
                    intent.putExtra(Constants.PROJECT_ID, project.getId());
                    intent.putExtra(Constants.USER_ID, userId);
                    context.startActivity(intent);
                }
            });
        }
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
