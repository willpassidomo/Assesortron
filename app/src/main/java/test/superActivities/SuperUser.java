package test.superActivities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import test.Fragments.SetListSelectionFragment;
import test.UserFragments.ActiveProjects;
import test.UserFragments.NewProjectRequired;
import test.UserFragments.StringListEdit;
import test.assesortron5.R;
import test.assesortron5.UserSelect;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.Project;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/17/15.
 */
public class SuperUser extends NavDrawerActivityPrototype implements NewProjectRequired.ProjectListener {
    String userName;
    String userId = "00000";

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {
        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(R.layout.header_user_drawer, this);
        IconHeaderRecyclerAdapter.IconHeaderObject[] items = {
            adapter.newItem(this, "Active Projects" ,R.drawable.ic_list_items, ActiveProjects.newInstance(userId)),
            adapter.newItem(this, "New Project",R.drawable.ic_new_item, NewProjectRequired.newInstance(this)),
            adapter.newItem(this, "Inbox",R.drawable.ic_mail_message, new Intent(this, UserSelect.class)),
            adapter.newItem(this, "Set Trades",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.TRADES)),
            adapter.newItem(this, "Set Progresses",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.PROGRESSES)),
            adapter.newItem(this, "Logout",R.drawable.ic_back_arrow, new Intent(this, UserSelect.class))
        };

        adapter.setItems(items);
        return adapter;
    }
    @Override
    public String getDrawerClosedHeader() {
        if (userName != null) {
            return userName;
        } else {
            return "User";
        }
    }

    @Override
    public String getDrawerOpenHeader() {
        if (userName != null) {
            return userName + " menu";
        } else {
            return "User menu";
        }
    }

    @Override
    public void initialize() {}

    @Override
    public void handleIntent(Intent intent) {
        userName = intent.getStringExtra(Constants.USER_NAME);
    }

    @Override
    public void submitProject(Project project) {
        done();
    }
}
