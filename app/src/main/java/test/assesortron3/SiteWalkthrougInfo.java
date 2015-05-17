package test.assesortron3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import test.objects.Project;
import test.objects.WalkThrough;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SiteWalkthrougInfo.OnInfoFragListener} interface
 * to handle interaction events.
 * Use the {@link SiteWalkthrougInfo#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SiteWalkthrougInfo extends Fragment implements test.assesortron3.TabFragment {
    WalkThrough walkThrough;
    ImageButton imageButton;
    Project project;
    Spinner floor, trade, progress;
    List<String> floorList = new ArrayList<String>();
    List<String> tradeList = new ArrayList<String>();
    List<String> progressList = new ArrayList<String>();

    private static final String TAB_NAME = "info";

    private OnInfoFragListener mListener;

    public SiteWalkthrougInfo newInstance() {
        SiteWalkthrougInfo fragment = new SiteWalkthrougInfo();
        return fragment;
    }
    public SiteWalkthrougInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walkthrough_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            imageButton = (ImageButton) getView().findViewById(R.id.walk_through_image);
            ViewGroup.LayoutParams imageButtonLayoutParams = imageButton.getLayoutParams();
            Log.i("info image layout","w- " +imageButtonLayoutParams.width + " h- " + imageButtonLayoutParams.height);
            //imageButtonLayoutParams.height = imageButtonLayoutParams.width;
           // imageButton.setImageURI(picture);
        }
            setVariables();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnInfoFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInfoFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setVariables() {
        floor = (Spinner) getView().findViewById(R.id.spinner_floor);
        trade = (Spinner) getView().findViewById(R.id.spinner_trade);
        progress = (Spinner) getView().findViewById(R.id.spinner_progress);


        floorList = new LinkedList<String>();
        for (int i = project.getNumBasementFloors(); i > 0; i--) {
            floorList.add("b" + i);
        }
        for (int i = 1; i < project.getNumAGFloors(); i++) {
            floorList.add(i + "");
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, floorList);
        floor.setAdapter(arrayAdapter);

        tradeList = new LinkedList<String>();
        tradeList.add("Demo");
        tradeList.add("Framing");
        tradeList.add("Plumbing- rough");
        tradeList.add("Plumbing- finish");
        tradeList.add("Electical");

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, tradeList);
        trade.setAdapter(arrayAdapter1);

        progressList = new LinkedList<String>();
        progressList.add("yet to commence");
        progressList.add("commenced");
        progressList.add("25%");
        progressList.add("In Progress");
        progressList.add("75%");
        progressList.add("Substantially Complete");

        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, progressList);
        progress.setAdapter(arrayAdapter2);


        if (walkThrough.getFloor() != null) {
            for (int i = 0; i < floorList.size(); i++) {
                if (walkThrough.getFloor().equals(floorList.get(i))) {
                    floor.setSelection(i);
                }
            }
        }
        if (walkThrough.getTrade() != null) {
            for (int i = 0; i < tradeList.size(); i++) {
                if (walkThrough.getTrade().equals(tradeList.get(i))) {
                    trade.setSelection(i);
                }
            }
        }
        if (walkThrough.getProgress() != null) {
            for (int i = 0; i < progressList.size(); i++) {
                if (walkThrough.getProgress().equals(progressList.get(i))) {
                    progress.setSelection(i);
                }
            }
        }
    }




    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public String getTabName() {
        return TAB_NAME;
    }

    @Override
    public void setFields(Object...object) {
        if (object[0] instanceof WalkThrough) {
            walkThrough = (WalkThrough) object[0];
        }
        if (object[1] instanceof Project) {
                project = (Project) object[1];
         }
        if (getView() != null) {
            setVariables();
        }
        }


    @Override
    public void getValues() {
        if (mListener != null) {
            mListener.getInfo(floor.getSelectedItem().toString(),trade.getSelectedItem().toString(), progress.getSelectedItem().toString());
        }
    }

    public interface OnInfoFragListener {
        // TODO: Update argument type and name
        public void getInfo(String floor, String subcontractor, String progress);
    }
}
