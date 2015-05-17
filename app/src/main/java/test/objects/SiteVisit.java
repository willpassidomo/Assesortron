package test.objects;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import assesortron.assesortronTaskerAPI.model.WalkThroughDTO;
import test.persistence.Constants;

/**
 * Created by willpassidomo on 3/4/15.
 */
public class SiteVisit {
    private String id;
    private String projectId;
    private Date timeStarted;
    private Date lastEntry;
    private boolean active = true;

    private ArrayList<WalkThroughDTO> walkThroughs = new ArrayList<>();
    private DrawRequest drawRequest;

    public static SiteVisit getDBSiteWalk() {
        return new SiteVisit();
    }

    public SiteVisit(String projectId) {
        this.timeStarted = new Date();
        this.setId(UUID.randomUUID().toString());
        this.projectId = projectId;
    }

    private SiteVisit() {}

    public Date getStartDate() {
        return timeStarted;
    }

    /**
     * @return the timeStarted
     */
    public Date getTimeStarted() {
        return timeStarted;
    }

    /**
     * @return the lastEntry
     */
    public Date getLastEntry() {
        return lastEntry;
    }

    /**
     * @return the walkThroughs
     */
    public ArrayList<WalkThroughDTO> getWalkThroughs() {
        return walkThroughs;
    }

    /**
     * @return the drawRequests
     */

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    /**
     * @param lastEntry the lastEntry to set
     */
    public void setLastEntry(Date lastEntry) {
        this.lastEntry = lastEntry;
    }


    public void setWalkThroughs(ArrayList<WalkThroughDTO> walkThroughs) {
        this.walkThroughs = walkThroughs;
    }

    public void submit() {
        this.setActive(false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DrawRequest getDrawRequest() {
        return drawRequest;
    }

    public void setDrawRequest(DrawRequest drawRequest) {
        this.drawRequest = drawRequest;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public static abstract class SiteWalkEntry implements BaseColumns {
        public static final String TABLE_NAME = "siteWalks";
        public static final String COLUMN_ID = "siteWalkId";
        public static final String COLUMN_TIME_STARTED = "timeStarted";
        public static final String COLUMN_LAST_ENTRY = "lastEntry";
        public static final String COLUMN_IS_ACTIVE = "isActive";
        public static final String COLUMN_PROJECT_ID = "projectIdc";

        public static final String TEXT_TYPE = " TEXT";

        public static final String CREATE_SITE_WALK_ENTRY_TABLE = Constants.createTableString(
                                                                    TABLE_NAME,
                                                                    COLUMN_ID + TEXT_TYPE,
                                                                    COLUMN_TIME_STARTED + TEXT_TYPE,
                                                                    COLUMN_LAST_ENTRY + TEXT_TYPE,
                                                                    COLUMN_IS_ACTIVE + Constants.INTEGER_TYPE,
                                                                    COLUMN_PROJECT_ID + Constants.TEXT_TYPE);

    }


    public static abstract class SiteWalkDrawRequestBridge implements BaseColumns {

        public static final String TABLE_NAME = "siteWalkDrawRequestBridge";
        public static final String CREATE_SITE_WALK_DRAW_REQUEST_BRIDGE_TABLE = Constants.createTableString(
                TABLE_NAME,
                SiteWalkDrawRequestBridge._ID + Constants.TEXT_TYPE,
                SiteWalkEntry.COLUMN_ID + Constants.TEXT_TYPE,
                DrawRequest.DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + Constants.TEXT_TYPE);
    }

    public static abstract class SiteWalkWalkThroughBridge implements BaseColumns {

        public static final String TABLE_NAME = "SiteWalktWalkThroughs";
        public static final String CREATE_PROJECT_WALKTHROUGH_TABLE = Constants.createTableString(
                TABLE_NAME,
                SiteWalkWalkThroughBridge._ID + Constants.INTEGER_TYPE,
                SiteWalkEntry.COLUMN_ID + Constants.TEXT_TYPE,
                WalkThrough.WalkThroughEntry.COLUMN_WALK_THROUGH_ID + Constants.TEXT_TYPE
        );

    }
}