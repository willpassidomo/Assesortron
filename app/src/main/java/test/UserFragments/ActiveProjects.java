package test.UserFragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import test.adapters.ProjectListAdapter;
import test.assesortron5.R;
import test.objects.Project;
import test.persistence.Storage;

/**
 * Created by otf on 7/17/15.
 */
public class ActiveProjects extends Fragment {
    String userId;
    ListView listView;
    List<Project> projects;

    public ActiveProjects() {
        super();
    }

    public static ActiveProjects newInstance(String userId) {
        ActiveProjects activeProjects = new ActiveProjects();
        activeProjects.setUserId(userId);
        return activeProjects;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);
        View view = inflater.inflate(R.layout.fragment_list, null);
        listView = (ListView)view.findViewById(R.id.list_fragment_list);
        setProjects();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    private void setProjects() {
        AsyncTask<String, Void, List<Project>> task = new AsyncTask<String, Void, List<Project>>() {
            @Override
            protected List<Project> doInBackground(String... strings) {
                return Storage.getActiveProjects(getActivity());
            }

            @Override
            protected void onPostExecute(List<Project> projectList) {
                ProjectListAdapter adapter = new ProjectListAdapter(getActivity(), projectList);
                listView.setAdapter(adapter);
                projects = projectList;

            }
        };
        task.execute(userId);
    }
}
