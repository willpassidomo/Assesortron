package test.objects;

import android.provider.BaseColumns;

import java.util.Date;
import java.util.UUID;

/**
 * Created by willpassidomo on 1/15/15.
 */
public class User {

    private String id;
    private String userName;
    private String password;
    private Date dateCreated;

    public User(String userName, String password) {
        this.setId(UUID.randomUUID().toString());
        this.setUserName(userName);
        this.setPassword(password);
        this.setDateCreated(new Date());
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    private void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public static abstract class UserTable implements BaseColumns {
        public static final String COLUMN_USER_ID = "userId";

        public static final String TEXT_TYPE = " TEXT ";
        public static final String COMMA_SEP = " , ";

        //TODO

        // finish implementing the User database table when the fields for user are finalized

        //SEE INNER CLASSES OF PROJECT.CLASS FOR EXAMPLE
    }

    public static abstract class ProjectUserBridge implements BaseColumns {

        public static final String COLUMN_USER_PROJECT_ID = "userProjectID";



        public static final String COLUMN_PROJECT_WALKTHROUGH_ID = "projectWalkthroughId";
        public static final String TABLE_NAME_PROJECT_WALK_THROUGHS = "projectWalkThroughs";
        public static final String CREATE_PROJECT_WALKTHROUGH_TABLE =
                "CREATE TABLE " + TABLE_NAME_PROJECT_WALK_THROUGHS + " (" +
                        ProjectUserBridge._ID + "INTEGER PRIMARY KEY, " +
                        UserTable.COLUMN_USER_ID + UserTable.TEXT_TYPE + UserTable.COMMA_SEP +
                        COLUMN_PROJECT_WALKTHROUGH_ID + UserTable.TEXT_TYPE + ")";

    }
}
