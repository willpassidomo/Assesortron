package test.UserFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.api.client.http.javanet.NetHttpTransport;

import test.assesortron5.R;
import test.objects.Address;
import test.objects.Project;
import test.persistence.State;
import test.persistence.Storage;

/**
 * Created by otf on 7/18/15.
 */
public class NewProjectRequired extends Fragment {
    ProjectListener projectListener;
    Project project;

    private EditText name, floors, basementFloors;
    private EditText streetAddress, city, zip;
    private AutoCompleteTextView state;
    private Button submit, takePicture;
    private ViewSwitcher viewSwitcher;
    private ImageView imageView;

    public NewProjectRequired() {}

    public static NewProjectRequired newInstance() {
        return new NewProjectRequired();
    }

    public static NewProjectRequired newInstance(ProjectListener projectListener) {
        NewProjectRequired newProjectRequired = new NewProjectRequired();
        newProjectRequired.setProjectListener(projectListener);
        return newProjectRequired;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);
        View view = inflater.inflate(R.layout.fragment_project_required, null);

        setVariables(view);
        setListeners();
        return view;
    }

    private void setProjectListener(ProjectListener projectListener) {
        this.projectListener = projectListener;
    }

    private void setProject(Project project) {
        this.project = project;
    }

    private void setVariables(View view) {
        name = (EditText)view.findViewById(R.id.project_enter_project_name);
        streetAddress = (EditText)view.findViewById(R.id.project_enter_street_address);
        city = (EditText)view.findViewById(R.id.project__enter_city);
        state = (AutoCompleteTextView)view.findViewById(R.id.project_enter_state);
        zip = (EditText)view.findViewById(R.id.project_enter_zip);
        floors = (EditText)view.findViewById(R.id.project_enter_above_floors);
        basementFloors = (EditText)view.findViewById(R.id.project_enter_below_floors);

        takePicture = (Button)view.findViewById(R.id.project_enter_take_picture);
        imageView = (ImageView)view.findViewById(R.id.project_enter_site_picture);
        viewSwitcher = (ViewSwitcher)view.findViewById(R.id.project_enter_view_switcher);

        submit = (Button)view.findViewById(R.id.project_enter_submit);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, State.getAbbreviationList());
        state.setAdapter(adapter);
        state.setThreshold(1);
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complete()) {
                    if (project == null) {
                        project = new Project();
                        setProject();
                        Storage.storeProject(getActivity(), project);
                    } else {
                        setProject();
                        projectListener.submitProject(project);
                    }
                    clearFields();
                } else {
                    new MaterialDialog.Builder(getActivity())
                            .title("More Information Needed")
                            .content("Projects must have " +
                                    "\nName" +
                                    "\nStreet Address" +
                                    "\neither City & State OR Zip Code" +
                                    "\neither Above Ground Floors or Below Ground Floors" +
                                    "\n\nProject Picture is optional")
                            .positiveText("OK")
                            .show();
                }
            }
        });
    }

    private boolean complete() {
        return check(name) &
                check(streetAddress) &
                (
                        (check(city) & check(state)) |
                                check(zip)
                ) &
                (check(floors) | check(basementFloors));
    }

    private boolean check(EditText editText) {
        if (editText.getText().toString().equals("")) {
            setError(editText);
            return false;
        } else {
            return true;
        }
    }

    private void setError(View view) {
        view.setBackgroundColor(Color.RED);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.TRANSPARENT);
                Log.i("ONCLICKC", "Clicked");
            }
        });
    }

    private void setProject() {
        project.setName(name.getText().toString());
        Address address;
        if (project.getAddress() != null) {
            address = project.getAddress();
        } else {
            address = new Address();
        }
        address.setStreetAddress(streetAddress.getText().toString());
        address.setCity(city.getText().toString());
        address.setState(state.getText().toString());
        address.setZip(zip.getText().toString());
        project.setAddress(address);
        project.setNumAGFloors(floors.getText().toString());
        project.setNumBasementFloors(basementFloors.getText().toString());
    }

    private void clearFields() {
        name.setText("");
        streetAddress.setText("");
        city.setText("");
        state.setText("");
        zip.setText("");
        floors.setText("");
        basementFloors.setText("");
    }

    public interface ProjectListener {
        public void submitProject(Project project);
    }

}
