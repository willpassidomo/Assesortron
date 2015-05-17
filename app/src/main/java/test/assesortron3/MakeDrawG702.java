package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import test.objects.DrawRequest;
import test.objects.Project;
import test.persistence.Storage;

public class MakeDrawG702 extends Activity {
    DrawRequest draw;

    Context context;
    Project project;
    String drawId;
    String projectId;

    TextView projectName;
    TextView amount;
    TextView minDraw;
    TextView maxDraw;
    TextView retainageRel;
    TextView contingencyDraw;
    TextView contingencyBalance;
    TextView balanceToComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._dep_activity_make_draw);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        projectId = intent.getStringExtra("id");
        project = Storage.getProjectById(this, projectId);

        setVariables();

        projectName.setText(project.getName());

        String type = intent.getStringExtra("new_or_edit");

        if(type.equals("new")) {
            newDraw();
        } else if(type.equals("edit")) {
            draw = Storage.getDrawRequestById(this, intent.getStringExtra("draw_request_id"));
            editDraw();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_draw, menu);
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

    public void setVariables() {
        context = this;
        projectName = (TextView) this.findViewById(R.id.label_project_name);
        amount = (TextView) this.findViewById(R.id.make_draw_current_request);
        maxDraw =  (TextView) this.findViewById(R.id.make_draw_max_request);
        minDraw = (TextView) this.findViewById(R.id.make_draw_min_request);
        retainageRel = (TextView) this.findViewById(R.id.make_draw_retainage_rel);
        contingencyDraw = (TextView) this.findViewById(R.id.make_draw_contingency_draw);
        contingencyBalance = (TextView) this.findViewById(R.id.make_draw_contingency_balance);
        balanceToComplete = (TextView) this.findViewById(R.id.make_draw_balance_to_complete);

//        maxDraw.setText(Storage.getMaxDrawRequest(this, projectId));
//        minDraw.setText(Storage.getMinDrawRequest(this, projectId));
//        retainageRel.setText(project.getRetainageRel()+"");

    }

    private void editDraw() {
//        amount.setText(draw.getCurrentRequest()+"");
//        maxDraw.setText(draw.getMaxRequest().toString()+"");
//        minDraw.setText(draw.getMinRequest().toString());
//        retainageRel.setText(draw.getRetainageRel().toString());
//        contingencyDraw.setText(draw.getContigencyDraw().toString());
//        contingencyBalance.setText(draw.getContingencyBalance().toString());
//        balanceToComplete.setText(draw.getBalanceToComplete().toString());

        Button submitButton = (Button) this.findViewById(R.id.submit);
        submitButton.setText("submit changes");
        submitButton.setOnClickListener(editSubmitListener());
    }

    public OnClickListener editSubmitListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO

                //updates the current Draw's request amount with the project

                //this will probabrly be a network call, or maybe this can only be done before the whole project is submitted,
                // if it waits until the end for submition..idk, but it will probably need some validation
                draw = getDrawRequestFromFields(draw);

                Storage.storeDrawRequest(context, projectId, draw);

                finish();

            }
        };
        return listener;
    }

    private void newDraw() {
        Button submitButton = (Button) this.findViewById(R.id.submit);
        submitButton.setOnClickListener(newDrawListener());
    }

    private OnClickListener newDrawListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO

                //will make a network call or at least store the new Draw information object
                calculate(view);

                draw = getDrawRequestFromFields(new DrawRequest());

                project.addDrawRequests(draw);
                Storage.storeDrawRequest(context, projectId, draw);



                Intent intent = new Intent(context, ProjectHomeScreen.class);
                intent.putExtra("id", project.getId());

                startActivity(intent);
            }
        };
        return listener;
    }

    public void calculate(View view) {
        //TODO

        //usig the value of Current Reg, and the current fields from project, calculate Contingency Draw, contingenty Balance and balance to complete
        //fill in Max Req to Dat, Min Req to Date and retainage Rel based off of Project (I think)
    }

    private DrawRequest getDrawRequestFromFields(DrawRequest draw) {
//        draw.setCurrentRequest(amount.getText().toString());
//        draw.setMaxRequest(maxDraw.getText().toString());
//        draw.setMinRequest(minDraw.getText().toString());
//        draw.setRetainageRel(retainageRel.getText().toString());
//        draw.setContingencyDraw(contingencyDraw.getText().toString());
//        draw.setContingencyBalance(contingencyBalance.getText().toString());
//        draw.setBalanceToComplete(balanceToComplete.getText().toString());

        return draw;
    }

}
