package test.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by otf on 8/3/15.
 */
public class SpeechToTextFragment extends Fragment {
    public static final int SPEECH_TEXT_RESULTS = 23;
    SpeechToTextFragmentCallback parent;

    public SpeechToTextFragment() {
    }

    public static SpeechToTextFragment newInstance(SpeechToTextFragmentCallback parent) {
        SpeechToTextFragment speechToTextFragment = new SpeechToTextFragment();
        speechToTextFragment.setParent(parent);
        return speechToTextFragment;
    }

    private void setParent(SpeechToTextFragmentCallback parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        if (checkIfPresent()) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Notes!");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, SPEECH_TEXT_RESULTS);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SpeechToTextFragmentCallback && activity == null) {
            parent = (SpeechToTextFragmentCallback)activity;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_TEXT_RESULTS) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                parent.setSpeechText(results.get(0));
            }
        }
    }

    private boolean checkIfPresent() {
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(getActivity(), "Voice recognizer not present", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public interface SpeechToTextFragmentCallback {
        public void setSpeechText(String speechText);
    }

}
