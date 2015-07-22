package test.persistence;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import test.objects.FieldValue;

/**
 * Created by willpassidomo on 3/21/15.
 */
public class Constants {
    public static final String PROJECT_ID = "project_id";
    public static final String SITE_VISIT_ID = "site_visit_id";
    public static final String WALK_THROUGH_ID = "walk_through_id";
    public static final String STRING_LIST_TYPE = "string_list_type";
    public static final String NEW_OR_EDIT = "new_or_edit";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String USER_NAME = "user_name";

    public static final String BROADCAST_SYNC_SERVICE_RESPONSE = "sync_service_response";

    public static final String ID_TYPE = "type";
    public static final String TYPE_SITEVISIT = "type_site_visit";
    public static final String TYPE_PROJECT = "type_project";

    public static final int FIELD_VALUE_PROJECT = 1;
    public static final int FIELD_VALUE_SITE_VISIT = 2;
    public static final int FIELD_VALUE_USER = 3;

    public static final String INTEGER_TYPE = " INTEGER";
    public static final String TEXT_TYPE = " TEXT";
    public static final String REAL_TYPE = " REAL";
    public static final String COMMA_SEP = " , ";


    public static String createTableString(String tableName, String Id, String...columnNames) {
        String createTable = "CREATE TABLE " + tableName + " (" + Id + " PRIMARY KEY, ";
        for (int i = 0; i < (columnNames.length - 1); i++) {
            createTable += columnNames[i] + COMMA_SEP;
        }
        createTable += columnNames[columnNames.length -1];
        createTable += ")";
        return createTable;
    }

    public static String createBridgeTableString(String tableName, String idOne, String idTwo) {
        String createTable = "CREATE TABLE " + tableName + " (" + idOne + COMMA_SEP + idTwo + " PRIMARY KEY) ";
        return createTable;
    }

    public static void storeInitialSiteWalkQuestions(Context context) {
        List<FieldValue> fvs = new ArrayList<>();
        fvs.add(new FieldValue("Temperature", false, false));
        fvs.add(new FieldValue("Weather", false, false));
        fvs.add(new FieldValue("Date", true, false));
        fvs.add(new FieldValue("Time", false, false));
        fvs.add(new FieldValue("Humidity", false, false));

        Storage.storeSiteVisitQuestions(context, fvs);
    }

    public static List<String> getInitialProgresses() {
        List progressList = new LinkedList();
        progressList.add("yet to commence");
        progressList.add("commenced");
        progressList.add("25%");
        progressList.add("In Progress");
        progressList.add("75%");
        progressList.add("Substantially Complete");
        return progressList;
    }

    public static List<String> getInitalTrades() {
        List tradeList = new LinkedList<String>();
        tradeList.add("Demo");
        tradeList.add("Framing");
        tradeList.add("Plumbing- rough");
        tradeList.add("Plumbing- finish");
        tradeList.add("Electical");
        return tradeList;
    }
}
