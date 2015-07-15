package test.persistence;

import android.content.Context;
import android.provider.BaseColumns;
import android.util.Log;

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
    private static DataBaseStorage dataBaseStorage;
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
        try {
        setDataBaseStorage(context);
        dataBaseStorage.insertProject(project);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeProjectQuestions(Context context, String projectId, List<FieldValue> fvs) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT, fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeSiteVisit(Context context, String projectId, SiteVisit siteVisit) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertSiteWalk(projectId, siteVisit);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeSiteVisitQuestions(Context context, List<FieldValue> fvs) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT, fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeFieldValues(Context context, List<FieldValue> fvs) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertFieldValues(fvs);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeWalkThrough(Context context, String siteWalkId, WalkThrough walkThrough) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.insertWalkThrough(siteWalkId, walkThrough);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeDrawRequest(Context context, String siteWalkId, DrawRequest drawRequest) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.insertDrawRequest(siteWalkId, drawRequest);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeDrawRequestItem(Context context, String drawRequestId, DrawRequestItem drawRequestItem) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertDrawRequestItem(drawRequestId, drawRequestItem);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static void storeTradeList(Context context, List<String> trades, String projectId) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertProjectTrades(trades, projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeTrades(Context context, List<String> trades) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertTrades(trades);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void storeProgresses(Context context, List<String> progresses) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.insertProgresses(progresses);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteProject(Context context, Project project) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.deleteProject(project);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteWalkThrough(Context context, String walkThroughId) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.deleteWalkThrough(walkThroughId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteDrawRequestItem(Context context, String drawRequestId) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.deleteDrawRequestItem(drawRequestId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteDrawRequest(Context context, DrawRequest drawRequest) {
        try {
        setDataBaseStorage(context);
        dataBaseStorage.deleteDrawRequest(drawRequest);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteFieldValue(Context context, String fieldValueId) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.deleteFieldValue(fieldValueId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteProgress(Context context, String progress) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.deleteProgress(progress);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static void deleteTrade(Context context, String trade) {
        try {
            setDataBaseStorage(context);
            dataBaseStorage.deleteTrade(trade);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static Project getProjectById(Context context, String id) {
        try {
            setDataBaseStorage(context);
            Project project = dataBaseStorage.getProject(id);
            project.setDrawRequests(dataBaseStorage.getProjectDrawRequests(id));
            return project;
        } finally {
            dataBaseStorage.close();
        }
    }

    public static SiteVisit getSiteWalkById(Context context, String id) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getSiteWalk(id);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static DrawRequest getDrawRequestBySiteWalkId(Context context, String siteWalkId) {
        try {
            setDataBaseStorage(context);
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

        setDataBaseStorage(context);
        return dataBaseStorage.getAllProjects();
    }

    public static List<SiteVisit> getActiveSiteWalks(Context context, String projectId) {
        try {
            setDataBaseStorage(context);
            List<String> siteWalkIds = dataBaseStorage.getSiteWalkIds(projectId);
            Log.i("total site walks found", "#" + siteWalkIds.size());
            return dataBaseStorage.getActiveSiteWalks(siteWalkIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static  List<SiteVisit> getSiteWalks(Context context, String projectId) {
        try {
            setDataBaseStorage(context);
            List<String> siteWalkIds = dataBaseStorage.getSiteWalkIds(projectId);
            return dataBaseStorage.getSiteWalks(siteWalkIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<WalkThrough> getWalkThroughBySiteWalkId(Context context, String siteWalkId) {
        try {
            setDataBaseStorage(context);
            List<String> walkThroughIds = dataBaseStorage.getSiteWalktWalkThroughs(siteWalkId);
            return dataBaseStorage.getWalkThroughs(walkThroughIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<WalkThrough> getWalkThroughs(Context context, List<String> walkThroughIds) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getWalkThroughs(walkThroughIds);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static Map<Date, ArrayList<String>> getSWalkThroughDates(Context context, String siteWalkId) {
        try {
            setDataBaseStorage(context);
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
        try {
        setDataBaseStorage(context);
        return dataBaseStorage.getProjectDrawRequests(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static DrawRequestItem getChangeOrderById(Context context, String changeOrderId) {
        try {
        setDataBaseStorage(context);
        return dataBaseStorage.getChangeOrder(changeOrderId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static DrawRequest getDrawRequestById(Context context, String drawRequestId) {
        try {
        setDataBaseStorage(context);
        return dataBaseStorage.getDrawRequest(drawRequestId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static WalkThrough getWalkThroughById(Context context, String walkThroughId) {
        try {
        setDataBaseStorage(context);
        return dataBaseStorage.getWalkThrough(walkThroughId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<String> getProjectTradeList(Context context, String projectId) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getProjectTrades(projectId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getProjectQuestions(Context context) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_PROJECT);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getProjectQuestions(Context context, String id) {
        try {
            setDataBaseStorage(context);
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

    public static List<FieldValue> getSiteWalkQuestions(Context context) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<FieldValue> getSiteWalkQuestions(Context context, String id) {
        try {
            setDataBaseStorage(context);
            List<FieldValue> fvs = getQuestionsByOwnerId(context, id);
            if (fvs == null || fvs.size() == 0) {
                fvs = dataBaseStorage.getStockFieldValues(Constants.FIELD_VALUE_SITE_VISIT);
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
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getFieldValuesByOwner(ownerId);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static FieldValue getQuestionById(Context context, String id) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getFieldValue(id);
        }
        finally {
            dataBaseStorage.close();
        }
    }

    public static List<String> getTradeList(Context context) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getTrades();
        }
        finally {
            dataBaseStorage.close();
        }
    }


    public static List<String> getProgressList(Context context) {
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getProgresses();
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
        try {
            setDataBaseStorage(context);
            return dataBaseStorage.getRetainageRel(projectId);
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

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static User getUserById(String id) {
        //TODO

        return user;
    }



    private static void setDataBaseStorage(Context context) {
        dataBaseStorage = new DataBaseStorage(context);
    }

    public static void storeTestProject(Context context, Project project) {
        project.setId("test");
        project.setSquareFeet(1250);
        project.setNumBasementFloors(2);
        project.setNumAGFloors(5);
        project.setInitialStartDate("2/10/15");
        project.setInitialCompletionDate("5/22/15");
        project.setAddress("122 Broadway Manhattan,NY,NY 00324");
        project.setName("Broadway Joe's");
        project.setContractAmount(new BigDecimal(1200000));
        project.setDateCreated(new Date());
        project.setLoanAmount(new BigDecimal(1500000));

        setDataBaseStorage(context);

        dataBaseStorage.insertProject(project);
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

}
