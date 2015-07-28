package test.SiteVisitFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import test.assesortron5.R;
import test.drawers.DrawerActivtyListener;
import test.objects.DrawRequest;
import test.objects.WalkThrough;
import test.persistence.Storage;
import test.superActivities.FragmentDrawerListener;

/**
 * Created by otf on 7/26/15.
 */
public class DrawRequestFragment extends Fragment {
    String siteWalkId;
    FragmentDrawerListener fragmentDrawerListener;
    DrawRequestFragmentListener drawRequestFragmentListener;
    DrawRequest drawRequest;
    EditText currentRequest, recommendation, conditions;
    Button submit;

    public DrawRequestFragment() {}

    public static DrawRequestFragment newInstance(FragmentDrawerListener parentListener, String siteWalkId, DrawRequest drawRequest) {
        DrawRequestFragment drawRequestFragment = new DrawRequestFragment();
        drawRequestFragment.setDrawRequest(drawRequest);
        drawRequestFragment.setParentListener(parentListener);
        drawRequestFragment.setSiteWalkId(siteWalkId);
        return drawRequestFragment;
    }

    public static DrawRequestFragment newInstance(DrawRequestFragmentListener parentListener, DrawRequest drawRequest) {
        DrawRequestFragment drawRequestFragment = new DrawRequestFragment();
        drawRequestFragment.setDrawRequest(drawRequest);
        drawRequestFragment.setParentListener(parentListener);
        return drawRequestFragment;
    }

    private void setDrawRequest(DrawRequest drawRequest) {
        this.drawRequest = drawRequest;
    }

    private void setParentListener(FragmentDrawerListener parentListener) {
        this.fragmentDrawerListener = parentListener;
    }

    private void setParentListener(DrawRequestFragmentListener parentListener) {
        this.drawRequestFragmentListener = parentListener;
    }

    private void setSiteWalkId(String siteWalkId) {
        this.siteWalkId = siteWalkId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstance) {
        super.onCreateView(inflater, vg, savedInstance);
        View view = inflater.inflate(R.layout.fragment_make_draw_request, null);
        currentRequest = (EditText)view.findViewById(R.id.make_draw_current_request);
        recommendation = (EditText)view.findViewById(R.id.make_draw_current_recommendation);
        conditions = (EditText)view.findViewById(R.id.make_draw_conditions);

        submit = (Button)view.findViewById(R.id.make_draw_request_submit);
        submit.setOnClickListener(submitListener());
        setValues();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }

    private void setValues() {
        currentRequest.setText(drawRequest.getCurrentRequest());
        recommendation.setText(drawRequest.getCurrentRecmomendation());
        conditions.setText(drawRequest.getConditions());
    }

    private View.OnClickListener submitListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawRequest.setConditions(conditions.getText().toString());
                drawRequest.setCurrentRecommendation(recommendation.getText().toString());
                drawRequest.setCurrentRequest(currentRequest.getText().toString());

                if (fragmentDrawerListener != null) {
                    Storage.storeDrawRequest(getActivity(), siteWalkId, drawRequest);
                    fragmentDrawerListener.done();
                }
                if (drawRequestFragmentListener != null) {
                    drawRequestFragmentListener.submitDrawRequest(drawRequest);
                }
            }
        };
    }

    public interface DrawRequestFragmentListener {
        public void submitDrawRequest(DrawRequest drawRequest);
    }
}
