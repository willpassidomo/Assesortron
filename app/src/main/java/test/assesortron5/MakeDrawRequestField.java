package test.assesortron5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import test.objects.DrawRequestItem;

public class MakeDrawRequestField extends Fragment {
    MakeDrawRequest.MakeDrawRequestInterface parentActivity;
    String type;

    TextView subcontractorNameTV, amountTV, descriptionTV;
    EditText subcontractorNameEdit, amountEdit, descriptionEdit;
    ViewSwitcher subcontractoNameSwitcher, amountSwitcher, descriptionSwitcher;
    CheckBox executedCB;

    Button actionButton;

    DrawRequestItem drawRequestItem;

    List<ViewSwitcher> switchers;

    public static MakeDrawRequestField newInstance(MakeDrawRequest.MakeDrawRequestInterface parentActivity, String type) {
        MakeDrawRequestField drawRequestField = new MakeDrawRequestField();
        drawRequestField.setType(type);
        drawRequestField.setParentActivity(parentActivity);
        parentActivity.getActivity().setTitle("New " + DrawRequestItem.getTitle(type));
        return drawRequestField;
    }

    public static MakeDrawRequestField newInstance(MakeDrawRequest.MakeDrawRequestInterface parentActivity, DrawRequestItem drawRequestItem) {
        MakeDrawRequestField drawRequestField = new MakeDrawRequestField();
        drawRequestField.setParentActivity(parentActivity);
        drawRequestField.setDrawRequestItem(drawRequestItem);
        drawRequestField.setType(drawRequestItem.getType());
        parentActivity.getActivity().setTitle("New " + DrawRequestItem.getTitle(drawRequestItem.getType()));
        return drawRequestField;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_draw_request_item, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            setVariables();
        }
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
        actionButton = (Button)getView().findViewById(R.id.change_order_action_button);

        subcontractorNameTV = (TextView) getView().findViewById(R.id.change_order_subcontractor);
        amountTV = (TextView) getView().findViewById(R.id.change_order_amount);
        descriptionTV = (TextView) getView().findViewById(R.id.change_order_description);

        subcontractorNameEdit = (EditText) getView().findViewById(R.id.edit_change_order_subcontractor);
        amountEdit = (EditText) getView().findViewById(R.id.edit_change_order_amount);
        descriptionEdit = (EditText) getView().findViewById(R.id.edit_change_order_description);

        subcontractoNameSwitcher = (ViewSwitcher) getView().findViewById(R.id.switcher_change_order_subcontractor);
        amountSwitcher = (ViewSwitcher) getView().findViewById(R.id.switcher_change_order_amount);
        descriptionSwitcher = (ViewSwitcher) getView().findViewById(R.id.switcher_change_order_description);

        executedCB = (CheckBox) getView().findViewById(R.id.check_box_executed);

        switchers = new ArrayList<ViewSwitcher>();
        switchers.add(subcontractoNameSwitcher);
        switchers.add(amountSwitcher);
        switchers.add(descriptionSwitcher);

        if (drawRequestItem != null) {
            viewChangeOrder();
        } else {
            newDrawRequestField(type);
        }
    }


    //TODO
    //this needs to go, the fragment needs to be returned before the variables can be set, so the action
    //of "actionButton" needs to be determined by the presence or lack of an instantiated "walkthrough" object
    public void newDrawRequestField(final String type) {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitChangeOrder(type);
            }
        });
    }

    public void viewChangeOrder() {
        actionButton.setText("edit");

        amountTV.setText(drawRequestItem.getAmount()+"");
        subcontractorNameTV.setText(drawRequestItem.getSubContractor());
        descriptionEdit.setText(drawRequestItem.getDescription());

        for (ViewSwitcher vs: switchers) {
            vs.showNext();
        }

        actionButton.setOnClickListener(editableOnClickListener());
    }

    public OnClickListener editableOnClickListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {

                amountEdit.setText(drawRequestItem.getAmount()+"");
                subcontractorNameEdit.setText(drawRequestItem.getSubContractor());
                descriptionEdit.setText(drawRequestItem.getDescription());

                editChangeOrder();
            }
        };
        return listener;


    }

    public void editChangeOrder() {
        actionButton.setText("submit edit");
        actionButton.setOnClickListener(submitEditOnClickListener());

    }

    public OnClickListener submitEditOnClickListener() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                drawRequestItem.setAmount(amountEdit.getText().toString());
                drawRequestItem.setDescription(descriptionEdit.getText().toString());
                drawRequestItem.setSubContractor(subcontractorNameEdit.getText().toString());

                parentActivity.submitDrawRequestItem(drawRequestItem);
            }
        };
        return listener;
    }

    public void submitChangeOrder(String type) {
        drawRequestItem = new DrawRequestItem(type);
        drawRequestItem.setAmount(amountEdit.getText().toString());
        drawRequestItem.setDescription(descriptionEdit.getText().toString());
        drawRequestItem.setSubContractor(subcontractorNameEdit.getText().toString());
        drawRequestItem.setType(type);
        parentActivity.submitDrawRequestItem(drawRequestItem);
    }

    public void setDrawRequestItem(DrawRequestItem drawRequestItem) {
        this.drawRequestItem = drawRequestItem;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParentActivity(MakeDrawRequest.MakeDrawRequestInterface parentActivity) {
        this.parentActivity = parentActivity;
    }
}
