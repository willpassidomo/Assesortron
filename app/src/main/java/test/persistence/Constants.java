package test.persistence;

/**
 * Created by willpassidomo on 3/21/15.
 */
public class Constants {
    public static final String PROJECT_ID = "project_id";
    public static final String DRAW_REQUEST_ID = "draw_id";
    public static final String SITE_VISIT_ID = "site_visit_id";
    public static final String WALK_THROUGH_ID = "walk_through_id";
    public static final String DRAW_REQUEST_ITEM_ID = "draw_request_item_id";
    public static final String DRAW_REQUEST_ITEM_FIELD = "field";
    public static final String NEW_OR_EDIT = "new_or_edit";
    public static final String NEW = "new";
    public static final String EDIT = "edit";

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
}
