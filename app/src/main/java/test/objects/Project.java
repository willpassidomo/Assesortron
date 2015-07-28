package test.objects;


import android.net.Uri;
import android.provider.BaseColumns;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import assesortron.assesortronTaskerAPI.model.ProjectDTO;
import test.persistence.Constants;

/**
 * Created by willpassidomo on 1/14/15.
 */
public class Project {
    private String id;
    private Date dateCreated;
    private String userId;
    private String name;
    private Address address;
    private String initialStartDate;
    private String initialCompletionDate;
    private String actualStartDate;
    private String actualCompletionDate;
    private BigDecimal loanAmount;
    private BigDecimal contractAmount;
    private double retainageRel;
    private int numAGFloors;
    private int numBasementFlors;
    private double squareFeet;
    private boolean hasOutdoorWork;

    private List<Uri> pictures = new ArrayList<Uri>();

    private List<DrawRequestItem> changeOrders;// = new ArrayList<ChangeOrder>();
    private List<DrawRequest> drawRequests;// = new ArrayList<DrawRequest>();
    private List<WalkThrough> walkThroughs;// = new ArrayList<WalkThrough>();

    public Project(String string) {

    }

    public Project() {
        this.id = UUID.randomUUID().toString();
        this.dateCreated = new Date();
    }

    public static Project initializeDBProject() {
        return new Project("");
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getInitialStartDate() {
        return initialStartDate;
    }

    public void setInitialStartDate(String initialStartDate) {
        this.initialStartDate = initialStartDate;
    }

    public String getInitialCompletionDate() {
        return initialCompletionDate;
    }

    public void setInitialCompletionDate(String initialCompletionDate) {
        this.initialCompletionDate = initialCompletionDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(String actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        if (loanAmount != null && !loanAmount.isEmpty()) {
            this.loanAmount = new BigDecimal(loanAmount);
        }
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public void setContractAmount(String contractAmount) {
        if (contractAmount != null && !contractAmount.isEmpty()) {
            this.contractAmount = new BigDecimal(contractAmount);
        }
    }

    public int getNumAGFloors() {
        return numAGFloors;
    }

    public void setNumAGFloors(int numAGFloors) {
        this.numAGFloors = numAGFloors;
    }

    public void setNumAGFloors(String numAGFloors) {
        if (numAGFloors != null && !numAGFloors.isEmpty()) {
            this.numAGFloors = Integer.parseInt(numAGFloors);
        }
    }

    public int getNumBasementFloors() {
        return numBasementFlors;
    }

    public void setNumBasementFloors(int numBasementFlors) {
        this.numBasementFlors = numBasementFlors;
    }

    public void setNumBasementFloors(String numBasementFlors) {
        if (numBasementFlors != null && !numBasementFlors.isEmpty()) {
            this.numBasementFlors = Integer.parseInt(numBasementFlors);
        }
    }

    public boolean isHasOutdoorWork() {
        return hasOutdoorWork;
    }

    public void setHasOutdoorWork(boolean hasOutdoorWork) {
        this.hasOutdoorWork = hasOutdoorWork;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateCreated(String date) {
        this.dateCreated= new Date(date);
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public double getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(double squareFeet) {
        this.squareFeet = squareFeet;
    }

    public void setSquareFeet(String squareFeet) {
        if (squareFeet != null && !squareFeet.isEmpty()) {
            this.squareFeet = Double.parseDouble(squareFeet);
        }
    }

    public List<DrawRequest> getDrawRequests() {
        return drawRequests;
    }

    public DrawRequest getDrawRequestById(String id) {
        for (DrawRequest dr : drawRequests) {
            if (dr.getId().equals(id)) {
                return dr;
            }
        }
        return null;
    }

    public void addDrawRequests(DrawRequest drawRequest) {
        //this.drawRequests.add(drawRequest);
    }

    public List<WalkThrough> getWalkThroughs() {
        return walkThroughs;
    }

    public WalkThrough getWalkThroughById(String id) {
        for (WalkThrough wt : walkThroughs) {
            if (wt.getId().equals(id)) {
                return wt;
            }
        }
        return null;
    }

    public void addWalkThroughs(WalkThrough walkThrough) {
        //walkThroughs.add(walkThrough);
    }

    public List<DrawRequestItem> getChangeOrders() {
        return changeOrders;
    }

    public DrawRequestItem getChangeOrderById(String id) {
        for (DrawRequestItem co : changeOrders) {
            if (co.getId().equals(id)) {
                return co;
            }
        }
        return null;
    }

    public void addChangeOrder(DrawRequestItem drawRequestItem) {
        //changeOrders.add(changeOrder);
    }

    public WalkThrough removeWalkThrough(int i) {
        return walkThroughs.remove(i);
    }

    public DrawRequest removeDrawRequest(int i) {
        return drawRequests.remove(i);
    }

    public DrawRequestItem removeChangeOrder(int i) {
        return changeOrders.remove(i);
    }

    public void addSitePicture(Uri pictureUri) {
        this.pictures.add(pictureUri);
    }

    public void removeSitePicture(Uri pictureUri) {
        this.pictures.remove(pictureUri);
    }

    public void removeSitePicture(int i) {
        this.pictures.remove(i);
    }

    public List<Uri> getPictures() {
        return this.pictures;
    }

    public Uri getPicture(int i) {
        return this.pictures.get(i);
    }

    public void setSitePictures(List<Uri> sitePictures) {
        this.setPictures(new ArrayList<Uri>(sitePictures));
    }

    public void setPictures(List<Uri> pictures) {
        this.pictures = pictures;
    }

    public void setChangeOrders(List<DrawRequestItem> drawRequestItems) {
        this.changeOrders = drawRequestItems;
    }

    public void setDrawRequests(List<DrawRequest> drawRequests) {
        this.drawRequests = drawRequests;
    }

    public void setWalkThroughs(List<WalkThrough> walkThroughs) {
        this.walkThroughs = walkThroughs;
    }

    public double getRetainageRel() {
        return retainageRel;
    }

    public void setRetainageRel(double retainageRel) {
        this.retainageRel = retainageRel;
    }

    public void setRetainageRel(String retainageRel) {
        if (retainageRel != null && retainageRel.isEmpty()) {
            this.retainageRel = Double.parseDouble(retainageRel);
        }
    }


    public static abstract class ProjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_PROJECT_ID = "projectId";
        public static final String COLUMN_PROJECT_NAME = "projectName";
        public static final String COLUMN_PROJECT_DATE_CREATED = "projectDateCreated";
        public static final String COLUMN_PROJECT_USER_ID = "projectUserId";
        public static final String COLUMN_PROJECT_ADDRESS = "projectAddress";
        public static final String COLUMN_PROJECT_INITIAL_START = "projectInitalStartDate";
        public static final String COLUMN_PROJECT_INITIAL_COMPLETION = "projectInitialCompletionDate";
        public static final String COLUMN_PROJECT_ACTUAL_START = "projectActualStartDate";
        public static final String COLUMN_PROJECT_ACTUAL_COMPLETION = "projectActualCompletionDate";
        public static final String COLUMN_PROJECT_LOAN_AMOUNT = "projectLoanAmount";
        public static final String COLUMN_PROJECT_CONTRACT_AMOUNT = "projectContractAmount";
        public static final String COLUMN_PROJECT_NUM_AG_FLOORS = "projectNumAGFloors";
        public static final String COLUMN_PROJECT_NUM_BASEMENT_FLOORS = "projectNumBasementFloors";
        public static final String COLUMN_SQUARE_FEET = "projectSqareFeet";
        public static final String COLUMN_HAS_OUTDOOR_WORK = "projectHasOutDoorWork";


        public static final String TEXT_TYPE = " TEXT";
        public static final String INTEGER_TYPE = " INTEGER";
        public static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = " , ";

        public static final String CREATE_PROJECT_TABLE =
                "CREATE TABLE " + ProjectEntry.TABLE_NAME + " (" +
                        ProjectEntry.COLUMN_PROJECT_ID + " TEXT PRIMARY KEY, " +
                        ProjectEntry.COLUMN_PROJECT_NAME + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_ADDRESS + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_DATE_CREATED + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_USER_ID + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_INITIAL_START + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_INITIAL_COMPLETION + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_ACTUAL_START + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_ACTUAL_COMPLETION + TEXT_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_LOAN_AMOUNT + REAL_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_CONTRACT_AMOUNT + REAL_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_NUM_AG_FLOORS + INTEGER_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_PROJECT_NUM_BASEMENT_FLOORS + INTEGER_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_SQUARE_FEET + REAL_TYPE + COMMA_SEP +
                        ProjectEntry.COLUMN_HAS_OUTDOOR_WORK + INTEGER_TYPE + ")";

    }

    public static abstract class ProjectImageBridge implements BaseColumns {

        public static final String COLUMN_PROJECT_IMAGE_ID = "projectImageId";
        public static final String TABLE_NAME_PROJECT_PICTURES = "projectPictures";
        public static final String CREATE_PROJECT_IMAGE_TABLE = Constants.createTableString(
                TABLE_NAME_PROJECT_PICTURES,
                ProjectImageBridge._ID + Constants.INTEGER_TYPE,
                ProjectEntry.COLUMN_PROJECT_ID + Constants.TEXT_TYPE,
                COLUMN_PROJECT_IMAGE_ID + Constants.TEXT_TYPE);
    }



    public static abstract class ProjectDrawRequestBridge implements BaseColumns {

        public static final String COLUMN_PROJECT_DRAW_REQUEST_ID = "projectDrawRequestId";
        public static final String TABLE_NAME_PROJECT_DRAW_REQUESTS = "projectDrawRequests";
        public static final String CREATE_PROJECT_DRAW_REQUEST_TABLE = Constants.createTableString(
                TABLE_NAME_PROJECT_DRAW_REQUESTS,
                ProjectDrawRequestBridge._ID + Constants.INTEGER_TYPE,
                ProjectEntry.COLUMN_PROJECT_ID + Constants.TEXT_TYPE,
                COLUMN_PROJECT_DRAW_REQUEST_ID + Constants.TEXT_TYPE
        );
    }

    public static abstract class ProjectWalkThroughBridge implements BaseColumns {

        public static final String COLUMN_PROJECT_WALKTHROUGH_ID = "projectWalkthroughId";
        public static final String TABLE_NAME_PROJECT_WALK_THROUGHS = "projectWalkThroughs";
        public static final String CREATE_PROJECT_WALKTHROUGH_TABLE = Constants.createTableString(
                TABLE_NAME_PROJECT_WALK_THROUGHS,
                ProjectWalkThroughBridge._ID + Constants.INTEGER_TYPE,
                ProjectEntry.COLUMN_PROJECT_ID + Constants.TEXT_TYPE,
                COLUMN_PROJECT_WALKTHROUGH_ID + Constants.TEXT_TYPE
        );
    }

    public static abstract class ProjectTradesBridge implements BaseColumns {
        public static final String TABLE_NAME_PROJECT_TRADES = "projectTrades";
        public static final String COLUMN_PROJECT_TRADES_STRING = "projectTradesString";
        public static final String CREATE_PROJECT_TRADES_TABLE =
                Constants.createBridgeTableString(
                        TABLE_NAME_PROJECT_TRADES,
                        ProjectEntry.COLUMN_PROJECT_ID + Constants.TEXT_TYPE,
                        COLUMN_PROJECT_TRADES_STRING + Constants.TEXT_TYPE
                );
    }

    public static abstract class ProjectSiteWalkBridge implements BaseColumns {
        public static final String TABLE_NAME = "projectSiteVisits";
        public static final String CREATE_PROJECT_SITE_WALK_TABLE = Constants.createBridgeTableString(
                TABLE_NAME,
                ProjectEntry.COLUMN_PROJECT_ID + Constants.TEXT_TYPE,
                SiteVisit.SiteWalkEntry.COLUMN_ID + Constants.TEXT_TYPE
        );
    }



}