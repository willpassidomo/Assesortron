package test.superActivities.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import test.assesortron5.R;
import test.persistence.Constants;
import test.persistence.Storage;
import test.superActivities.SuperUser;

/**
 * Created by otf on 7/28/15.
 */
public class UserSelect extends Activity {
    public static final int REGISTER_NEW_USER = 101;

    Context context = this;
    Button login, register;
    CheckBox saveInfo;
    TextView password;
    AutoCompleteTextView username;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_user_select2);

        if (Storage.isLoggedIn(this)) {
            String loggedInId = Storage.getLoggedInId();
            if (Storage.getUserById(this, loggedInId) != null) {
                completeLogin(loggedInId);
            } else {
                Toast.makeText(this, "stored ID is not a valid ID",Toast.LENGTH_SHORT).show();
            }
        }

        setVariables();
    }

    private void setVariables() {
        login = (Button)findViewById(R.id.user_select_login);
        register = (Button)findViewById(R.id.user_select_register);
        saveInfo = (CheckBox)findViewById(R.id.user_select_save_info_q);
        username = (AutoCompleteTextView)findViewById(R.id.user_select_username);
        password = (TextView)findViewById(R.id.user_select_password);

        login.setOnClickListener(loginOnClickListener());
        register.setOnClickListener(registerOnClickListener());

        username.setAdapter(userNameAdapter());
        username.setThreshold(1);

    }

    private View.OnClickListener loginOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = Storage.login(context, username.getText().toString(), password.getText().toString());
                if (userId == null || userId.equals("")) {
                    new MaterialDialog.Builder(context)
                            .title("Login Failed")
                            .content("Incorrect Username and/or Password")
                            .positiveText("ok")
                            .show();
                } else {
                    completeLogin(userId);
                }
            }
        };
    }

    private View.OnClickListener registerOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Register.class);
                startActivityForResult(intent, REGISTER_NEW_USER);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int result, Intent data) {
        if (requestCode == REGISTER_NEW_USER) {
            if (result == Register.COMPLETED) {
                completeLogin(data.getStringExtra(Constants.USER_ID));
            }
        }
    }

    private void completeLogin(String userId) {
        if (saveInfo != null && saveInfo.isChecked()) {
            Storage.setLoggedInId(this, userId);
        }
        Intent intent = new Intent(this, SuperUser.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }

    private ArrayAdapter<String> userNameAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Storage.getUserNames(context));
    }
}
