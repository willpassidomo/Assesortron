package test.objects;

import android.provider.BaseColumns;

import java.util.UUID;

import test.persistence.Constants;

/**
 * Created by otf on 7/18/15.
 */
public class Address {
    private String id;
    private String streetNumber;
    private String streetName;
    private String city;
    private String state;
    private String zip;

    public Address() {
        this.id = UUID.randomUUID().toString();
    }

    public Address(String streetNumber, String streetName, String city, String state, String zip) {
        this();
        this.setStreetNumber(streetNumber);
        this.setStreetAddress(streetName);
        this.setCity(city);
        this.setState(state);
        this.setZip(zip);
    }

    private Address(String id) {
        this.id = id;
    }

    public static Address getDBAddress(String id) {
        return new Address(id);
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public Address setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public String getStreeAddress() {
        return streetName;
    }

    public Address setStreetAddress(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public Address setState(String state) {
        this.state = state;
        return this;
    }

    public String getZip() {
        return zip;
    }

    public Address setZip(String zip) {
        this.zip = zip;
        return this;
    }

//    //public String getStreetAddress() {
//        return this.streetNumber + " " + this.streetName;
//    }

    public String getCityStateZip() {
        String addy = "";
        if (this.city != null && !this.city.equals("")) {
            addy += city + ", ";
        } else {
            addy += "--, ";
        }
        if (this.state != null && !this.state.equals("")) {
            addy += state + ", ";
        } else {
            addy += "--, ";
        }
        if (this.zip != null && !this.zip.equals("")) {
            addy += zip;
        } else {
            addy += "-----";
        }
        return addy;
    }

    public String getId() {
        return this.id;
    }

    public static abstract class AddressEntry implements BaseColumns {
        public static final String TABLE_NAME = "addresses";
        public static final String ADDRESS_ID = "addyId";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String STREET_NUMBER = "streetNumber";
        public static final String STREET_NAME = "streetName";
        public static final String ZIP_CODE = "zipCode";

        public static final String CREATE_ADDRESS_TABLE =
                Constants.createTableString(
                        TABLE_NAME,
                        ADDRESS_ID + Constants.TEXT_TYPE,
                        CITY + Constants.TEXT_TYPE,
                        STATE + Constants.TEXT_TYPE,
                        STREET_NUMBER + Constants.TEXT_TYPE,
                        STREET_NAME + Constants.TEXT_TYPE,
                        ZIP_CODE + Constants.TEXT_TYPE);
    }
}
