package test.assesortron5;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import test.objects.WalkThrough;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SiteWalkthroughNote.OnNoteFragListener} interface
 * to handle interaction events.
 * Use the {@link SiteWalkthroughNote#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SiteWalkthroughNote extends Fragment implements test.assesortron5.TabFragment {
    private String TAB_NAME = "notes";

    private String oldNote;
    private TextView projectName;
    private EditText note;
    private WalkThrough walkThrough;


    private OnNoteFragListener parentListener;

    public SiteWalkthroughNote newInstance() {
        SiteWalkthroughNote fragment = new SiteWalkthroughNote();
        return fragment;
    }

    public SiteWalkthroughNote() {
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
        return inflater.inflate(R.layout.fragment_walk_through_note, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInst) {
        super.onActivityCreated(savedInst);

        note = (EditText) getView().findViewById(R.id.walk_through_note);
        if (walkThrough != null) {
            note.setText(walkThrough.getNotes());
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void getValues() {
        if (parentListener != null) {
            parentListener.onNoteEntered(note.getText().toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            parentListener = (OnNoteFragListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentListener = null;
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
            if (walkThrough.getNotes() != null && getView() != null) {
                note.setText(walkThrough.getNotes());
            }
        }
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
    public interface OnNoteFragListener {
        public void onNoteEntered(String note);
    }

}
