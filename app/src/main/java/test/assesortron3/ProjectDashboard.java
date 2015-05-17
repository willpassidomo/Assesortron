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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.objects.Project;
import test.persistence.Constants;
import test.persistence.Storage;

public class ProjectDashboard extends Activity {
    Project project;

    private boolean edit = false;
    private Context context;
    private LinearLayout linearLayout;
    private List<ViewSwitcher> viewSwitchers;

    private TextView name;
    private TextView address;
    private TextView loanAmount;
    private TextView contractAmount;
    private TextView retainageRel;
    private TextView initStartDate;
    private TextView initCompDate;
    private TextView actualCompDate;
    private TextView actualStartDate;
    private TextView date;
    private TextView floors;
    private TextView basementFloors;
    private TextView sqrFt;

    private Button editButton;

    private EditText editName;
    private EditText editAddress;
    private EditText editContractAmount;
    private EditText editLoatAmount;
    private EditText editRetainageRel;
    private EditText editInitCompDate;
    private EditText editInitStart;
    private EditText editActCompDate;
    private EditText editActStart;
    private EditText editFloors;
    private EditText editBasementFloors;
    private EditText editSqrFt;

    private CheckBox hasOutSideWork;

    private ViewSwitcher switcherName;
    private ViewSwitcher switchAddress;
    private ViewSwitcher switchLoanAmount;
    private ViewSwitcher switchContractAmount;
    private ViewSwitcher switchRetainageRel;
    private ViewSwitcher switchInitCompDate;
    private ViewSwitcher switchInitStart;
    private ViewSwitcher switchActComDate;
    private ViewSwitcher switchActStartDate;
    private ViewSwitcher switchFloors;
    private ViewSwitcher switchBasementFloors;
    private ViewSwitcher switchSqrFt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_dashboard);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        linearLayout = (LinearLayout)this.findViewById(R.id.dashboard_layout);
        viewSwitchers = new ArrayList<ViewSwitcher>();

        setVariables();

        //DOESNT WORK, NEEDS TO BE RECURSIVE (but already did it manually in setVariables
//        for (int i = 0; i < linearLayout.getChildCount(); i++ ) {
//            if (linearLayout.getChildAt(i) instanceof ViewSwitcher)
//                viewSwitchers.add((ViewSwitcher) linearLayout.getChildAt(i));
//        }

        Intent intent = getIntent();
        String type = intent.getStringExtra(Constants.NEW_OR_EDIT);

        if (type.equals(Constants.NEW)) {
            newProject();

        } else if (type.equals(Constants.EDIT)) {
            String id = intent.getStringExtra(Constants.PROJECT_ID);
            project = Storage.getProjectById(this, id);
            viewProject();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_dashboard, menu);
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

    private void setVariables() {
        name = (TextView) this.findViewById(R.id.project_name);
        address = (TextView) this.findViewById(R.id.project_address);
        loanAmount = (TextView) this.findViewById(R.id.project_loan_amount);
        contractAmount = (TextView) this.findViewById(R.id.project_est_contract_amount);
        retainageRel = (TextView) this.findViewById(R.id.project_retainage_rel);
        initStartDate = (TextView) this.findViewById(R.id.project_initial_start);
        initCompDate = (TextView) this.findViewById(R.id.project_initial_completion);
        actualCompDate = (TextView) this.findViewById(R.id.project_actual_completion);
        actualStartDate = (TextView) this.findViewById(R.id.project_actual_start);
        date = (TextView) this.findViewById(R.id.project_date);
        floors = (TextView) this.findViewById(R.id.project_num_floors);
        basementFloors = (TextView) this.findViewById(R.id.project_basement_floors);
        sqrFt = (TextView) this.findViewById(R.id.project_sq_ft);
        editButton = (Button) this.findViewById(R.id.edit_button);

        editName = (EditText) this.findViewById(R.id.edit_p_name);
        editAddress = (EditText) this.findViewById(R.id.edit_p_address);
        editContractAmount = (EditText) this.findViewById(R.id.edit_p_contact_amount);
        editRetainageRel = (EditText) this.findViewById(R.id.edit_p_retainage_rel);
        editLoatAmount = (EditText) this.findViewById(R.id.edit_p_loan_amount);
        editInitCompDate = (EditText) this.findViewById(R.id.edit_inital_completion);
        editInitStart = (EditText) this.findViewById(R.id.edit_p_initial_start);
        editActCompDate = (EditText) this.findViewById(R.id.edit_p_actual_completion);
        editActStart = (EditText) this.findViewById(R.id.edit_p_actual_start);
        editFloors = (EditText) this.findViewById(R.id.edit_p_num_floors);
        editBasementFloors = (EditText) this.findViewById(R.id.edit_p_basement_floors);
        editSqrFt = (EditText) this.findViewById(R.id.edit_p_sq_ft);

        hasOutSideWork = (CheckBox) this.findViewById(R.id.has_site_work);

        switcherName = (ViewSwitcher) this.findViewById(R.id.swicher_project_name);
        switchAddress = (ViewSwitcher) this.findViewById(R.id.switcher_address);
        switchLoanAmount = (ViewSwitcher) this.findViewById(R.id.switcher_loan_amount);
        switchRetainageRel = (ViewSwitcher) this.findViewById(R.id.switcher_retainage_rel);
        switchContractAmount = (ViewSwitcher) this.findViewById(R.id.switcher_contract_amount);
        switchInitCompDate = (ViewSwitcher) this.findViewById(R.id.switcher_init_comp_date);
        switchInitStart = (ViewSwitcher) this.findViewById(R.id.switcher_init_start_date);
        switchActComDate = (ViewSwitcher) this.findViewById(R.id.switcher_actual_comp_date);
        switchActStartDate = (ViewSwitcher) this.findViewById(R.id.switcher_actual_start_date);
        switchFloors = (ViewSwitcher) this.findViewById(R.id.switcher_num_floors);
        switchBasementFloors = (ViewSwitcher) this.findViewById(R.id.switcher_num_basement_floors);
        switchSqrFt = (ViewSwitcher) this.findViewById(R.id.switcher_sq_ft);

        viewSwitchers.add(switcherName);
        viewSwitchers.add(switchAddress);
        viewSwitchers.add(switchLoanAmount);
        viewSwitchers.add(switchContractAmount);
        viewSwitchers.add(switchRetainageRel);
        viewSwitchers.add(switchInitCompDate);
        viewSwitchers.add(switchInitStart);
        viewSwitchers.add(switchActComDate);
        viewSwitchers.add(switchActStartDate);
        viewSwitchers.add(switchFloors);
        viewSwitchers.add(switchBasementFloors);
        viewSwitchers.add(switchSqrFt);

    }

    public void viewProject() {
        setTextNull(name,project.getName());
        if(project.getAddress()!=null) {
            setTextNull(address, project.getAddress());
        }
        if(project.getLoanAmount()!= null) {
            setTextNull(loanAmount, project.getLoanAmount().toString());
        }
        if(project.getContractAmount()!=null) {
            setTextNull(contractAmount, project.getContractAmount().toString());
        }
        setTextNull(retainageRel, project.getRetainageRel() + "");
        if(project.getInitialStartDate()!= null) {
            setTextNull(initStartDate, project.getInitialStartDate().toString());
        }
        if(project.getInitialCompletionDate()!=null) {
            setTextNull(initCompDate, project.getInitialCompletionDate().toString());
        }
        if(project.getActualCompletionDate() != null) {
            setTextNull(actualCompDate, project.getActualCompletionDate().toString());
            }
        if(project.getActualStartDate()!= null) {
            setTextNull(actualStartDate, project.getActualStartDate().toString());
            }
        setTextNull(date, new Date().toString());
        setTextNull(floors, project.getNumAGFloors() + "");
        setTextNull(basementFloors, project.getNumBasementFloors()+"");
        setTextNull(sqrFt, project.getSquareFeet() + "");

        editButton.setText("edit");
        editButton.setOnClickListener(editSwitcherListener());
    }

    public void setTextNull(TextView textView, String string) {
        if (string != null) {
            textView.setText(string);
        }
    }

    public OnClickListener editSwitcherListener() {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (edit) {
                    setFieldsFromEdit();
                } else {
                    setEditFields();
                }

            }
        };
        return listener;

    }

    private void setEditFields() {
        for (ViewSwitcher switcher: viewSwitchers) {
            switcher.showNext();
        }

        editName.setText(name.getText());
        editAddress.setText(address.getText());
        editLoatAmount.setText(loanAmount.getText());
        editContractAmount.setText(contractAmount.getText());
        editRetainageRel.setText(retainageRel.getText());
        editInitCompDate.setText(initCompDate.getText());
        editInitStart.setText(initStartDate.getText());
        editActCompDate.setText(actualCompDate.getText());
        editActStart.setText(actualStartDate.getText());
        editFloors.setText(floors.getText());
        editBasementFloors.setText(basementFloors.getText());
        editSqrFt.setText(sqrFt.getText());

        editButton.setText("save changes");
        edit = true;


    }

    private void setFieldsFromEdit() {
        for (ViewSwitcher switcher: viewSwitchers) {
            switcher.showNext();
        }

        project.setName(editName.getText().toString());
        project.setLoanAmount(editLoatAmount.getText().toString());
        project.setContractAmount(editContractAmount.getText().toString());
        project.setRetainageRel(editRetainageRel.getText().toString());
        project.setInitialCompletionDate(editInitCompDate.getText().toString());
        project.setInitialStartDate(editInitStart.getText().toString());
        project.setAddress(editAddress.getText().toString());
        project.setNumAGFloors(editFloors.getText().toString());
        project.setNumBasementFloors(editBasementFloors.getText().toString());
        project.setHasOutdoorWork(hasOutSideWork.isChecked());
        project.setActualCompletionDate(editActCompDate.getText().toString());
        project.setActualStartDate(editActStart.getText().toString());
        project.setSquareFeet(editSqrFt.getText().toString());

        Storage.storeProject(this, project);

        viewProject();
        editButton.setText("edit");
        edit = false;
    }

    public void newProject() {
        edit = true;
        for(ViewSwitcher viewSwitcher: viewSwitchers) {
            viewSwitcher.showNext();
        }
        Button editButton = (Button) this.findViewById(R.id.edit_button);
        editButton.setText("Save Project");
        editButton.setOnClickListener(submitProjectListener());
    }

    public OnClickListener submitProjectListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO

                //this is where the submit projet button should gather and package the proper fields from the enty
                // screen (view, the instance of projectdashboard, and make a network call which will call the create Project method
                //for the User.


                if (editName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Project must have a name", Toast.LENGTH_SHORT).show();
                } else {
                    createProject();
                }
            }

            private void createProject() {
                    project = new Project();

                    project.setName(editName.getText().toString());
                    project.setDateCreated(new Date());
                    project.setLoanAmount(editLoatAmount.getText().toString());
                    project.setContractAmount(editContractAmount.getText().toString());
                    project.setInitialCompletionDate(editInitCompDate.getText().toString());
                    project.setInitialStartDate(editInitStart.getText().toString());
                    project.setAddress(editAddress.getText().toString());
                    project.setNumAGFloors(editFloors.getText().toString());
                    project.setNumBasementFloors(editBasementFloors.getText().toString());
                    project.setHasOutdoorWork(hasOutSideWork.isChecked());
                    project.setActualCompletionDate(editActCompDate.getText().toString());
                    project.setActualStartDate(editActStart.getText().toString());
                    project.setSquareFeet(editSqrFt.getText().toString());
                    project.setUser(Storage.getUser());

                    //maybe instead of making the network call now, it can just store the info locally. once the session is completed, then the
                    //network call could be made.. that might be better if this program isn't dependent on network connectivity
                    // ..the best practice in this case, now i think, is to have a method which receives an alert that the network call failed then
                    //saves the data to the local database

                    Storage.storeProject(context, project);

                    Intent intent = new Intent(context, ProjectHomeScreen.class);

                    intent.putExtra("id", project.getId());
                    startActivity(intent);

            }
        };
        return listener;
    }


}
