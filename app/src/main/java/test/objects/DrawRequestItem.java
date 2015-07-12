package test.objects;

import android.provider.BaseColumns;

import java.util.Date;
import java.util.UUID;

import assesortron.assesortronTaskerAPI.model.DrawRequestItemDTO;
import test.persistence.Constants;

/**
 * Created by willpassidomo on 1/23/15.
 */
public class DrawRequestItem implements Comparable<DrawRequestItem>{

    public static final String CHANGE_ORDER = "change_order";
    public static final String RETAINAGE_RELEASE = "retainage_rel";
    public static final String STORED_MATERIALS = "stored_materials";
    public static final String NEW_DRAW_CONTINGENCY = "draw_from_contingency";
    public static final String BUYOUT = "buyout";

    private String id;
    private String type;
    private String subContractor;
    private double amount;
    private String description;
    private boolean executed;
    private Date dateSubmitted;

    public DrawRequestItem(String type) {
        this.type = type;
        this.setDateSubmitted(new Date());
        this.setId(UUID.randomUUID().toString());
    }

    public static DrawRequestItem getDBDrawRequestItem() {
        return new DrawRequestItem("");
    }

    public String getSubContractor() {
        return subContractor;
    }

    public void setSubContractor(String subContractor) {
        this.subContractor = subContractor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        if (!amount.isEmpty()) {
            this.amount = Double.parseDouble(amount);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public void setExecuted(String executed) {
        this.executed = new Boolean(executed);
    }

    public Date getDateSubmitted() {
        return this.dateSubmitted;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = new Date(dateSubmitted);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return DrawRequestItem.getTitle(this.getType());
    }

    public static String getTitle(String type) {
        switch(type) {
            case CHANGE_ORDER:
                return "Change Order";
            case RETAINAGE_RELEASE:
                return "Retainage Release";
            case STORED_MATERIALS:
                return "Stored Materials";
            case NEW_DRAW_CONTINGENCY:
                return "New Draw From Contingency";
            case BUYOUT:
                return "% Buyout";
            default:
                return "unknown";
        }
    }

    public DrawRequestItemDTO getDTO() {
        DrawRequestItemDTO dto = new DrawRequestItemDTO();
        dto.setAmount(amount);
        dto.setType(type);
        dto.setTypeName(getTitle());
        dto.setDateSubmittedLong(dateSubmitted.getTime());
        dto.setDescription(description);
        dto.setExecuted(executed);
        dto.setId(id);
        dto.setSubContractor(subContractor);
        return dto;
    }

    @Override
    public int compareTo(DrawRequestItem drawRequestItem) {
        return this.getId().compareTo(drawRequestItem.getId());
    }

    public static abstract class DrawRequestItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "drawRequestItem";
        public static final String COLUMN_DRAW_REQUEST_ID = "drawRequestItemId";
        public static final String COLUMN_DRAW_REQUEST_ITEM_TYPE = "drawRequestItemType";
        public static final String COLUMN_DRAW_REQUEST_ITEM_SUBCONTRACTOR = "drawRequestItemSubcontractor";
        public static final String COLUMN_DRAW_REQUEST_ITEM_AMOUNT = "drawRequestItemAmount";
        public static final String COLUMN_DRAW_REQUEST_ITEM_DESCRIPTION = "drawRequestItemDescription";
        public static final String COLUMN_DRAW_REQUEST_ITEM_EXECUTED = "drawRequestItemExecuted";
        public static final String COLUMN_DRAW_REQUEST_ITEM_SUBMITTED = "drawRequestItemDateSub";

        public static final String CREATE_DRAW_REQUES_TITEM_TABLE = Constants.createTableString(
                        TABLE_NAME,
                        COLUMN_DRAW_REQUEST_ID + Constants.TEXT_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_TYPE + Constants.TEXT_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_SUBCONTRACTOR + Constants.TEXT_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_AMOUNT + Constants.REAL_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_DESCRIPTION + Constants.TEXT_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_EXECUTED + Constants.INTEGER_TYPE,
                        COLUMN_DRAW_REQUEST_ITEM_SUBMITTED + Constants.TEXT_TYPE);

    }
}
