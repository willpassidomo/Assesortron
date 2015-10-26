package test.objects;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import assesortron.assesortronTaskerAPI.model.WalkThroughDTO;
import test.persistence.Constants;

/**
 * Created by willpassidomo on 1/15/15.
 */
public class WalkThrough {
   private String id;
   private Date date;
    private String floor;
    private String trade;
    private String progress;
    private String notes = "";
    private List<String> pictures = new ArrayList<String>();

    public WalkThrough() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
    }

    private WalkThrough(String nothing) {}

    public static WalkThrough getDBWalkThrough() {
        return new WalkThrough("");
    }

    public void setNote(String note) {
        this.setNotes(note);
    }

    public String getNotes() {
        return notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = new Date(date);
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void removeSitePicture(Uri pictureUri) {
        this.pictures.remove(pictureUri);
    }

    public void removeSitePicture(int i) {
        this.pictures.remove(i);
    }

    public List<String> getPictures() {
        return this.pictures;
    }

    public String getPicture(int i) {
        return this.pictures.get(i);
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setSitePictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void addSitePicture(String stringUri) {
        pictures.add(stringUri);
    }

    public WalkThroughDTO getDTO() {
        WalkThroughDTO dto = new WalkThroughDTO();
        dto.setId(id);
        dto.setTrade(trade);
        dto.setDateString(date.toString());
        dto.setFloor(floor);
        dto.setNote(notes);
        dto.setProgress(progress);
        return dto;
    }

    public static abstract class WalkThroughEntry implements BaseColumns {

        public static final String COLUMN_WALK_THROUGH_ID = "walkThroughID";
        public static final String COLUMN_WALK_THROUGH_DATE = "walkThroughDate";
        public static final String COLUMN_WALK_THROUGH_FLOOR = "walkThroughFloor";
        public static final String COLUMN_WALK_THROUGH_TRADE = "walkThroughTrade";
        public static final String COLUMN_WALK_THROUGH_PROGRESS = "walkThroughProgress";
        public static final String COLUMN_WALK_THROUGH_NOTE = "walkThroughNote";

        public static final String TABLE_NAME_WALK_THROUGHS = "walkThroughs";

        public static final String TEXT_TYPE = " TEXT";
        public static final String INTEGER_TYPE = "INTEGER";
        public static final String REAL_TYPE = "REAL";
        private static final String COMMA_SEP = ",";

        public static final String CREATE_WALKTHROUGH_TABLE = Constants.createTableString(
                        TABLE_NAME_WALK_THROUGHS,
                        COLUMN_WALK_THROUGH_ID + Constants.TEXT_TYPE,
                        COLUMN_WALK_THROUGH_DATE + Constants.TEXT_TYPE,
                        COLUMN_WALK_THROUGH_FLOOR + Constants.TEXT_TYPE,
                        COLUMN_WALK_THROUGH_TRADE + Constants.TEXT_TYPE,
                        COLUMN_WALK_THROUGH_PROGRESS + Constants.TEXT_TYPE,
                        COLUMN_WALK_THROUGH_NOTE + Constants.TEXT_TYPE
        );
    }

    public static abstract class WalkThroughPictureBridge implements BaseColumns {

        public static final String COLUMN_WALK_THROUGH_PICTURE_URI = "walkThroughPictureURI";
        public static final String TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE = "walkThroughPictureBridge";

        public static final String CREATE_WALKTHROUGH_PICTURE_TABLE =
                "CREATE TABLE " + TABLE_NAME_WALK_THROUGH_PICTURE_BRIDGE + " (" +
                        WalkThroughPictureBridge._ID + " INTEGER PRIMARY KEY, " +
                        WalkThroughEntry.COLUMN_WALK_THROUGH_ID + WalkThroughEntry.TEXT_TYPE + WalkThroughEntry.COMMA_SEP +
                        COLUMN_WALK_THROUGH_PICTURE_URI + WalkThroughEntry.TEXT_TYPE + ")";

    }
}
