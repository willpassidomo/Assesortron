package test.Network;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by otf on 8/15/15.
 */
public class CurrentAddressService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;

    public static final int NUM_ADDRESSES_RETURNED = 20;

    public static final String ACTION = "currentAddressServiceAction";
    public static final String MESSAGE = "currentAddressServiceMessage";
    public static final String ADDRESS = "currentAddressServiceAddress";


    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int a, int b) {
        broadCast("connecting..");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

    private void broadCast(String message) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(MESSAGE, message);
        sendBroadcast(intent);
    }

    private void broadCastAddress(ArrayList<Address> addresses) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putParcelableArrayListExtra(ADDRESS, addresses);
        sendBroadcast(intent);
    }

    private AsyncTask<Location, Void, List<Address>> getAddresses() {
        return new AsyncTask<Location, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Location... locations) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), NUM_ADDRESSES_RETURNED);
                    if (addresses == null) {
                        broadCast("geocoder not available");
                    } else if (addresses.size() < 1) {
                        broadCast("No Addresses for current location");
                    } else {
                        return addresses;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses != null) {
                    broadCastAddress(new ArrayList<Address>(addresses));
                }
                stopSelf();
            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = getCurrentCoordinates();
        if (location != null) {
            getAddresses().execute(location);
        } else {
            broadCast("Could not find Current Locations (coordinates)");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        broadCast("connection suspended");
        stopSelf();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        broadCast("Connection to location client failed :(");
        stopSelf();
    }

    public Location getCurrentCoordinates() {
            if (mGoogleApiClient.isConnected()) {
                return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                broadCast("cannot connect to google api client");
            }
            return null;
        }
}
