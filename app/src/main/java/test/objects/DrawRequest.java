package test.objects;

import android.provider.BaseColumns;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import test.persistence.Constants;

/**
 * Created by willpassidomo on 1/15/15.
 */
public class DrawRequest {
    private String id;
    private Date date;
    private BigDecimal currentRequest;
    private BigDecimal currentRecommendation;
    private String conditions;

    private Map<String, List<DrawRequestItem>> drawRequestItemLists = new HashMap<String, List<DrawRequestItem>>();

    public DrawRequest() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
    }

    public DrawRequest(String nothing) {}

    public static DrawRequest getDBDrawRequest() {
        return new DrawRequest("");
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

    public List<DrawRequestItem> getItemList(String type) {
        if (getDrawRequestItemLists().containsKey(type)) {
            return getDrawRequestItemLists().get(type);
        } else {
            return new ArrayList<>();
        }
    }

    public List<DrawRequestItem> getItemList() {
        List<DrawRequestItem> drawRequestItems = new ArrayList<DrawRequestItem>();
        for(List<DrawRequestItem> driList: getDrawRequestItemLists().values()) {
            for (DrawRequestItem dri: driList) {
                drawRequestItems.add(dri);
            }
        }
        return drawRequestItems;
    }

    public void addDrawRequestItem(DrawRequestItem drawRequestItem) {
        if (getDrawRequestItemLists().containsKey(drawRequestItem.getType())) {
            List<DrawRequestItem> drawRequestItems = getDrawRequestItemLists().get(drawRequestItem.getType());
            if (!drawRequestItems.contains(drawRequestItem)) {
                drawRequestItems.add(drawRequestItem);
                getDrawRequestItemLists().put(drawRequestItem.getType(), drawRequestItems);
            }
        } else {
            List<DrawRequestItem> drList = new ArrayList<>();
            drList.add(drawRequestItem);
            getDrawRequestItemLists().put(drawRequestItem.getType(), drList);
        }
    }

    public void addDrawRequestItems(List<DrawRequestItem> drawRequestItems) {
        for (DrawRequestItem dri: drawRequestItems) {
            addDrawRequestItem(dri);
        }
    }

    public String getCurrentRequest() {return currentRequest.toString();}

    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = new BigDecimal(currentRequest);
    }

    public String getCurrentRecmomendation() {
        return currentRecommendation.toString();
    }

    public void setCurrentRecommendation(String currentRecommendation) {
        this.currentRecommendation = new BigDecimal(currentRecommendation);
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Map<String, List<DrawRequestItem>> getDrawRequestItemLists() {
        return drawRequestItemLists;
    }

    public void setDrawRequestItemLists(Map<String, List<DrawRequestItem>> drawRequestItemLists) {
        this.drawRequestItemLists = drawRequestItemLists;
    }


    public static abstract class DrawRequestEntry implements BaseColumns {

        public static final String TABLE_NAME = "baseColumns";
        public static final String COLUMN_DRAW_REQUEST_ID = "drawRequestId";
        public static final String COLUMN_DRAW_REQUEST_DATE = "drawRequestDate";
        public static final String COLUMN_DRAW_REQUEST_CURRENT_REQUEST = "drawRequestCurrentRequest";
        public static final String COLUMN_DRAW_REQUEST_CURRENT_RECOMMENDATION = "drawRequestCurrentRecommendation";
        public static final String COLUMN_DRAW_REQUEST_NOTE = "drawRequestNote";

        public static final String CREATE_DRAW_REQUEST_TABLE = Constants.createTableString(
                TABLE_NAME,
                COLUMN_DRAW_REQUEST_ID + Constants.TEXT_TYPE,
                COLUMN_DRAW_REQUEST_DATE + Constants.TEXT_TYPE,
                COLUMN_DRAW_REQUEST_CURRENT_REQUEST + Constants.TEXT_TYPE,
                COLUMN_DRAW_REQUEST_CURRENT_RECOMMENDATION + Constants.TEXT_TYPE,
                COLUMN_DRAW_REQUEST_NOTE + Constants.TEXT_TYPE
        );
    }

    public static abstract class DrawRequestItemBridge implements BaseColumns {

        public static final String COLUMN_DRAW_REQUEST_ITEM_ID = "drawRequestItemId";
        public static final String TABLE_NAME = "drawRequestItemBridge";
        public static final String CREATE_DRAW_REQUEST_ITEM_BRIDGE_TABLE = Constants.createTableString(
                TABLE_NAME,
                DrawRequestEntry.COLUMN_DRAW_REQUEST_ID + Constants.TEXT_TYPE,
                COLUMN_DRAW_REQUEST_ITEM_ID + Constants.TEXT_TYPE
        );
    }
}
