package test.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.assesortron3.R;

/**
 * Created by otf on 7/13/15.
 */
public class SetListSelectionFragment extends Fragment {
    List<String> stringList;
    List<View> radioButtons = new ArrayList<>();
    Button editEntries;
    LinearLayout selectionOptionsContainer;

    public SetListSelectionFragment() {
        super();
    }

    public static SetListSelectionFragment getInstance(List<String> stringList) {
        SetListSelectionFragment sptf = new SetListSelectionFragment();
        sptf.setStringList(stringList);
        return sptf;
    }

    private void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstance) {
        super.onCreateView(inflater, v, savedInstance);

        View view = inflater.inflate(R.layout.fragment_set_project_trades, null);
        editEntries = (Button)view.findViewById(R.id.set_project_trades_edit_options);
        selectionOptionsContainer = (LinearLayout)view.findViewById(R.id.set_project_trades_list_container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        for (String s: stringList) {
            Log.i("SetListSelectionFrag", "making: " + s);
            selectionOptionsContainer.addView(getRadioButtonEntry(s));
        }
    }

    private View getRadioButtonEntry(String string) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_string_selection, null);
        TextView stringVal = (TextView)view.findViewById(R.id.list_string_stringval);
        stringVal.setText(string);
        radioButtons.add(view);
        return view;
    }

    public List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        for (View view: radioButtons) {
            RadioButton radioButton = (RadioButton)view.findViewById(R.id.list_string_radio_button);
            if (radioButton.isChecked()) {
                TextView s = (TextView)view.findViewById(R.id.list_string_stringval);
                selected.add(s.getText().toString());
            }
        }
        return selected;
    }
}
