package test.assesortron5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import test.persistence.DataBaseStorage;
import test.superActivities.SuperProject;
import test.superActivities.SuperSiteVisit;
import test.superActivities.SuperUser;


public class UserSelect extends Activity {
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);

        final Context context = this;

    Button test = (Button)findViewById(R.id.test_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SuperUser.class);
                startActivity(intent);
            }
        });

        Button testNewLogin = (Button)findViewById(R.id.test_login);
        testNewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, test.superActivities.Login.UserSelect.class);
                startActivity(intent);
            }
        });

        //TEMP
        Intent intent = new Intent(context, test.superActivities.Login.UserSelect.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
