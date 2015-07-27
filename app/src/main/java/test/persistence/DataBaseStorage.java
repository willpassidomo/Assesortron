package test.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.objects.Address;
import test.objects.FieldValue;
import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.WalkThrough;

/**
 * Created by willpassidomo on 1/24/15.
 */
public class DataBaseStorage extends SQLiteOpenHelper {
    private static DataBaseStorage dataBaseStorage;
    private static String DATABASE_NAME = "assesortronDB13.db";
    private static int DATABASE_VERSION = 12;

    public DataBaseStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Project.ProjectEntry.CREATE_PROJECT_TABLE);
        sqLiteDatabase.execSQL(Project.ProjectDrawRequestBridge.CREATE_PROJECT_DRAW_REQUEST_TABLE);
        sqLiteDatabase.execSQL(Project.ProjectWalkThroughBridge.CREATE_PROJECT_WALKTHROUGH_TABLE);
        sqLiteDatabase.execSQL(Project.ProjectImageBridge.CREATE_PROJECT_IMAGE_TABLE);
        sqLiteDatabase.execSQL(Project.ProjectSiteWalkBridge.CREATE_PROJECT_SITE_WALK_TABLE);
        sqLiteDatabase.execSQL(SiteVisit.SiteWalkEntry.CREATE_SITE_WALK_ENTRY_TABLE);
        sqLiteDatabase.execSQL(SiteVisit.SiteWalkDrawRequestBridge.CREATE_SITE_WALK_DRAW_REQUEST_BRIDGE_TABLE);
        sqLiteDatabase.execSQL(SiteVisit.SiteWalkWalkThroughBridge.CREATE_PROJECT_WALKTHROUGH_TABLE);
        sqLiteDatabase.execSQL(DrawRequestItem.DrawRequestItemEntry.CREATE_DRAW_REQUES_TITEM_TABLE);
        sqLiteDatabase.execSQL(DrawRequest.DrawRequestEntry.CREATE_DRAW_REQUEST_TABLE);
        sqLiteDatabase.execSQL(DrawRequest.DrawRequestItemBridge.CREATE_DRAW_REQUEST_ITEM_BRIDGE_TABLE);
        sqLiteDatabase.execSQL(WalkThrough.WalkThroughEntry.CREATE_WALKTHROUGH_TABLE);
        sqLiteDatabase.execSQL(WalkThrough.WalkThroughPictureBridge.CREATE_WALKTHROUGH_PICTURE_TABLE);
        sqLiteDatabase.execSQL(Storage.TradesTable.CREATE_TRADES_STRING_TABLE);
        sqLiteDatabase.execSQL(Storage.ProgressesTable.CREATE_PROGRESSES_STRING_TABLE);
        sqLiteDatabase.execSQL(Project.ProjectTradesBridge.CREATE_PROJECT_TRADES_TABLE);
        sqLiteDatabase.execSQL(FieldValue.FieldValueTable.CREATE_FIELD_VALUE_TABLE);
        sqLiteDatabase.execSQL(FieldValue.StockFieldValueTable.CREATE_STOCK_FIELD_VALUE_TABLE);
        sqLiteDatabase.execSQL(Address.AddressEntry.CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        onCreate(sqLiteDatabase);
    }

    public static synchronized DataBaseStorage getDataBaseStorage(Context context) {
        if (dataBaseStorage == null) {
            dataBaseStorage = new DataBaseStorage(context);
        }
        return dataBaseStorage;
    }

    public boolean insertProgresses(List<String> progresses) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues contentValues;
            for (String string : progresses) {
                contentValues = new ContentValues();
                contentValues.put(Storage.ProgressesTable.COLUMN_PROGRESS_STRING, string);
                contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ID, "1");
                db.insertWithOnConflict(Storage.ProgressesTable.TABLE_NAME_PROGRESSES, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
            return true;
        }
        finally {

        }
    }

    public boolean insertProjectTrades(List<String> trades, String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues contentValues;
            for (String string : trades) {
                contentValues = new ContentValues();
                contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ID, projectId);
                contentValues.put(Project.ProjectTradesBridge.COLUMN_PROJECT_TRADES_STRING, string);
                db.insertWithOnConflict(Project.ProjectTradesBridge.TABLE_NAME_PROJECT_TRADES, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
            return true;
        }
        finally {

        }
    }


    public boolean insertTrades(List<String> trades) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues;
        for (String string: trades) {
            contentValues = new ContentValues();
            contentValues.put(Storage.TradesTable.COLUMN_TRADE_STRING, string);
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ID, "1");
            db.insertWithOnConflict(Storage.TradesTable.TABLE_NAME_TRADES, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
        return true;
    }


    public boolean insertWalkThrough(String siteWalkId, WalkThrough wt) {
        SQLiteDatabase db = this.getWritableDatabase();

            Log.i("inserting pictures", "count = " + wt.getPictures().size());

            ContentValues contentValues = getContentValues(wt);
            ContentValues contentValues1 = getContentValues(siteWalkId, wt);
            ContentValues picContentValues = getContentValuesExtra1(wt);

            Log.i("pic contents value", "ize = " + picContentValues.size());

            db.insertWithOnConflict(WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.insertWithOnConflict(SiteVisit.SiteWalkWalkThroughBridge.TABLE_NAME, null, contentValues1, SQLiteDatabase.CONFLICT_REPLACE);
            if (picContentValues.size() > 0) {
                Log.i("inserting picturesss", "count = " + wt.getPictures().size());
                db.insertWithOnConflict(WalkThrough.WalkThroughPictureBridge.TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE, null, picContentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
            return true;
    }

    public boolean insertDrawRequest(String siteWalkId, DrawRequest dr) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = getContentValues(dr);
            ContentValues contentValues1 = getContentValues(siteWalkId, dr);

            db.insertWithOnConflict(DrawRequest.DrawRequestEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.insertWithOnConflict(SiteVisit.SiteWalkDrawRequestBridge.TABLE_NAME, null, contentValues1, SQLiteDatabase.CONFLICT_REPLACE);
            return true;
    }

    public void insertDrawRequestItem(String drawRequestId, DrawRequestItem drawRequestItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = getContentValues(drawRequestItem);
        ContentValues contentValues1 = getContentValues(drawRequestId, drawRequestItem);
        db.insertWithOnConflict(DrawRequestItem.DrawRequestItemEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.insertWithOnConflict(DrawRequest.DrawRequestItemBridge.TABLE_NAME, null, contentValues1, SQLiteDatabase.CONFLICT_REPLACE);
//
//        Log.i("DrawReqItem in DB", "\nrow Item Table- " + rowIdItemTable + "\nrow Bridge table" + rowIdBridgeTable);
    }

    public boolean insertProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = getContentValues(project);
            ContentValues contentValues1 = getContentValuesExtra1(project);

            long rowId = db.insertWithOnConflict(Project.ProjectEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            if (project.getPictures() != null && !project.getPictures().isEmpty()) {
                db.insertWithOnConflict(Project.ProjectImageBridge.TABLE_NAME_PROJECT_PICTURES, null, contentValues1, SQLiteDatabase.CONFLICT_REPLACE);
            }
            insertAddress(project.getAddress());
            return true;
    }

    public void insertAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValues(address);
        db.insertWithOnConflict(
                Address.AddressEntry.TABLE_NAME,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public void insertSiteWalk(String projectId, SiteVisit siteVisit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValues(siteVisit);
        ContentValues contentValues1 = getContentValues(projectId, siteVisit);

        db.insertWithOnConflict(SiteVisit.SiteWalkEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.insertWithOnConflict(Project.ProjectSiteWalkBridge.TABLE_NAME, null, contentValues1, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void insertStockFieldValues(int type, List<FieldValue> fvs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (FieldValue fv: fvs) {
            ContentValues contentValues = getContentValues(type, fv);
            db.insertWithOnConflict(FieldValue.StockFieldValueTable.TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public void insertFieldValues(List<FieldValue> fvs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (FieldValue fv: fvs) {
            ContentValues contentValues = getContentValues(fv);
            db.insertWithOnConflict(FieldValue.FieldValueTable.TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public List<String> getProjectTrades(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> trades = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    Project.ProjectTradesBridge.TABLE_NAME_PROJECT_TRADES,
                    new String[]{Project.ProjectTradesBridge.COLUMN_PROJECT_TRADES_STRING},
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ",
                    new String[] {projectId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trades.add(cursor.getString(0));
                Log.i("retrieved", cursor.getString(0));
                cursor.moveToNext();
            }
            return trades;
        }
        finally {
            cursor.close();
        }
    }

    public List<String> getTrades() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> trades = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    Storage.TradesTable.TABLE_NAME_TRADES,
                    new String[]{Storage.TradesTable.COLUMN_TRADE_STRING},
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ",
                    new String[]{"1"},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trades.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return trades;
        }
        finally {
//            cursor.close();
        }
    }

    //this was replaced in favor of a method which would iterate over a Project's walkthrough id's from the bridge table,
    //it was the bridge table which kept this method from working, projectId is not a filed in walkthroughs

//    public String getDrawRequestAmount(String projectId, String min_max) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = null;
//        try {
//            cursor = db.query(
//                    DrawRequest.DrawRequestEntry.TABLE_NAME,
//                    new String[]{min_max+"("+ DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_REQUEST + ")"},
//                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ?",
//                    new String[]{projectId},
//                    null,
//                    null,
//                    null
//            );
//            cursor.moveToFirst();
//            return cursor.getString(0);
//            }
//        finally {
//            cursor.close();
//        }
//
//    }

    public String getRetainageRel(String walkThroughId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String drawRM;
        Cursor cursor = null;
        try {
            cursor = db.query(
                    DrawRequest.DrawRequestEntry.TABLE_NAME,
                    new String[]{DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_REQUEST},
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + " = ?",
                    new String[]{walkThroughId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        finally {
            cursor.close();
        }

    }

    public List<String> getProgresses() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> progresses = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                Storage.ProgressesTable.TABLE_NAME_PROGRESSES,
                new String[]{Storage.ProgressesTable.COLUMN_PROGRESS_STRING},
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ",
                    new String[]{"1"},
                null,
                null,
                null
            );
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                progresses.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return progresses;
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    public List<DrawRequest> getProjectDrawRequests(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = Project.ProjectDrawRequestBridge.TABLE_NAME_PROJECT_DRAW_REQUESTS;
            String[] columns = {Project.ProjectDrawRequestBridge.COLUMN_PROJECT_DRAW_REQUEST_ID};
            String selection = Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ";
            String[] selectionsArgs = {projectId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionsArgs,
                    null,
                    null,
                    null
            );
            List<DrawRequest> drawRequests = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                drawRequests.add(getDrawRequest(cursor.getString(0)));
                cursor.moveToNext();
            }
            return drawRequests;
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public DrawRequestItem getChangeOrder(String changeOrderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = DrawRequestItem.DrawRequestItemEntry.TABLE_NAME;
            String[] columns = {
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBCONTRACTOR,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_AMOUNT,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_DESCRIPTION,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_EXECUTED,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBMITTED
            };
            String selection = DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID + "= ?";
            String[] selectionArgs = {changeOrderId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            return getDrawRequestItem(cursor);
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<DrawRequestItem> getDrawRequestItems(String drawRequestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = DrawRequest.DrawRequestItemBridge.TABLE_NAME;
            String[] columns = {
                    DrawRequest.DrawRequestItemBridge.COLUMN_DRAW_REQUEST_ITEM_ID
            };
            String selection = DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + " = ?";
            String[] selectionArgs = {drawRequestId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            List<String> drawRequestItemIds = new ArrayList<String>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                drawRequestItemIds.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return getDrawRequestItems(drawRequestItemIds);
        }
        finally {
            if (cursor != null && ! cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<DrawRequestItem> getDrawRequestItems(List<String> changeOrderIds) {
        if (changeOrderIds == null) {
            return null;
        }
        SQLiteDatabase db = this.getWritableDatabase();
            List<DrawRequestItem> drawRequestItems = new ArrayList<DrawRequestItem>();

            String table = DrawRequestItem.DrawRequestItemEntry.TABLE_NAME;
            String[] columns = {
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_TYPE,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBCONTRACTOR,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_AMOUNT,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_DESCRIPTION,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_EXECUTED,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBMITTED
            };
            String selection = DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID + " = ? ";
            String[] selectionArgs;
            String order = DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBMITTED + " ASC";


            for (String changeOrderId : changeOrderIds) {
                selectionArgs = new String[]{changeOrderId};
                Cursor cursor = db.query(
                        table,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        order);
                cursor.moveToFirst();
                DrawRequestItem co = getDrawRequestItem(cursor);

                drawRequestItems.add(co);
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return drawRequestItems;
    }

    public WalkThrough getWalkThrough(String walkThroughId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS;
            String[] columns = {
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_FLOOR,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_TRADE,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_PROGRESS,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_NOTE,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_DATE
            };
            String selection = WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + "= ?";
            String[] selectionArgs = {walkThroughId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            return getCursorWalkThrough(cursor);
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }



    public List<String> getSiteWalktWalkThroughs(String siteWalkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = SiteVisit.SiteWalkWalkThroughBridge.TABLE_NAME;
            String[] columns = {WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID};
            String selection = SiteVisit.SiteWalkEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = {siteWalkId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            List<String> walkThroughIds = new ArrayList<String>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                walkThroughIds.add(cursor.getString(0));
                cursor.moveToNext();
            }

            return walkThroughIds;
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public Map<Date, String> getWalkThroughDates(List<String> walkThroughIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        Map<Date, String> dates = new HashMap<Date, String>();

        String[] selectionArgs;
        for (String wt : walkThroughIds) {
            selectionArgs = new String[]{wt};
                cursor = db.query(
                        WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS,
                        new String[]{WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_DATE, WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID},
                        WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + "= ?",
                        selectionArgs,
                        null,
                        null,
                        null);
            cursor.moveToFirst();
            dates.put(new Date(cursor.getString(0)), cursor.getString(1));
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return dates;
    }

    public List<WalkThrough> getWalkThroughs(List<String> walkThroughIds) {
        SQLiteDatabase db = this.getWritableDatabase();
            List<WalkThrough> walkThroughs = new ArrayList<WalkThrough>();

            String table = WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS;
            String[] columns = {
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_FLOOR,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_TRADE,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_PROGRESS,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_NOTE,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_DATE
            };
            String selection = WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + "= ?";
            String[] selectionArgs;
            String order = WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_DATE + " ASC";

            for (String walkThroughId : walkThroughIds) {
                selectionArgs = new String[]{walkThroughId};
                Cursor cursor = db.query(
                        table,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        order
                );
                cursor.moveToFirst();
                WalkThrough wt = getCursorWalkThrough(cursor);
                walkThroughs.add(wt);
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return walkThroughs;
    }

    public List<String> getWalkThroughPicIds(String walkThroughId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = WalkThrough.WalkThroughPictureBridge.TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE;
            String[] columns = {WalkThrough.WalkThroughPictureBridge.COLUMN_WALK_THROUGH_PICTURE_URI};
            String selection = WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + " = ?";
            String[] selectionArgs = {walkThroughId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            List<String> pictureIds = new ArrayList<String>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.i("retrieved pic id", cursor.getString(0));
                pictureIds.add(cursor.getString(0));
                cursor.moveToNext();
            }

            return pictureIds;
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

//    public List<Uri> getWalkThroughPictures(List<String> pictureIds) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        List<Uri> picUris = new ArrayList<Uri>();
//
//        String table = WalkThrough.WalkThroughPictureBridge.TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE;
//        String[] columns = {WalkThrough.WalkThroughPictureBridge.COLUMN_WALK_THROUGH_PICTURE_URI};
//        String selection = WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + " = ?";
//        String[] selectionArgs;
//
//        for(String pictureId: pictureIds) {
//            selectionArgs = new String[]{pictureId};
//            Cursor cursor = db.query(
//                    table,
//                    columns,
//                    selection,
//                    selectionArgs,
//                    null,
//                    null,
//                    null
//            );
//            cursor.moveToFirst();
//            if(cursor.getCount() > 0) {
//                picUris.add(Uri.parse(cursor.getString(0)));
//            }
//        }
//        Log.i("pictures retrieved", "count = " + picUris.size());
//        return picUris;
//    }

    public Project getProject(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = Project.ProjectEntry.TABLE_NAME;
            String[] columns = {Project.ProjectEntry.COLUMN_PROJECT_ID, Project.ProjectEntry.COLUMN_PROJECT_NAME, Project.ProjectEntry.COLUMN_PROJECT_ADDRESS,
                    Project.ProjectEntry.COLUMN_PROJECT_USER_ID, Project.ProjectEntry.COLUMN_PROJECT_INITIAL_START, Project.ProjectEntry.COLUMN_PROJECT_INITIAL_COMPLETION,
                    Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_START, Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_COMPLETION, Project.ProjectEntry.COLUMN_PROJECT_LOAN_AMOUNT,
                    Project.ProjectEntry.COLUMN_PROJECT_CONTRACT_AMOUNT, Project.ProjectEntry.COLUMN_PROJECT_NUM_AG_FLOORS, Project.ProjectEntry.COLUMN_PROJECT_NUM_BASEMENT_FLOORS,
                    Project.ProjectEntry.COLUMN_SQUARE_FEET, Project.ProjectEntry.COLUMN_HAS_OUTDOOR_WORK, Project.ProjectEntry.COLUMN_PROJECT_DATE_CREATED};
            String selection = Project.ProjectEntry.COLUMN_PROJECT_ID + " = ?";
            String[] selectionArgs = {projectId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            if(!cursor.isAfterLast()) {
                Project project = getCursorProject(cursor);
                project.setAddress(getAddress(cursor.getString(2)));
                return project;
            }
            return null;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public Address getAddress(String addressId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {Address.AddressEntry.ADDRESS_ID,
                    Address.AddressEntry.CITY,
                    Address.AddressEntry.STATE,
                    Address.AddressEntry.STREET_NAME,
                    Address.AddressEntry.STREET_NUMBER,
                    Address.AddressEntry.ZIP_CODE};

            cursor = db.query(
                    Address.AddressEntry.TABLE_NAME,
                    columns,
                    Address.AddressEntry.ADDRESS_ID + " = ? ",
                    new String[]{addressId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            if(!cursor.isAfterLast()) {
                return getCursorAddress(cursor);
            }
            return null;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public SiteVisit getSiteWalk(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {
                    SiteVisit.SiteWalkEntry.COLUMN_ID,
                    SiteVisit.SiteWalkEntry.COLUMN_TIME_STARTED,
                    SiteVisit.SiteWalkEntry.COLUMN_LAST_ENTRY,
                    SiteVisit.SiteWalkEntry.COLUMN_IS_ACTIVE,
                    SiteVisit.SiteWalkEntry.COLUMN_PROJECT_ID
            };
            cursor = db.query(
                    SiteVisit.SiteWalkEntry.TABLE_NAME,
                    columns,
                    SiteVisit.SiteWalkEntry.COLUMN_ID + " = ?",
                    new String[]{id},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            SiteVisit siteVisit = getCursorSiteWalk(cursor);
            siteVisit.setFieldValues(getFieldValuesByOwner(siteVisit.getId()));
            return siteVisit;
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    public List<String> getSiteWalkIds(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    Project.ProjectSiteWalkBridge.TABLE_NAME,
                    new String[]{SiteVisit.SiteWalkEntry.COLUMN_ID},
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ?",
                    new String[]{projectId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            List<String> siteWalkIds = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                siteWalkIds.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return siteWalkIds;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<SiteVisit> getSiteWalks(List<String> siteWalkIds) {
        List<SiteVisit> siteVisits = new ArrayList<>();
        for (String id: siteWalkIds) {
            siteVisits.add(getSiteWalk(id));
        }
        return siteVisits;
    }

//    public List<SiteVisit> getSiteWalks(List<String> siteWalkIds) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = null;
//        try {
//            String[] columns = {
//                    SiteVisit.SiteWalkEntry.COLUMN_ID,
//                    SiteVisit.SiteWalkEntry.COLUMN_TIME_STARTED,
//                    SiteVisit.SiteWalkEntry.COLUMN_LAST_ENTRY,
//                    SiteVisit.SiteWalkEntry.COLUMN_IS_ACTIVE
//            };
//            List<SiteVisit> siteVisits = new ArrayList<>();
//            for (String siteWalkId : siteWalkIds) {
//                cursor = db.query(
//                        SiteVisit.SiteWalkEntry.TABLE_NAME,
//                        columns,
//                        SiteVisit.SiteWalkEntry.COLUMN_ID + " = ? ",
//                        new String[]{siteWalkId},
//                        null,
//                        null,
//                        null
//                );
//                cursor.moveToFirst();
//                SiteVisit siteVisit = getCursorSiteWalk(cursor);
//                if (siteVisit != null) {
//                    siteVisits.add(siteVisit);
//                }
//            }
//            return siteVisits;
//        }
//        finally {
//
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//    }

    public List<SiteVisit> getActiveSiteWalks(List<String> siteWalkIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {
                    SiteVisit.SiteWalkEntry.COLUMN_ID,
                    SiteVisit.SiteWalkEntry.COLUMN_TIME_STARTED,
                    SiteVisit.SiteWalkEntry.COLUMN_LAST_ENTRY,
                    SiteVisit.SiteWalkEntry.COLUMN_IS_ACTIVE,
                    SiteVisit.SiteWalkEntry.COLUMN_PROJECT_ID
            };
            List<SiteVisit> siteVisits = new ArrayList<>();
            for (String siteWalkId : siteWalkIds) {
                cursor = db.query(
                        SiteVisit.SiteWalkEntry.TABLE_NAME,
                        columns,
                        SiteVisit.SiteWalkEntry.COLUMN_IS_ACTIVE + " = ?" + " AND " +
                                SiteVisit.SiteWalkEntry.COLUMN_ID+ " = ?",
                        new String[]{"1",siteWalkId},
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    SiteVisit siteVisit = getCursorSiteWalk(cursor);
                    if (siteVisit != null) {
                        siteVisits.add(siteVisit);
                    }
                }
            }
            return siteVisits;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public DrawRequest getSiteWalkDrawRequest(String siteWalkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    SiteVisit.SiteWalkDrawRequestBridge.TABLE_NAME,
                    new String[] {DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID},
                    SiteVisit.SiteWalkEntry.COLUMN_ID + " = ?",
                    new String[]{siteWalkId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                String drawId = cursor.getString(0);
                DrawRequest drawRequest = getDrawRequest(drawId);
                drawRequest.addDrawRequestItems(getDrawRequestItems(drawId));
                return drawRequest;
            } else {
                return null;
            }
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<FieldValue> getStockFieldValues(int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID,
                    FieldValue.FieldValueTable.COL_FIELD,
                    FieldValue.FieldValueTable.COL_IN,
                    FieldValue.FieldValueTable.COL_REQUIRED
            };
            cursor = db.query(
                    FieldValue.StockFieldValueTable.TABLE_NAME,
                    columns,
                    FieldValue.StockFieldValueTable.COL_FIELD_VALUE_OWNER_TYPE + " = ? ",
                    new String[]{type + ""},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            ArrayList<FieldValue> fvs = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                fvs.add(getCursorStockFieldValue(cursor));
                cursor.moveToNext();
            }
            return  fvs;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<FieldValue> getFieldValuesByOwner(String ownerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID,
                    FieldValue.FieldValueTable.COL_OWNER_ID,
                    FieldValue.FieldValueTable.COL_FIELD,
                    FieldValue.FieldValueTable.COL_VALUE,
                    FieldValue.FieldValueTable.COL_IN,
                    FieldValue.FieldValueTable.COL_REQUIRED
            };
            cursor = db.query(
                    FieldValue.FieldValueTable.TABLE_NAME,
                    columns,
                    FieldValue.FieldValueTable.COL_OWNER_ID + " = ? ",
                    new String[]{ownerId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            ArrayList<FieldValue> fvs = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                fvs.add(getCursorFieldValue(cursor));
                cursor.moveToNext();
            }
            return  fvs;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public FieldValue getFieldValue(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        FieldValue fv = null;
        Cursor cursor = null;
        try {
            String[] columns = {
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID,
                    FieldValue.FieldValueTable.COL_OWNER_ID,
                    FieldValue.FieldValueTable.COL_FIELD,
                    FieldValue.FieldValueTable.COL_VALUE,
                    FieldValue.FieldValueTable.COL_IN,
                    FieldValue.FieldValueTable.COL_REQUIRED
            };
            cursor = db.query(
                    FieldValue.FieldValueTable.TABLE_NAME,
                    columns,
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID + " = ? ",
                    new String[]{id},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            if (cursor != null && !cursor.isAfterLast()) {
                fv = getCursorFieldValue(cursor);
                return fv;
            }
            if (fv == null) {
                String[] sColumns = {
                        FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID,
                        FieldValue.FieldValueTable.COL_FIELD,
                        FieldValue.FieldValueTable.COL_IN,
                        FieldValue.FieldValueTable.COL_REQUIRED
                };
                cursor = db.query(
                        FieldValue.StockFieldValueTable.TABLE_NAME,
                        sColumns,
                        FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID + " = ? ",
                        new String[]{id},
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                if (cursor != null && !cursor.isAfterLast()) {
                    fv = getCursorStockFieldValue(cursor);
                    return fv;
                }
            }
            return null;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public DrawRequest getDrawRequest(String drawRequestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String tableName = DrawRequest.DrawRequestEntry.TABLE_NAME;
            String[] columns = {
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_DATE,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_REQUEST,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_RECOMMENDATION,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_NOTE
            };

            cursor = db.query(
                    tableName,
                    columns,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + " = ? ",
                    new String[] {drawRequestId},
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            return getCursorDrawRequest(cursor);
        }
        finally {

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        }

}

    public List<Project> getAllProjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String tableName = Project.ProjectEntry.TABLE_NAME;

            String[] columns = {Project.ProjectEntry.COLUMN_PROJECT_ID, Project.ProjectEntry.COLUMN_PROJECT_NAME, Project.ProjectEntry.COLUMN_PROJECT_ADDRESS,
                    Project.ProjectEntry.COLUMN_PROJECT_USER_ID, Project.ProjectEntry.COLUMN_PROJECT_INITIAL_START, Project.ProjectEntry.COLUMN_PROJECT_INITIAL_COMPLETION,
                    Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_START, Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_COMPLETION, Project.ProjectEntry.COLUMN_PROJECT_LOAN_AMOUNT,
                    Project.ProjectEntry.COLUMN_PROJECT_CONTRACT_AMOUNT, Project.ProjectEntry.COLUMN_PROJECT_NUM_AG_FLOORS, Project.ProjectEntry.COLUMN_PROJECT_NUM_BASEMENT_FLOORS,
                    Project.ProjectEntry.COLUMN_SQUARE_FEET, Project.ProjectEntry.COLUMN_HAS_OUTDOOR_WORK, Project.ProjectEntry.COLUMN_PROJECT_DATE_CREATED};

            cursor = db.query(
                    tableName,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    Project.ProjectEntry.COLUMN_PROJECT_DATE_CREATED + " DESC"
            );

            List<Project> projects = new ArrayList<Project>();

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Project project = getCursorProject(cursor);
                project.setAddress(getAddress(cursor.getString(2)));
                projects.add(project);
                cursor.moveToNext();
            }
            return projects;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<Uri> getProjectSitePictures(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            String table = Project.ProjectImageBridge.TABLE_NAME_PROJECT_PICTURES;
            String[] columns = {Project.ProjectImageBridge.COLUMN_PROJECT_IMAGE_ID};
            String selection = Project.ProjectEntry.COLUMN_PROJECT_ID + " = ?";
            String[] selectionArgs = {projectId};

            cursor = db.query(
                    table,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            List<Uri> uris = new ArrayList<Uri>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                uris.add(Uri.parse(cursor.getString(0)));
                cursor.moveToNext();
            }
            return uris;
        }
        finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public void deleteProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            for (WalkThrough walkThrough : project.getWalkThroughs()) {
                deleteWalkThrough(walkThrough.getId());
            }

            for (DrawRequest drawRequest : project.getDrawRequests()) {
                deleteDrawRequest(drawRequest);
            }

            db.delete(
                    Project.ProjectEntry.TABLE_NAME,
                    Project.ProjectEntry.COLUMN_PROJECT_ID + "= ?",
                    new String[]{project.getId()}
            );
        }
        finally {

        }
    }

    public void deleteProject(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            List<String> siteVisits = getSiteWalkIds(projectId);
            for (String siteVisitId: siteVisits) {
                deleteSiteVisit(siteVisitId);
            }
            deleteAddress(projectId);
            db.delete(
                    Project.ProjectEntry.TABLE_NAME,
                    Project.ProjectEntry.COLUMN_PROJECT_ID + "= ?",
                    new String[]{projectId}
            );
        }
        finally {

        }
    }

    public void deleteAddress(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    Address.AddressEntry.TABLE_NAME,
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ",
                    new String[]{projectId}
            );
        }
        finally {

        }
    }

    public void deleteSiteVisit(String siteVisitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    SiteVisit.SiteWalkEntry.TABLE_NAME,
                    SiteVisit.SiteWalkEntry.COLUMN_ID + " = ? ",
                    new String[]{siteVisitId}
            );
            deleteSiteVisitWalkThroughs(siteVisitId);
            deleteSiteVisitDrawRequestAndItems(siteVisitId);
            deleteOwnersFieldValues(siteVisitId);
        }
        finally {

        }
    }

    public void deleteOwnersFieldValues(String ownerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    FieldValue.FieldValueTable.TABLE_NAME,
                    FieldValue.FieldValueTable.COL_OWNER_ID + " = ? ",
                    new String[]{ownerId}
            );
        }
        finally {

        }
    }

    public void deleteSiteVisitDrawRequestAndItems(String siteVisitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            DrawRequest drawRequest = getSiteWalkDrawRequest(siteVisitId);
            for (DrawRequestItem dr: drawRequest.getItemList()) {
                deleteDrawRequestItem(dr.getId());
            }
            db.delete(
                    DrawRequest.DrawRequestEntry.TABLE_NAME,
                    SiteVisit.SiteWalkEntry.COLUMN_ID + " = ?",
                    new String[]{siteVisitId}
            );
        }
        finally {

        }
    }

    public void deleteSiteVisitWalkThroughs(String siteVisitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS,
                    SiteVisit.SiteWalkEntry.COLUMN_ID + " = ?",
                    new String[]{siteVisitId}
            );
        }
        finally {

        }
    }

    public void deleteWalkThrough(String walkThroughId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    WalkThrough.WalkThroughEntry.TABLE_NAME_WALK_THROUGHS,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + "= ?",
                    new String[]{walkThroughId}
            );
            db.delete(
                    WalkThrough.WalkThroughPictureBridge.TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE,
                    WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + "= ?",
                    new String[]{walkThroughId}
            );
            db.delete(
                    Project.ProjectWalkThroughBridge.TABLE_NAME_PROJECT_WALK_THROUGHS,
                    Project.ProjectWalkThroughBridge.COLUMN_PROJECT_WALKTHROUGH_ID + "= ?",
                    new String[]{walkThroughId}
            );
        }
        finally {

        }
    }

    public void deleteProjectTrades(String projectId) {
        SQLiteDatabase db = this.getWritableDatabase();
            db.delete(
                    Project.ProjectTradesBridge.TABLE_NAME_PROJECT_TRADES,
                    Project.ProjectEntry.COLUMN_PROJECT_ID + " = ? ",
                    new String[]{projectId}
                    );
    }

    public void deleteTradeList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                Storage.TradesTable.TABLE_NAME_TRADES,
                null,
                null
        );
    }

    public void deleteTrade(String trade) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    Storage.TradesTable.TABLE_NAME_TRADES,
                    Storage.TradesTable.COLUMN_TRADE_STRING + " = ?",
                    new String[]{trade}
            );
        }
        finally {

        }
    }

    public void deleteProgresses() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                Storage.ProgressesTable.TABLE_NAME_PROGRESSES,
                null,
                null
        );
        }
        finally {

        }
        }

    public void deleteProgress(String progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    Storage.ProgressesTable.TABLE_NAME_PROGRESSES,
                    Storage.ProgressesTable.COLUMN_PROGRESS_STRING + " = ?",
                    new String[]{progress}
            );
        }
        finally {

        }
    }

    public void deleteDrawRequest(DrawRequest drawRequest) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (DrawRequestItem drawRequestItem: drawRequest.getItemList()) {
            deleteDrawRequestItem(drawRequestItem.getId());
        }
        try {
            db.delete(
                    DrawRequest.DrawRequestEntry.TABLE_NAME,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + " = ? ",
                    new String[]{drawRequest.getId()}
            );
            db.delete(
                    DrawRequest.DrawRequestItemBridge.TABLE_NAME,
                    DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + " = ?",
                    new String[]{drawRequest.getId()}
            );
        }
        finally {

        }
    }

    public void deleteDrawRequestItem(String drawRequestItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    DrawRequestItem.DrawRequestItemEntry.TABLE_NAME,
                    DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID + "= ?",
                    new String[]{drawRequestItemId}
            );
            db.delete(
                    DrawRequest.DrawRequestItemBridge.TABLE_NAME,
                    DrawRequest.DrawRequestItemBridge.COLUMN_DRAW_REQUEST_ITEM_ID + "= ?",
                    new String[]{drawRequestItemId}
            );
        }
        finally {

        }

    }

    public void deleteFieldValue(String fieldValueId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = 0,b = 0;
        try {
            a = db.delete(
                    FieldValue.FieldValueTable.TABLE_NAME,
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID + " = ?",
                    new String[]{fieldValueId}
            );
            b = db.delete(
                    FieldValue.StockFieldValueTable.TABLE_NAME,
                    FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID + " = ?",
                    new String[]{fieldValueId}
            );
        }
        finally {
            Log.i("deleted:", a + "rows from FV, " + b + "rows from StockFV");

        }
    }


    public ContentValues getContentValues(DrawRequest dr) {
        ContentValues contentValues = new ContentValues();

        //Insert DrawRequest into drawRequest Table
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID, dr.getId());
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_DATE, dr.getDate().toString());
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_REQUEST, dr.getCurrentRequest() + "");
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_CURRENT_RECOMMENDATION, dr.getCurrentRecmomendation() + "");
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_NOTE, dr.getConditions());
        return contentValues;
    }

    public ContentValues getContentValues(String siteWalkId, DrawRequest dr) {
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(SiteVisit.SiteWalkEntry.COLUMN_ID, siteWalkId);
        contentValues1.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID, dr.getId());
        return contentValues1;
    }

    public ContentValues getContentValues(WalkThrough wt) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID, wt.getId());
        if(wt.getDate() != null) {
            contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_DATE, wt.getDate().toString());
        }
        contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_FLOOR, wt.getFloor());
        contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_TRADE, wt.getTrade());
        contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_PROGRESS, wt.getProgress());
        contentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_NOTE, wt.getNotes());

        return contentValues;
    }

    public ContentValues getContentValuesExtra1(WalkThrough wt) {
        ContentValues picContentValues = new ContentValues();
        for (Uri uri: wt.getPictures()) {
            picContentValues.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID, wt.getId());
            picContentValues.put(WalkThrough.WalkThroughPictureBridge.COLUMN_WALK_THROUGH_PICTURE_URI, uri.toString());
        }
        return picContentValues;
    }

    public ContentValues getContentValues(String siteWalk, WalkThrough wt) {
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(SiteVisit.SiteWalkEntry.COLUMN_ID, siteWalk);
        contentValues1.put(WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID, wt.getId());
        return contentValues1;
    }

    public ContentValues getContentValues(Project project) {
        ContentValues contentValues = new ContentValues();

        //Inserts Project into the projects table
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ID, project.getId());
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_NAME, project.getName());
        if (project.getAddress() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ADDRESS, project.getAddress().getId());
        }
        if (project.getUser() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_USER_ID, project.getUser().getId());
        }
        if (project.getInitialStartDate() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_INITIAL_START, project.getInitialStartDate().toString());
        }
        if (project.getInitialCompletionDate() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_INITIAL_COMPLETION, project.getInitialCompletionDate().toString());
        }
        if(project.getActualStartDate() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_START, project.getActualStartDate().toString());
        }
        if(project.getActualCompletionDate() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ACTUAL_COMPLETION, project.getActualCompletionDate().toString());
        }
        if(project.getLoanAmount() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_LOAN_AMOUNT, project.getLoanAmount().doubleValue());
        }
        if(project.getContractAmount() != null) {
            contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_CONTRACT_AMOUNT, project.getContractAmount().doubleValue());
        }
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_NUM_AG_FLOORS, project.getNumAGFloors());
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_NUM_BASEMENT_FLOORS, project.getNumBasementFloors());
        contentValues.put(Project.ProjectEntry.COLUMN_SQUARE_FEET, project.getSquareFeet());
        contentValues.put(Project.ProjectEntry.COLUMN_HAS_OUTDOOR_WORK, project.isHasOutdoorWork());
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_DATE_CREATED, project.getDateCreated().toString());

        return contentValues;
    }

    public ContentValues getContentValues(Address address) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Address.AddressEntry.ADDRESS_ID, address.getId());
        contentValues.put(Address.AddressEntry.CITY, address.getCity());
        contentValues.put(Address.AddressEntry.STATE, address.getState());
        contentValues.put(Address.AddressEntry.STREET_NAME, address.getStreeAddress());
        contentValues.put(Address.AddressEntry.STREET_NUMBER, address.getStreetNumber());
        contentValues.put(Address.AddressEntry.ZIP_CODE, address.getZip());
        return contentValues;
    }

    public ContentValues getContentValuesExtra1(Project project) {
        ContentValues contentValues1 = new ContentValues();
        for (Uri uri: project.getPictures()) {
            contentValues1.put(Project.ProjectEntry.COLUMN_PROJECT_ID, project.getId());
            contentValues1.put(Project.ProjectImageBridge.COLUMN_PROJECT_IMAGE_ID, uri.toString());
        }
        return contentValues1;
    }

    public ContentValues getContentValues(DrawRequestItem co) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ID, co.getId());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_TYPE, co.getType());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_AMOUNT, co.getAmount());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBCONTRACTOR, co.getSubContractor());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_DESCRIPTION, co.getDescription());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_EXECUTED, co.isExecuted());
        contentValues.put(DrawRequestItem.DrawRequestItemEntry.COLUMN_DRAW_REQUEST_ITEM_SUBMITTED, co.getDateSubmitted().toString());

        return contentValues;
    }

    public ContentValues getContentValues(String drawRequestId, DrawRequestItem drawRequestItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID, drawRequestId);
        contentValues.put(DrawRequest.DrawRequestItemBridge.COLUMN_DRAW_REQUEST_ITEM_ID, drawRequestItem.getId());
        return contentValues;
    }

    public ContentValues getContentValues(SiteVisit siteVisit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_ID, siteVisit.getId());
        contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_TIME_STARTED, siteVisit.getTimeStarted().toString());
        if (siteVisit.getLastEntry() != null) {
            contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_LAST_ENTRY, siteVisit.getLastEntry().toString());
        }
        int b;
        if (siteVisit.isActive()) {b = 1;} else {b = 0;}
        contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_IS_ACTIVE, b);
        contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_PROJECT_ID, siteVisit.getProjectId());
        return contentValues;
    }

    public ContentValues getContentValues(String projectId, SiteVisit siteVisit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Project.ProjectEntry.COLUMN_PROJECT_ID, projectId);
        contentValues.put(SiteVisit.SiteWalkEntry.COLUMN_ID, siteVisit.getId());
        return contentValues;
    }

    /* for STOCK fieldValue objects, meaning they are not owned by an owner, rather a type

     */
    public ContentValues getContentValues(int type, FieldValue fv) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID, fv.getId());
        contentValues.put(FieldValue.StockFieldValueTable.COL_FIELD_VALUE_OWNER_TYPE, type);
        contentValues.put(FieldValue.FieldValueTable.COL_FIELD, fv.getField());
        int in;
        if (fv.getIn()) {in = 1;} else {in = 0;}
        contentValues.put(FieldValue.FieldValueTable.COL_IN, in);
        if (fv.getRequired()) {in = 1;} else {in = 0;}
        contentValues.put(FieldValue.FieldValueTable.COL_REQUIRED, in);
        return contentValues;
    }

    /*
        for OWNED Field Value pairs, this means the value part may be filled out.
     */

    public ContentValues getContentValues(FieldValue fv) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldValue.FieldValueTable.COLUMN_FIELD_VALUE_ID, fv.getId());
        if (fv.getOwnerId() == null) {
                Log.i("THERE IS NO OWNER ID", "FieldValue- " + fv.getField());
            }
        contentValues.put(FieldValue.FieldValueTable.COL_OWNER_ID, fv.getOwnerId());
        contentValues.put(FieldValue.FieldValueTable.COL_FIELD, fv.getField());
        contentValues.put(FieldValue.FieldValueTable.COL_VALUE, fv.getValue());
        int in;
        if (fv.getIn()) {in = 1;} else {in = 0;}
        contentValues.put(FieldValue.FieldValueTable.COL_IN, in);
        if (fv.getRequired()) {in = 1;} else {in = 0;}
        contentValues.put(FieldValue.FieldValueTable.COL_REQUIRED, in);
        return contentValues;
    }

    public Project getCursorProject(Cursor cursor) {
            Project project = Project.initializeDBProject();
            if (cursor.getString(0) != null) {
                project.setId(cursor.getString(0));
            }
            if (cursor.getString(1) != null) {
                project.setName(cursor.getString(1));
            }
//            if (cursor.getString(2) != null) {
//                project.setAddress(cursor.getString(2));
//            }
            if (cursor.getString(3) != null) {
                project.setUser(Storage.getUserById(cursor.getString(3)));
            }
            if (cursor.getString(4) != null) {
                project.setInitialStartDate(cursor.getString(4));
            }
            if (cursor.getString(5) != null) {
                project.setInitialCompletionDate(cursor.getString(5));
            }
            if (cursor.getString(6) != null) {
                project.setActualStartDate(cursor.getString(6));
            }
            if (cursor.getString(7) != null) {
                project.setActualCompletionDate(cursor.getString(7));
            }
            if (cursor.getString(8) != null) {
                project.setLoanAmount(cursor.getString(8));
            }
            if (cursor.getString(9) != null) {
                project.setContractAmount(cursor.getString(9));
            }
            if (cursor.getString(10) != null) {
                project.setNumAGFloors(cursor.getString(10));
            }
            if (cursor.getString(11) != null) {
                project.setNumBasementFloors(cursor.getString(11));
            }
            if (cursor.getString(12) != null) {
                project.setSquareFeet(cursor.getString(13));
            }
            if (cursor.getString(13) != null) {
                if (cursor.getString(13) == "0") {
                    project.setHasOutdoorWork(true);
                } else {
                    project.setHasOutdoorWork(false);
                }
            }
            if (cursor.getString(14) != null) {
                project.setDateCreated(cursor.getString(14));
            }
            project.setSitePictures(getProjectSitePictures(project.getId()));

            return project;
    }

    private Address getCursorAddress(Cursor cursor) {
        Address address = Address.getDBAddress(cursor.getString(0));
        address.setCity(cursor.getString(1))
                .setState(cursor.getString(2))
                .setStreetAddress(cursor.getString(3))
                .setStreetNumber(cursor.getString(4))
                .setZip(cursor.getString(5));
        return address;

    }

    private WalkThrough getCursorWalkThrough(Cursor cursor) {
            WalkThrough wt = WalkThrough.getDBWalkThrough();
            wt.setId(cursor.getString(0));
            wt.setFloor(cursor.getString(1));
            wt.setTrade(cursor.getString(2));
            wt.setProgress(cursor.getString(3));
            wt.setNote(cursor.getString(4));
            wt.setDate(cursor.getString(5));
            for (String uri : getWalkThroughPicIds(wt.getId())) {
                wt.addSitePicture(uri);
            }
            return wt;
    }

    private DrawRequestItem getDrawRequestItem(Cursor cursor) {
            DrawRequestItem co = DrawRequestItem.getDBDrawRequestItem();
            co.setId(cursor.getString(0));
            co.setType(cursor.getString(1));
            co.setSubContractor(cursor.getString(2));
            co.setAmount(cursor.getString(3));
            co.setDescription(cursor.getString(4));
            co.setExecuted(cursor.getString(5));
            co.setDateSubmitted(cursor.getString(6));
            return co;
    }

    private DrawRequest getCursorDrawRequest(Cursor cursor) {
            DrawRequest dr = DrawRequest.getDBDrawRequest();
            dr.setId(cursor.getString(0));
            dr.setDate(cursor.getString(1));
            dr.setCurrentRequest(cursor.getString(2));
            dr.setCurrentRecommendation(cursor.getString(3));
            dr.setConditions(cursor.getString(4));
            return dr;
    }

    private SiteVisit getCursorSiteWalk(Cursor cursor) {
        SiteVisit sw = SiteVisit.getDBSiteWalk();
        sw.setId(cursor.getString(0));
        sw.setTimeStarted(new Date(cursor.getString(1)));
        if (cursor.getString(2) != null) {
            sw.setLastEntry(new Date(cursor.getString(2)));
        }
        if (cursor.getString(3).equals("1")) {
            sw.setActive(true);
        } else {
            sw.setActive(false);
        }
        if (cursor.getString(4) != null) {
            sw.setProjectId(cursor.getString(4));
        }
        return sw;
    }

    private FieldValue getCursorFieldValue(Cursor cursor) {
        FieldValue fv = new FieldValue(cursor.getString(0));
        fv.setOwnerId(cursor.getString(1));
        fv.setField(cursor.getString(2));
        fv.setValue(cursor.getString(3));
        fv.setIn(cursor.getInt(4));
        fv.setRequired(cursor.getInt(5));
        return fv;
    }

    private FieldValue getCursorStockFieldValue(Cursor cursor) {
        FieldValue fv = new FieldValue(cursor.getString(0));
        fv.setField(cursor.getString(1));
        fv.setIn(cursor.getInt(2));
        fv.setRequired(cursor.getInt(3));
        return fv;
    }
}
