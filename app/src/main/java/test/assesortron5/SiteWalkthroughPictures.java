package test.assesortron5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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

import test.adapters.PictureListAdapter;
import test.objects.WalkThrough;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link test.assesortron5.SiteWalkthroughPictures.OnPictureFragListener} interface
 * to handle interaction events.
 * Use the {@link SiteWalkthroughPictures#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SiteWalkthroughPictures extends Fragment implements test.assesortron5.TabFragment {
    WalkThrough walkThrough;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAB_NAME = "pictures";

    private List<Uri> pictures = new ArrayList<Uri>();
    ListView pictureList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walkthrough_pictures, container, false);

        Button action = (Button)view.findViewById(R.id.walk_through_camera);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImageAction();
            }
        });

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

    public void captureImageAction() {

        //TODO

        //this should be stored in the harddrive, not in with the pictures folder...

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Site Images");
        imagesFolder.mkdirs(); // <----
        int i = imagesFolder.listFiles().length;
        File image = new File(imagesFolder, "project- image_"+i +".jpg");
        Uri fileUri = Uri.fromFile(image);
        intent.putExtra("Uri", fileUri);
        // start the image capture Intent
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(getActivity(), "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();

                //TODO
                //this needs to be changed to walkthroguh.addSitePicture....so far the walkthrough objects havent been made out of this screen
                // so im just going to use this to test using the project object. the project picture methods would be used for a cove page shot or something, not totally neccessary
                pictures.add(data.getData());
                setPictureList();
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

    public interface OnPictureFragListener {
        public void getPictures(List<Uri> uri);
    }
}
