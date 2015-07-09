package test.objects;

import android.provider.BaseColumns;

import java.util.UUID;

import assesortron.assesortronTaskerAPI.model.FieldValueDTO;
import test.persistence.Constants;

/**
 * Created by otf on 6/11/15.
 */
public class FieldValue implements Comparable {
    private String field;
    /// TODO
    // This value should not be string, should be generic....so if it is a number value. it will
    //check it properly...make it so the correct keyboard configuration appers when the field is
    //selected
    private String value;
    private boolean required = false;
    private boolean in;
    private String id;
    private String ownerId;

    public FieldValue(String ownerId, String field, String value, boolean in, boolean required) {
        this(ownerId, field, value, required);
        this.in = in;
    }

    public FieldValue(String field, boolean in, boolean required) {
        this(field, in);
        this.required = required;
    }

    public FieldValue(String ownerId, String field, String value, boolean in) {
        this(field, in);
        this.ownerId = ownerId;
        this.value = value;
    }

    public FieldValue(String field, boolean in) {
        this.id = UUID.randomUUID().toString();
        this.field = field;
        this.in = in;
        this.value = "";
    }

    public FieldValue(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return this.value;
    }

    public String getId() { return this.id;}

    public String getOwnerId() { return this.ownerId;}

    public void setOwnerId(String ownerId) {this.ownerId = ownerId;}

    public boolean getIn() { return this.in;}

    public void setIn(boolean in) { this.in = in;}

    public void setIn(int in) {
        if(in == 0) {
            this.in = false;
        }
        if(in == 1) {
            this.in = true;
        }
    }

    public void setRequired(int req) {
        if(req == 0) {
            this.required = false;
        }
        if(req == 1) {
            this.required = true;
        }
    }

    public boolean getRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public FieldValueDTO getDTO() {
        FieldValueDTO dto = new FieldValueDTO();
        dto.setField(field);
        dto.setValue(value);
        dto.setOwnerId(ownerId);
        dto.setIn(in);
        dto.setRequired(required);
        dto.setId(id);
        return dto;
    }

    @Override
    public boolean equals(Object fv) {
        if (fv instanceof FieldValue) {
            FieldValue fvv = (FieldValue)fv;
            return (this.field.equals(fvv.getField()));// && value.equals(fvv.getValue()));
        }
        return false;
    }

    @Override
    public int compareTo(Object fv) {
        if (fv instanceof FieldValue) {
            FieldValue fvv = (FieldValue)fv;
            return this.getField().compareTo(fvv.getField());
        } else {
            return 1;}
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public String toString() {
        return field;
    }

    public static abstract class FieldValueTable implements BaseColumns {
        public static final String TABLE_NAME = "fieldValueTable";
        public static final String COLUMN_FIELD_VALUE_ID = "fieldValueId";
        public static final String COL_OWNER_ID = "ownerId";
        public static final String COL_VALUE = "value";
        public static final String COL_FIELD = "field";
        public static final String COL_IN = "inC";
        public static final String COL_REQUIRED = "required";

        public static final String CREATE_FIELD_VALUE_TABLE = Constants.createTableString(
                TABLE_NAME,
                COLUMN_FIELD_VALUE_ID + Constants.TEXT_TYPE,
                COL_OWNER_ID + Constants.TEXT_TYPE,
                COL_FIELD + Constants.TEXT_TYPE,
                COL_VALUE + Constants.TEXT_TYPE,
                COL_IN + Constants.INTEGER_TYPE,
                COL_REQUIRED + Constants.INTEGER_TYPE);
    }

    public static abstract class StockFieldValueTable implements BaseColumns {
        public static final String TABLE_NAME = "stockFieldValuesTable";
        public static final String COL_FIELD_VALUE_OWNER_TYPE = "fieldValueOwnerType";

        public static final String CREATE_STOCK_FIELD_VALUE_TABLE = Constants.createTableString(
                TABLE_NAME,
                FieldValueTable.COLUMN_FIELD_VALUE_ID + Constants.TEXT_TYPE,
                COL_FIELD_VALUE_OWNER_TYPE + Constants.INTEGER_TYPE,
                FieldValueTable.COL_FIELD + Constants.TEXT_TYPE,
                FieldValueTable.COL_IN + Constants.INTEGER_TYPE,
                FieldValueTable.COL_REQUIRED + Constants.INTEGER_TYPE);
    }
}
