package test.UserFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.util.ArrayList;
import java.util.List;

import test.Fragments.CameraPictureFragment;
import test.Network.CurrentAddressService;
import test.assesortron5.R;
import test.objects.Address;
import test.objects.Project;
import test.persistence.Constants;
import test.persistence.State;
import test.persistence.Storage;

/**
 * Created by otf on 7/18/15.
 */
public class NewProjectRequired extends Fragment implements CameraPictureFragment.CameraPictureFragmentCallback {
    ProjectListener projectListener;
    AddressReceiver addressReceiver;
    Project project;
    String userId;

    private EditText name, floors, basementFloors;
    private EditText streetAddress, city, zip;
    private AutoCompleteTextView state;
    private Button submit, takePicture, findCurrentAddress;
    private ViewSwitcher viewSwitcher;
    private ImageView imageView;

    private String sitePictureId;

    public NewProjectRequired() {}

    public static NewProjectRequired newInstance(String userId) {
        NewProjectRequired newProjectRequired = new NewProjectRequired();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        newProjectRequired.setArguments(bundle);
        return newProjectRequired;
    }

    public static NewProjectRequired newInstance(ProjectListener projectListener, String userId) {
        NewProjectRequired newProjectRequired = new NewProjectRequired();
        newProjectRequired.setProjectListener(projectListener);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        newProjectRequired.setArguments(bundle);
        return newProjectRequired;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
        Log.i("NewProject OnCreate", "OnCreate Called...");
        addressReceiver = new AddressReceiver();
        IntentFilter intentFilter = new IntentFilter(CurrentAddressService.ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(addressReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle saved) {
        super.onCreateView(inflater, vg, saved);
        View view = inflater.inflate(R.layout.fragment_project_required, null);
        userId = getArguments().getString(Constants.USER_ID);
        setVariables(view);
        setListeners();
        return view;
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(addressReceiver);
        super.onStop();
    }


    private void setProjectListener(ProjectListener projectListener) {
        this.projectListener = projectListener;
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

        findCurrentAddress = (Button)view.findViewById(R.id.project_enter_find_current_address);

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
                        project = new Project(userId);
                        setProject();
                        projectListener.submitProject(project);
                    } else {
                        setProject();
                        projectListener.submitProject(project);
                    }
                    clearFields();
                } else {
                    new MaterialDialog.Builder(getActivity())
                            .title("More Information Needed")
                            .content("Projects must have " +
                                    "\n -Name" +
                                    "\n -Street Address" +
                                    "\n -either City & State OR Zip Code" +
                                    "\n -either Above Ground Floors or Below Ground Floors" +
                                    "\n\nProject Picture is optional")
                            .positiveText("OK")
                            .show();
                }
            }
        });
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        if (sitePictureId != null) {
            setImageViewImage(sitePictureId);
        }

        findCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrentAddressService.class);
                getActivity().startService(intent);
            }
        });
    }


    private void startCamera() {
        getFragmentManager()
                .beginTransaction()
                .add(CameraPictureFragment.newInstance(this), null)
                .commit();
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

    private void setAddress(Address address) {
        streetAddress.setText(address.getStreeAddress());
        city.setText(address.getCity());
        state.setText(address.getState());
        zip.setText(address.getZip());
    }

    private void setError(final EditText view) {
        view.setBackgroundColor(Color.RED);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        project.addSitePicture(sitePictureId);
    }

    private void clearFields() {
        name.setText("");
        name.setBackgroundColor(Color.TRANSPARENT);
        streetAddress.setText("");
        streetAddress.setBackgroundColor(Color.TRANSPARENT);
        city.setText("");
        city.setBackgroundColor(Color.TRANSPARENT);
        state.setText("");
        state.setBackgroundColor(Color.TRANSPARENT);
        zip.setText("");
        zip.setBackgroundColor(Color.TRANSPARENT);
        floors.setText("");
        floors.setBackgroundColor(Color.TRANSPARENT);
        basementFloors.setText("");
        basementFloors.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void returnImageId(String imageId) {
        sitePictureId = imageId;
        setImageViewImage(imageId);
    }

    private void setImageViewImage(String imageId) {
        AsyncTask<String, Void, Bitmap> setImage = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                return Storage.getPictureByOwnerId(getActivity(), strings[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                viewSwitcher.showNext();
            }
        };
        setImage.execute(imageId);
    }

    public class AddressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(CurrentAddressService.MESSAGE);
            if (message != null && !"".equals(message)) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
            List<android.location.Address> addressList = intent.getParcelableArrayListExtra(CurrentAddressService.ADDRESS);
            if (addressList != null) {
                final List<Address> addresses = new ArrayList<>();
                for (android.location.Address address: addressList) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        Log.i("Address line " + i, address.getAddressLine(i));
                    }
                    Address address1 = Address.fromAndroidAddress(address);
                    if (address1.getStreeAddress() != null && address1.getCity() != null) {
                        addresses.add(address1);
                    }
                }
                if (addresses.size() > 0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(addresses.size() + " possible address" + (addresses.size() > 1 ? "es" : ""))
                            .adapter(new AddressListAdapter(addresses),
                                    new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                            setAddress(addresses.get(i));
                                            materialDialog.dismiss();
                                        }
                                    })
                            .negativeText("cancel")
                            .show();
                } else {
                    Toast.makeText(getActivity(), "No address found, Sorry!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class AddressListAdapter extends BaseAdapter {
        List<Address> addresses;

        public AddressListAdapter(List<Address> addresses) {
            this.addresses = addresses;
        }

        @Override
        public int getCount() {
            return addresses.size();
        }

        @Override
        public Object getItem(int i) {
            return addresses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.list_empty, null);
            }
            TextView text = (TextView)view.findViewById(R.id.list_empty_message);
            text.setText(addresses.get(i).getFullAddress());
            return view;
        }
    }

    public interface ProjectListener {
        public void submitProject(Project project);
    }

}
