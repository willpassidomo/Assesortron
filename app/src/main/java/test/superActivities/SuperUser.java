package test.superActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import test.Fragments.SetListSelectionFragment;
import test.UserFragments.ActiveProjects;
import test.UserFragments.NewProjectRequired;
import test.UserFragments.StringListEdit;
import test.assesortron5.R;
import test.assesortron5.UserSelect;
import test.drawers.IconHeaderRecyclerAdapter;
import test.objects.Project;
import test.objects.User;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/17/15.
 */
public class SuperUser extends NavDrawerActivityPrototype implements NewProjectRequired.ProjectListener, SetListSelectionFragment.SetListSelectionInterface {
    private static final String NEW_PROJECT_TRADES = "new_project_trades";
    Context context = this;
    User user;
    Project projectBeingStarted;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
    }

    private View headerView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.header_user, parent, false);
        TextView email = (TextView) view.findViewById(R.id.header_user_email);
        TextView name = (TextView) view.findViewById(R.id.header_user_name);
        CircleImageView image = (CircleImageView) view.findViewById(R.id.header_user_image);
        Bitmap userImage = user.getImage();
        email.setText(user.getEmail());
        name.setText(user.getUserName());
        image.setImageBitmap(userImage);
        return view;
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {

        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(this) {
            @Override
            public View inflateHeader(LayoutInflater inflater, ViewGroup parent) {
                return headerView(inflater, parent);
            }
        };
        IconHeaderRecyclerAdapter.ListItem[] items = {
            adapter.newItem(this, "Active Projects" ,R.drawable.ic_list_items, ActiveProjects.newInstance(user.getId())),
            adapter.newItem(this, "New Project",R.drawable.ic_new_item, NewProjectRequired.newInstance(this)),
            adapter.newItem(this, "Inbox",R.drawable.ic_mail_message, new Fragment()),
            adapter.newItem(this, "Master Trades List",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.TRADES)),
            adapter.newItem(this, "Master Progress List",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.PROGRESSES)),
            adapter.newItem(this, "Logout",R.drawable.ic_back_arrow, logoutListener())
        };

        adapter.setItems(items);
        return adapter;
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
    public String getDrawerClosedHeader() {
        if (user != null) {
            return user.getUserName();
        } else {
            return "User";
        }
    }

    @Override
    public String getDrawerOpenHeader() {
        if (user != null) {
            return user.getUserName() + " menu";
        } else {
            return "User menu";
        }
    }

    @Override
    public void initialize() {}

    @Override
    public void handleIntent(Intent intent) {
        String userId = intent.getStringExtra(Constants.USER_ID);
        if (userId != null && !userId.equals("")) {
            user = Storage.getUserById(this, userId);
        }
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
            displayFragment(NewProjectRequired.newInstance(this));
            done();
        }

    }
}
