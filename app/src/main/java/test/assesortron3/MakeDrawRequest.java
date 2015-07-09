package test.assesortron3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import test.objects.DrawRequest;
import test.objects.DrawRequestItem;


public class MakeDrawRequest extends Fragment {
    Context context;
    DrawRequest drawRequest;
    MakeDrawRequestInterface parentActivtiy;

    Button addChangeOrder, addRetainageRel, addStoredMaterials, addDrawFromContingency,
            addBuyout, submit;
    TextView viewChangeOrder, viewRetainageRel, viewStoredMaterials, viewDrawFromContingency,
            viewBuyout;
    TextView changeOrderCount, changeOrderTotal, storedMaterialsCount, storedMaterialsTotal,
            retainageRelCount, retainageRelTotal, drawContingencyCount, drawContingencyTotal,
            buyoutCount, buyoutTotal;
    EditText currentRequest, currentRecommendation, conditions;

    public MakeDrawRequest() {
    }

    public static MakeDrawRequest newInstance() {
        MakeDrawRequest makeDrawRequest = new MakeDrawRequest();
        return makeDrawRequest;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_draw_request, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            setVariables();
            setListeners();
            if (drawRequest != null) {
                setFields();
            }
        }
    }

    @Override
    public  void onResume() {
        super.onResume();
        if (parentActivtiy != null) {
            drawRequest = parentActivtiy.getDrawRequest();
            setFields();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_make_draw_request, menu);
//        return true;
//    }

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

    private void setVariables() {
        addChangeOrder = (Button) getView().findViewById(R.id.change_order_add_button);
        addRetainageRel = (Button) getView().findViewById(R.id.retainage_rel_add_button);
        addStoredMaterials = (Button) getView().findViewById(R.id.stored_materials_add_button);
        addDrawFromContingency = (Button) getView().findViewById(R.id.contingency_draw_add_button);
        addBuyout = (Button) getView().findViewById(R.id.buyout_add_button);
        currentRequest = (EditText) getView().findViewById(R.id.make_draw_current_request);
        currentRecommendation = (EditText) getView().findViewById(R.id.make_draw_current_recommendation);
        conditions = (EditText) getView().findViewById(R.id.make_draw_conditions);
        submit = (Button) getView().findViewById(R.id.make_draw_request_submit);
        viewChangeOrder = (TextView) getView().findViewById(R.id.make_draw_request_change_order_label);
        viewRetainageRel = (TextView) getView().findViewById(R.id.make_draw_request_retainage_rel_label);
        viewStoredMaterials = (TextView) getView().findViewById(R.id.make_draw_request_stored_material_label);
        viewDrawFromContingency = (TextView) getView().findViewById(R.id.make_draw_request_draw_contingency_label);
        viewBuyout = (TextView) getView().findViewById(R.id.make_draw_request_buyout_label);

        changeOrderCount = (TextView) getView().findViewById(R.id.change_order_count);
        changeOrderTotal = (TextView) getView().findViewById(R.id.change_order_total);
        storedMaterialsCount = (TextView) getView().findViewById(R.id.stored_material_count);
        storedMaterialsTotal = (TextView) getView().findViewById(R.id.stored_material_total);
        retainageRelCount = (TextView) getView().findViewById(R.id.retainage_rel_count);
        retainageRelTotal = (TextView) getView().findViewById(R.id.retainage_rel_total);
        drawContingencyCount = (TextView) getView().findViewById(R.id.contingency_draw_count);
        drawContingencyTotal = (TextView) getView().findViewById(R.id.contingency_draw_total);
        buyoutCount = (TextView) getView().findViewById(R.id.buyout_count);
        buyoutTotal = (TextView) getView().findViewById(R.id.buyout_total);
    }

    private void setFields() {
        changeOrderCount.setText(drawRequest.getItemList(DrawRequestItem.CHANGE_ORDER).size()+"");
        changeOrderTotal.setText(getTotal(drawRequest.getItemList(DrawRequestItem.CHANGE_ORDER)));
        storedMaterialsCount.setText(drawRequest.getItemList(DrawRequestItem.STORED_MATERIALS).size()+"");
        storedMaterialsTotal.setText(getTotal(drawRequest.getItemList(DrawRequestItem.STORED_MATERIALS)));
        retainageRelCount.setText(drawRequest.getItemList(DrawRequestItem.RETAINAGE_RELEASE).size()+"");
        retainageRelTotal.setText(getTotal(drawRequest.getItemList(DrawRequestItem.RETAINAGE_RELEASE)));
        drawContingencyCount.setText(drawRequest.getItemList(DrawRequestItem.NEW_DRAW_CONTINGENCY).size()+"");
        drawContingencyTotal.setText(getTotal(drawRequest.getItemList(DrawRequestItem.NEW_DRAW_CONTINGENCY)));
        buyoutCount.setText(drawRequest.getItemList(DrawRequestItem.BUYOUT).size()+"");
        buyoutTotal.setText(getTotal(drawRequest.getItemList(DrawRequestItem.BUYOUT)));

        currentRequest.setText(drawRequest.getCurrentRequest());
        currentRecommendation.setText(drawRequest.getCurrentRecmomendation());
        conditions.setText(drawRequest.getConditions());
    }

    private void setListeners() {
        addChangeOrder.setOnClickListener(newDrawRequestField(DrawRequestItem.CHANGE_ORDER));
        addBuyout.setOnClickListener(newDrawRequestField(DrawRequestItem.BUYOUT));
        addDrawFromContingency.setOnClickListener(newDrawRequestField(DrawRequestItem.NEW_DRAW_CONTINGENCY));
        addRetainageRel.setOnClickListener(newDrawRequestField(DrawRequestItem.RETAINAGE_RELEASE));
        addStoredMaterials.setOnClickListener(newDrawRequestField(DrawRequestItem.STORED_MATERIALS));

        viewChangeOrder.setOnClickListener(viewList(DrawRequestItem.CHANGE_ORDER));
        viewRetainageRel.setOnClickListener(viewList(DrawRequestItem.RETAINAGE_RELEASE));
        viewStoredMaterials.setOnClickListener(viewList(DrawRequestItem.STORED_MATERIALS));
        viewDrawFromContingency.setOnClickListener(viewList(DrawRequestItem.NEW_DRAW_CONTINGENCY));
        viewBuyout.setOnClickListener(viewList(DrawRequestItem.BUYOUT));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawRequest.setCurrentRequest(currentRequest.getText().toString());
                drawRequest.setCurrentRecommendation(currentRecommendation.getText().toString());
                drawRequest.setConditions(conditions.getText().toString());
                parentActivtiy.submitDrawRequest(drawRequest);
            }
        });
    }
    private View.OnClickListener newDrawRequestField(final String type) {
        //TODO
        //this should redirect to the proper list view for the type specified by the parameter "type" for
        //the DrawRequest
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    parentActivtiy.displayNewTypeEntry(type);
                } else {

                }
            }
        };
    }

    private View.OnClickListener viewList(final String type) {
        //TODO
        //set this up so it conditionally checks what the screen layout is and either sends the list to the right
        //if the view is large enough as a fragment or starts it as a seperate activity if small screen
        //for now, it is set up as a small screen only
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    parentActivtiy.displayList(type);
                } else {

                }
            }
        };
    }

    public void setFields(MakeDrawRequestInterface parentActivtiy, DrawRequest drawRequest) {
        this.parentActivtiy = parentActivtiy;
        this.drawRequest = drawRequest;
    }

    private String getTotal(List<DrawRequestItem> dri) {
        double total = 0.0;
        for (DrawRequestItem dr: dri) {
            total += dr.getAmount();
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(total);
    }

    public interface MakeDrawRequestInterface {
        public void displayList(String type);
        public void displayNewTypeEntry(String type);
        public void editEntry(String drawRequestItemId);
        public void submitDrawRequestItem(DrawRequestItem drawRequestItem);
        public void submitDrawRequest(DrawRequest drawRequest);
        public Activity getActivity();
        public DrawRequest getDrawRequest();
    }
}
