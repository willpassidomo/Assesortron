package test.persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.objects.DrawRequest;
import test.objects.DrawRequestItem;
import test.objects.Project;
import test.objects.SiteVisit;
import test.objects.User;
import test.objects.WalkThrough;
import test.objects.FieldValue;

/**
 * Created by willpassidomo on 1/14/15.
 */
public class Storage {
    private static User user = new User("USER_FIRST", "USER_LAST");

    public static List<String> getMasterTradeList() {
        List<String> l = new ArrayList<String>();
        l.add("atrade");
        l.add("atripe");
        l.add("attitude");
        l.add("at");
        l.add("atribute");
        l.add("ahoy");
        return l;
    }

    public static void storeProject(Context context, Project project) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.insertProject(project);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeProjectQuestions(Context context, String projectId, List<FieldValue> fvs) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT, fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeSiteVisit(Context context, String projectId, SiteVisit siteVisit) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertSiteWalk(projectId, siteVisit);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeSiteVisitQuestions(Context context, List<FieldValue> fvs) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT, fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeFieldValues(Context context, List<FieldValue> fvs) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertFieldValues(fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeWalkThrough(Context context, String siteWalkId, WalkThrough walkThrough) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.insertWalkThrough(siteWalkId, walkThrough);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeDrawRequest(Context context, String siteWalkId, DrawRequest drawRequest) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.insertDrawRequest(siteWalkId, drawRequest);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeDrawRequestItem(Context context, String drawRequestId, DrawRequestItem drawRequestItem) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertDrawRequestItem(drawRequestId, drawRequestItem);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static void storeProjectTradeList(Context context, List<String> trades, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            Log.i("FOR PROJECT ID", projectId);
            Log.i("num trades- ", trades.size() + "");
            for (String s : trades) {
                Log.i("Inserting trade:", s);
            }
            dataBaseStorage.insertProjectTrades(trades, projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeTrades(Context context, List<String> trades) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertTrades(trades);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeProgresses(Context context, List<String> progresses) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertProgresses(progresses);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storePicture(Context context, String pictureId, byte[] bytes) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.insertPicture(pictureId, bytes);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteProject(Context context, Project project) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.deleteProject(project);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteProject(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deleteProject(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deletePicture(Context context, String pictureId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deletePicture(pictureId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteWalkThrough(Context context, String walkThroughId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.deleteWalkThrough(walkThroughId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteDrawRequestItem(Context context, String drawRequestId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.deleteDrawRequestItem(drawRequestId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteDrawRequest(Context context, DrawRequest drawRequest) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        dataBaseStorage.deleteDrawRequest(drawRequest);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteFieldValue(Context context, String fieldValueId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deleteFieldValue(fieldValueId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteProgress(Context context, String progress) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deleteProgress(progress);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteTrade(Context context, String trade) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deleteTrade(trade);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static Project getProjectById(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            Project project = dataBaseStorage.getProject(id);
            project.setDrawRequests(dataBaseStorage.getProjectDrawRequests(id));
            return project;
        } finally {
            dataBaseStorage.close();
        }
    }

    public static SiteVisit getSiteWalkById(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getSiteWalk(id);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static DrawRequest getDrawRequestBySiteWalkId(Context context, String siteWalkId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getSiteWalkDrawRequest(siteWalkId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<Project> getActiveProjects(Context context) {
        //TODO
        //make a network call if you dont have the projects yet, return the projects from active memory
        //if the program does

        //returns just active projects, not submited ones..if that is a hing

        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        return dataBaseStorage.getAllProjects();
    }

    public static List<SiteVisit> getActiveSiteWalks(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> siteWalkIds = dataBaseStorage.getSiteWalkIds(projectId);
            Log.i("total site walks found", "#" + siteWalkIds.size());
            return dataBaseStorage.getSiteWalks(siteWalkIds, true);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<SiteVisit> getFinishedSiteWalks(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> siteWalkIds = dataBaseStorage.getSiteWalkIds(projectId);
            return dataBaseStorage.getSiteWalks(siteWalkIds, false);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static  List<SiteVisit> getSiteWalks(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> siteWalkIds = dataBaseStorage.getSiteWalkIds(projectId);
            return dataBaseStorage.getSiteWalks(siteWalkIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<WalkThrough> getWalkThroughBySiteWalkId(Context context, String siteWalkId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> walkThroughIds = dataBaseStorage.getSiteWalktWalkThroughs(siteWalkId);
            return dataBaseStorage.getWalkThroughs(walkThroughIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<WalkThrough> getWalkThroughs(Context context, List<String> walkThroughIds) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getWalkThroughs(walkThroughIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static Map<Date, ArrayList<String>> getSWalkThroughDates(Context context, String siteWalkId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> walkThroughIds = dataBaseStorage.getSiteWalktWalkThroughs(siteWalkId);
            Map<Date, String> dates = dataBaseStorage.getWalkThroughDates(walkThroughIds);
            Map<Date, ArrayList<String>> dateGroups = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            for (Date date: dates.keySet()) {
                try {
                    Date newDate = sdf.parse(sdf.format(date));
                    ArrayList<String> wtid = dateGroups.get(newDate);
                    if (wtid  == null) {
                        wtid = new ArrayList<String>();
                    }
                    wtid.add(dates.get(date));
                    dateGroups.put(newDate, wtid);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return dateGroups;
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<DrawRequest> getDrawRequestByProjectId(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        return dataBaseStorage.getProjectDrawRequests(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static DrawRequestItem getChangeOrderById(Context context, String changeOrderId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        return dataBaseStorage.getChangeOrder(changeOrderId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static DrawRequest getDrawRequestById(Context context, String drawRequestId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        return dataBaseStorage.getDrawRequest(drawRequestId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static WalkThrough getWalkThroughById(Context context, String walkThroughId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
        return dataBaseStorage.getWalkThrough(walkThroughId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<String> getProjectTradeList(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getProjectTrades(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getProjectQuestions(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_PROJECT);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getProjectQuestions(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<FieldValue> fvs = getQuestionsByOwnerId(context, id);
            if (fvs == null || fvs.size() == 0) {
                fvs = dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_PROJECT);
                for(FieldValue fv: fvs) {
                    fv.setOwnerId(id);
                }
            }
            return fvs;
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getSiteWalkQuestions(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<FieldValue> fvs = getQuestionsByOwnerId(context, id);
            if (fvs == null || fvs.size() == 0) {
                fvs = dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT);
                if (fvs == null || fvs.size() == 0) {
                    Constants.storeInitialSiteWalkQuestions(context);
                    getSiteWalkQuestions(context, id);
                }
                for (FieldValue fv: fvs) {
                    fv.setOwnerId(id);
                }
            }
            return fvs;
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getQuestionsByOwnerId(Context context, String ownerId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getFieldValuesByOwner(ownerId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static FieldValue getQuestionById(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getFieldValue(id);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<String> getTradeList(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> trades = dataBaseStorage.getTrades();
            if (trades.size() < 1) {
                trades = Constants.getInitalTrades();
                Storage.storeTrades(context, trades);
            }
            return trades;
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static List<String> getProgressList(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            List<String> progresses = dataBaseStorage.getProgresses();
            if (progresses.size() < 1) {
                progresses = Constants.getInitialProgresses();
                Storage.storeProgresses(context, progresses);
            }
            return progresses;
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static int getWalkThroughEntryCount(Context context, String siteWalkId) {
        return getWalkThroughBySiteWalkId(context, siteWalkId).size();
    }

    public static int getDrawRequestItemCount(Context context, String siteWalkId) {
        DrawRequest dr = getDrawRequestBySiteWalkId(context, siteWalkId);
        if (dr != null) {
            return dr.getItemList().size();
        } else {
            return 0;
        }

    }

    public static String getRetainageRel(Context context, String projectId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getRetainageRel(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static Bitmap getPictureByOwnerId(Context context, String pictureId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            byte[] bytes = dataBaseStorage.getPictureByteArray(pictureId);
            return BitmapFactory.decodeByteArray(bytes, 0, 0);
        }
        finally {
            dataBaseStorage.close();
        }
    }



//    public static String getBalanceToComplete(Context context, String ProjectId) {
//        Project project = Storage.getProjectById(projectId);
//        project.get
//    }

//    public static List<>

    public static List<String> getUserNames(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getUserNames();
        }
        finally {
            dataBaseStorage.close();
        }    }

    public static boolean storeUser(Context context, User newUser, String password) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.insertUser(newUser, password);
        }
        finally {
            dataBaseStorage.close();
        }    }

    public static User getUserById(Context context, String id) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.getUserById(id);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static String login(Context context, String username, String password) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            return dataBaseStorage.checkLoginCredentials(username, password);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    private static String loggedInId;

    public static boolean isLoggedIn(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            loggedInId = dataBaseStorage.getLoggedIn();
            return (loggedInId != null && !loggedInId.equals(""));
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static String getLoggedInId() {
        return loggedInId;
    }

    public static void setLoggedInId(Context context, String userId) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.setLoggedin(userId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void removeLoggedInId(Context context) {
        DataBaseStorage dataBaseStorage = DataBaseStorage.getDataBaseStorage(context);
        try {
            dataBaseStorage.deleteLoggedInId();
        } finally {
            dataBaseStorage.close();
        }
    }

    public static abstract class ProgressesTable implements BaseColumns {
       public static final String TABLE_NAME_PROGRESSES = "progresses";
       public static final String COLUMN_PROGRESS_STRING = "progressString";


       public static final String TEXT_TYPE = " TEXT";
       public static final String COMMA_SEP = " ,";

       public static final String CREATE_PROGRESSES_STRING_TABLE =
               Constants.createBridgeTableString(
                       TABLE_NAME_PROGRESSES,
                       Project.ProjectEntry.COLUMN_PROJECT_ID + TEXT_TYPE,
                       COLUMN_PROGRESS_STRING + TEXT_TYPE
               );
   }

    public static abstract class TradesTable implements BaseColumns {
        public static final String TABLE_NAME_TRADES = "trades";
        public static final String COLUMN_TRADE_STRING = "tradeString";

        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = " ,";

        public static final String CREATE_TRADES_STRING_TABLE =
                Constants.createBridgeTableString(
                        TABLE_NAME_TRADES,
                        Project.ProjectEntry.COLUMN_PROJECT_ID + TEXT_TYPE,
                        COLUMN_TRADE_STRING + TEXT_TYPE);
    }

    public static abstract class PictureTable implements BaseColumns {
        public static final String TABLE_NAME = "pictures";
        public static final String COLUMN_ID = "picId";
        public static final String COLUMN_BYTES = "bytes";
        public static final String CREATE_TABLE_PICTURES =
                Constants.createTableString(
                        TABLE_NAME,
                        COLUMN_ID + Constants.TEXT_TYPE,
                        COLUMN_BYTES + Constants.BLOB
                );
    }
}
