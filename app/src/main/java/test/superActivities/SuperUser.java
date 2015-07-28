package test.superActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
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
public class SuperUser extends NavDrawerActivityPrototype implements NewProjectRequired.ProjectListener {
    Context context = this;
    User user;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        mDrawerLayout.openDrawer(mRecylerView);
    }

    private View headerView() {
        View view = LayoutInflater.from(this).inflate(R.layout.header_user, null);
        TextView email = (TextView) view.findViewById(R.id.header_user_email);
        TextView name = (TextView) view.findViewById(R.id.header_user_name);
        CircleImageView image = (CircleImageView) view.findViewById(R.id.header_user_image);
        email.setText(user.getEmail());
        name.setText(user.getUserName());
        image.setImageBitmap(user.getImage());
        return view;
    }

    @Override
    public RecyclerView.Adapter getRecyclerAdapter() {

        IconHeaderRecyclerAdapter adapter = new IconHeaderRecyclerAdapter(headerView(), this);
        IconHeaderRecyclerAdapter.ListItem[] items = {
            adapter.newItem(this, "Active Projects" ,R.drawable.ic_list_items, ActiveProjects.newInstance(user.getId())),
            adapter.newItem(this, "New Project",R.drawable.ic_new_item, NewProjectRequired.newInstance(this)),
            adapter.newItem(this, "Inbox",R.drawable.ic_mail_message, new Intent(this, UserSelect.class)),
            adapter.newItem(this, "Master Trades List",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.TRADES)),
            adapter.newItem(this, "Set Progresses",R.drawable.ic_info, StringListEdit.newInstance(StringListEdit.PROGRESSES)),
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
        if (userId == null || userId.equals("")) {
            userId = "1";
        }
        user = Storage.getUserById(this, userId);
    }

    @Override
    public void submitProject(Project project) {
        Storage.storeProject(this, project);
        done();
    }
}
