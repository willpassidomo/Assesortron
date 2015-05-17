package test.assesortron3;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.persistence.Constants;
import test.persistence.Storage;

public class MakeDraw extends Activity implements MakeDrawRequest.MakeDrawRequestInterface{
    String siteWalkId;

    MakeDrawRequest.MakeDrawRequestInterface context;
    Button goG702, goMakeDrawRequest;
    ViewSwitcher vs;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawRequest drawRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_draw);
        context = this;

        siteWalkId = getIntent().getStringExtra(Constants.SITE_VISIT_ID);

        drawRequest = Storage.getDrawRequestBySiteWalkId(this, siteWalkId);
        Log.i("drawRequestId- ", drawRequest.getId() + "\ndrawRequestItemCount- " + drawRequest.getItemList().size());
        if (drawRequest == null) {
            drawRequest = new DrawRequest();
            Toast.makeText(this, "new Draw Request Started",Toast.LENGTH_LONG);
        }



        goG702 = (Button) this.findViewById(R.id.draw_request_g702);
        goMakeDrawRequest = (Button) this.findViewById(R.id.draw_request_current_request);
        vs = (ViewSwitcher) this.findViewById(R.id.make_draw_viewSwitcher);

        fragmentManager = getFragmentManager();
        setListeners();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_draw, menu);
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

    private void setListeners() {
        goG702.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //fill this in when the G702 fragment is finished, like below
            }
        });
        goMakeDrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vs.showNext();
                MakeDrawRequest mdr = MakeDrawRequest.newInstance();
                mdr.setFields(context, drawRequest);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.make_draw_fragment, mdr);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public void submitDrawRequestItem(DrawRequestItem drawRequestItem) {
        drawRequest.addDrawRequestItem(drawRequestItem);
        Storage.storeDrawRequestItem(this, drawRequest.getId(), drawRequestItem);
        fragmentManager.popBackStack();
    }

    @Override
    public void submitDrawRequest(DrawRequest newDrawRequest) {
        newDrawRequest.addDrawRequestItems(drawRequest.getItemList());
        Storage.storeDrawRequest(this, siteWalkId, newDrawRequest);
        drawRequest = newDrawRequest;
        vs.showNext();
    }

    @Override
    public void displayNewTypeEntry(String type) {
        displayFragment(MakeDrawRequestField.newInstance(this, type));
    }

    @Override
    public void displayList(String type) {
        addFragment(DrawRequestItemList.newInstance(drawRequest.getItemList(type)));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public DrawRequest getDrawRequest() {
        return drawRequest;
    }

    private void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.make_draw_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void displayFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.make_draw_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
