package test.superActivities.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.w3c.dom.Text;

import java.util.List;

import test.Fragments.CameraPictureFragment;
import test.assesortron5.R;
import test.objects.User;
import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by otf on 7/28/15.
 */
public class Register extends Activity implements CameraPictureFragment.CameraPictureFragmentCallback {
    public static int COMPLETED = 1;
    public static int CANCLED = 2;
    private Context context = this;
    Button submit, cancel;
    ImageButton camera;
    TextView name, email, password, confirmpassword;
    String imageId;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_register_user);

        setVariables();
    }

    private void setVariables() {
        submit = (Button)findViewById(R.id.register_submit);
        cancel = (Button)findViewById(R.id.register_cancel);
        camera = (ImageButton)findViewById(R.id.register_camera_button);

        name = (TextView)findViewById(R.id.register_name);
        email = (TextView)findViewById(R.id.register_email);
        password = (TextView)findViewById(R.id.register_password);
        confirmpassword = (TextView) findViewById(R.id.register_password_confirm);

        submit.setOnClickListener(submitOnClickListener());
        cancel.setOnClickListener(cancelOnClickListener());
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraFrag();
            }
        });
    }

    private boolean complete() {
        String lname = name.getText().toString();
        String lemail = email.getText().toString();

        if (lname.length() < 3 || !isProperEmailAddress(lemail)) {
            new MaterialDialog.Builder(this)
                    .title("New User failed")
                    .content("for new registration, name must be at least 3 characters and email must be poperly formatted")
                    .show();
            return false;
        }

        String lpassword = password.getText().toString();
        String lconfirmpassword = confirmpassword.getText().toString();

        if (lpassword.length() < 6) {
            new MaterialDialog.Builder(this)
                    .title("Password too short")
                    .content("password must be at least 6 characters")
                    .show();
            return false;
        }

        if (!lpassword.equals(lconfirmpassword)) {
            new MaterialDialog.Builder(this)
                    .title("Passwords do not match")
                    .content("password and confirm password do not match")
                    .show();
            return false;
        }
        return true;
    }

    private User buildUser() {
        String lname = name.getText().toString();
        String lemail = email.getText().toString();
        User user = new User(lname, lemail);
        user.setImageId(imageId);
        return user;

    }



    private boolean isProperEmailAddress(String string) {
        char[] chars = string.toCharArray();
        for (int i = 1; i < chars.length - 5; i ++) {
            if (chars[i] == '@') {
                return true;
            }
        }
        return false;
    }

    private boolean uniqueUserName() {
        String lname = name.getText().toString();
        List<String> names = Storage.getUserNames(this);
        return !names.contains(lname);
    }


    private View.OnClickListener submitOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complete()) {
                    if (uniqueUserName()) {
                        User user = buildUser();
                        Storage.storeUser(context, user, password.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra(Constants.USER_ID, user.getId());
                        setResult(COMPLETED, intent);
                        finish();
                    } else {
                        Toast.makeText(context, "username \"" + name.getText().toString() + "\" already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener cancelOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(CANCLED);
                finish();
            }
        };
    }

    private void startCameraFrag() {
        getFragmentManager()
                .beginTransaction()
                .add(CameraPictureFragment.newInstance(this), "camera")
                .commit();
    }

    @Override
    public void returnImageId(String imageId) {
        this.imageId = imageId;
    }
}
