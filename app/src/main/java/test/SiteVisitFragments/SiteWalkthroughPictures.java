package test.SiteVisitFragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import test.Fragments.CameraPictureFragment;
import test.adapters.PictureListAdapter;
import test.assesortron5.R;
import test.objects.WalkThrough;



public class SiteWalkthroughPictures extends Fragment implements TabFragment, CameraPictureFragment.CameraPictureFragmentCallback {
    WalkThrough walkThrough;
    private static final String TAB_NAME = "pictures";

    private Button action;
    private List<String> pictures = new ArrayList<String>();
    ListView pictureList;


    private OnPictureFragListener mListener;

    public SiteWalkthroughPictures newInstance() {
        SiteWalkthroughPictures fragment = new SiteWalkthroughPictures();
        return fragment;
    }
    public SiteWalkthroughPictures() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walkthrough_pictures, container, false);

        action = (Button)view.findViewById(R.id.walk_through_camera);

        pictureList = (ListView) view.findViewById(R.id.walk_through_picture_list);
        setPictureList();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstances) {
        super.onActivityCreated(savedInstances);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPictureFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void setPictureList() {
        PictureListAdapter pla = new PictureListAdapter(getActivity(), pictures);
        pictureList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        pictureList.setAdapter(pla);
    }

    private void setListener() {
        if (action != null) {
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCamera();
                }
            });
        }
    }

    private void startCamera() {
        getActivity()
                .getFragmentManager()
                .beginTransaction()
                .add(CameraPictureFragment.newInstance(this), "")
                .commit();
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
            setListener();
            pictures = walkThrough.getPictures();
            if (getView() != null) {
                setPictureList();
            }
        }
    }

    @Override
    public void getValues() {
        if (mListener != null) {
            mListener.getPictures(pictures);
        }
    }

    @Override
    public void returnImageId(String imageId) {
        pictures.add(imageId);
    }

    public interface OnPictureFragListener {
        public void getPictures(List<String> uri);
    }
}
