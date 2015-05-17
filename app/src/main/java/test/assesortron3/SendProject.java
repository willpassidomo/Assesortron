package test.assesortron3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import assesortron.assesortronTaskerAPI.AssesortronTaskerAPI;
import assesortron.assesortronTaskerAPI.AssesortronTaskerAPI.TaskerAPI.SendProjectExcel;
import assesortron.assesortronTaskerAPI.model.ChangeOrderDTO;
import assesortron.assesortronTaskerAPI.model.DrawRequestDTO;
import assesortron.assesortronTaskerAPI.model.ProjectDTO;
import assesortron.assesortronTaskerAPI.model.StringWrapper;
import assesortron.assesortronTaskerAPI.model.WalkThroughDTO;
import test.Network.AppConstants;
import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.WalkThrough;
import test.persistence.Storage;


public class SendProject extends Activity {
    Context context;
    Project project;

    TextView response;
    EditText email;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_project);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("id");
        //String email = intent.getStringExtra("email");
        project = Storage.getProjectById(this, projectId);

        context = this.getApplicationContext();

        setFields();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFields() {
        response = (TextView)this.findViewById(R.id.response_field);
        email = (EditText)this.findViewById(R.id.email_field);
        submit = (Button)this.findViewById(R.id.send_project);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProject(project, email.getText().toString());
            }
        });
    }

    public void sendProject(Project project, String email) {
        final String emailAddress = email;
        ProjectDTO projectDTO = getDTO(project);
        AsyncTask<ProjectDTO, Void, StringWrapper> sendProject = new AsyncTask<ProjectDTO, Void, StringWrapper>() {
            @Override
            protected StringWrapper doInBackground(ProjectDTO... params) {
                AssesortronTaskerAPI atAPI = AppConstants.getAssServiceHandle();
                try {

                    Log.e("project param", params[0].getName());
//                    List<String> strings = new ArrayList<>();
//                    strings.add("hello");
//                    strings.add("worlds");
//                    strings.add("hi");
//                    SubmitTestList subTestList = atAPI.taskerAPI().submitTestList(strings);
//                    String string = subTestList.execute();
                    Log.i("walkthroughs #", params[0].getWalkThroughs().size()+"");
                    for(WalkThroughDTO wD: params[0].getWalkThroughs()) {
                        Log.i("WT floor, subcon", wD.getFloor() + " - " + wD.getTrade());
                    }
                    SendProjectExcel submit = atAPI.taskerAPI().sendProjectExcel(emailAddress, params[0]);
                    StringWrapper string = submit.execute();
                    return string;

                } catch (IOException e) {
                    //Toast.makeText(context, "submit project failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("network call", "Shit failed brah");
                }
                 return new StringWrapper().setString("error");
            }

            @Override
            protected void onPostExecute(StringWrapper string) {
                response.setText(string.getString());
            }
            };
        sendProject.execute(projectDTO);
    }

    private void setResponse(String string) {
        response.setText(string);
    }

    private ProjectDTO getDTO(Project project) {
        ProjectDTO pDTO = new ProjectDTO();
        pDTO.setName(project.getName());
        pDTO.setActualCompletionDate(project.getActualCompletionDate());
        pDTO.setActualStartDate(project.getActualStartDate());
        pDTO.setInitialCompletionDate(project.getInitialCompletionDate());
        pDTO.setInitialStartDate(project.getInitialStartDate());
        pDTO.setAddress(project.getAddress());
        if (project.getContractAmount() != null) {
            pDTO.setContractAmountString(project.getContractAmount().toString());
        }
        pDTO.setHasOutdoorWork(project.isHasOutdoorWork());
        if (project.getDateCreated() != null) {
            pDTO.setDateCreatedString(project.getDateCreated().toString());
        }
        if (project.getLoanAmount() != null) {
            pDTO.setLoanAmountString(project.getLoanAmount().toString());
        }
        pDTO.setSquareFeet(project.getSquareFeet());

        List<WalkThroughDTO> walkThroughDTOList = new ArrayList<>();
        List<ChangeOrderDTO> changeOrderDTOs = new ArrayList<>();
        List<DrawRequestDTO> drawRequestDTOs = new ArrayList<>();
        for (SiteVisit sw : Storage.getSiteWalks(this, project.getId())) {
            for (WalkThrough wt : Storage.getWalkThroughBySiteWalkId(this, sw.getId())) {
                walkThroughDTOList.add(getDTO(wt));
            }
            DrawRequest dr = Storage.getDrawRequestBySiteWalkId(this, sw.getId());
                drawRequestDTOs.add(getDTO(dr));
    }
        pDTO.setWalkThroughs(walkThroughDTOList);
        pDTO.setChangeOrders(changeOrderDTOs);
        pDTO.setDrawRequests(drawRequestDTOs);
        return pDTO;
    }

    private WalkThroughDTO getDTO(WalkThrough wt) {
        WalkThroughDTO wtDTO = new WalkThroughDTO();
        wtDTO.setDate(new DateTime(wt.getDate()));
        wtDTO.setFloor(wt.getFloor());
        wtDTO.setId(wt.getId());
        wtDTO.setNote(wt.getNotes());
        wtDTO.setTrade(wt.getTrade());
        return wtDTO;
    }

    private ChangeOrderDTO getDTO(DrawRequestItem co) {
        ChangeOrderDTO coDTO = new ChangeOrderDTO();
        coDTO.setAmount(co.getAmount());
        coDTO.setDateSubmitted(new DateTime(co.getDateSubmitted()));
        coDTO.setDescription(co.getDescription());
        coDTO.setExecuted(co.isExecuted());
        coDTO.setSubContractor(co.getSubContractor());
        coDTO.setId(co.getId());
        return coDTO;
    }

    private DrawRequestDTO getDTO(DrawRequest dr) {
        DrawRequestDTO drDTO = new DrawRequestDTO();
//        if (dr.getCurrentRequest() != null) {
//            drDTO.setCurrentRequestString(dr.getCurrentRequest().toString());
//        }
//        if (dr.getMaxRequest() != null) {
//            drDTO.setMaxRequestString(dr.getMaxRequest().toString());
//        }
//        if (dr.getMinRequest() != null) {
//            drDTO.setMinRequestString(dr.getMinRequest().toString());
//        }
//        if (dr.getContigencyDraw() != null) {
//            drDTO.setContingencyDrawString(dr.getContigencyDraw().toString());
//        }
//        if (dr.getContingencyBalance() != null) {
//            drDTO.setContingencyBalanceString(dr.getContingencyBalance().toString());
//        }
//        if (dr.getBalanceToComplete() != null) {
//            drDTO.setBalanceToCompleteString(dr.getBalanceToComplete().toString());
//        }
        return drDTO;
    }
}


