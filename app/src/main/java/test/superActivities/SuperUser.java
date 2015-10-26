package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import test.Fragments.SetListSelectionFragment;
import test.Fragments.SoftQuestionsFragment;
import test.UserFragments.ActiveProjects;
import test.UserFragments.NewProjectRequired;
import test.UserFragments.StringListEdit;
import test.adapters.ProjectListAdapter;
import test.assesortron5.R;
import test.assesortron5.UserSelect;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.FieldValue;
import test.objects.Project;
import test.objects.User;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/17/15.
 */
public class SuperUser extends AppCompatActivity implements NewProjectRequired.ProjectListener, SetListSelectionFragment.SetListSelectionInterface, StringListEdit.StringListEditCallback, SoftQuestionsFragment.DataListener {
    private static final int PROJECT_QUESTIONS = 1;
    private static final int MASTER_TRADES = 2;
    private static final int MASTER_PROGRESESS = 3;

    public static final String NEW_PROJECT_REQUIRED_FRAGMENT = "new_project_required_fragment";
    private static final String NEW_PROJECT_TRADES = "new_project_trades";
    private List<String> projectQuestions = new ArrayList<>();
    private Context context = this;
    private User user;
    private Project projectBeingStarted;
    private NewProjectRequired newProjectRequired;
    private Button newProject;
    private ListView activeProjects;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_blank);

        String userid = getIntent().getStringExtra(Constants.USER_ID);
        user = Storage.getUserById(this, userid);

        List<Project> projects = Storage.getActiveProjects(this, userid);
        ProjectListAdapter
    }


    private View.OnClickListener logoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage.removeLoggedInId(context);
                startActivity(new Intent(context, UserSelect.class));

            }
        };
    }



    @Override
    public void submitProject(Project project) {
        projectBeingStarted = project;
        Storage.storeProject(this, project);
        this.displayFragment(SetListSelectionFragment.getInstance(this, Storage.getTradeList(this), null, NEW_PROJECT_TRADES));
    }

    @Override
    public void setList(String id, List<String> strings) {
        if (id == NEW_PROJECT_TRADES) {
            Storage.storeProjectTradeList(this, strings, projectBeingStarted.getId());
            this.displayFragment(SoftQuestionsFragment.newInstance(this, Storage.getProjectQuestions(this, projectBeingStarted.getId()), projectBeingStarted.getId()));
        }
    }

    @Override
    public void setFieldValueData(List<FieldValue> fvs) {
        projectBeingStarted.setFieldValues(fvs);
    }

    @Override
    public void finishFieldValues(List<FieldValue> fvs) {
        projectBeingStarted.setFieldValues(fvs);
        Storage.storeFieldValues(this, projectBeingStarted.getFieldValues());
        newProjectRequired = NewProjectRequired.newInstance(this, user.getId());
        displayFragment(newProjectRequired);
        done();
    }

    @Override
    public void deleteString(int type, String string) {
        switch (type) {
            case MASTER_PROGRESESS:
                Storage.deleteProgress(this, string);
            case MASTER_TRADES:
                Storage.deleteTrade(this, string);
            case PROJECT_QUESTIONS:
                Storage.deleteProjectQuestion(this, string);
        }
    }

    @Override
    public void addString(int type, String string) {
        switch (type) {
            case MASTER_PROGRESESS:
                List<String> l = new ArrayList<>();
                l.add(string);
                Storage.storeProgresses(this, l);
                break;
            case MASTER_TRADES:
                List<String> ll = new ArrayList<>();
                ll.add(string);
                Storage.storeTrades(this, ll);
                break;
            case PROJECT_QUESTIONS:
                FieldValue fv = new FieldValue(string, false);
                Storage.storeProjectQuestions(this, fv);
                break;
        }
    }
}
