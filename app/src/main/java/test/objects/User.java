package test.objects;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

import java.util.Date;
import java.util.UUID;

import test.persistence.Constants;
import test.persistence.Storage;

/**
 * Created by willpassidomo on 1/15/15.
 */
public class User {

    private String id;
    private String userName;
    private String email;
    private String password;
    private Date dateCreated;
    private String imageId;

    public User(String userName, String password) {
        this.setId(UUID.randomUUID().toString());
        this.setUserName(userName);
        this.password = password;
        this.setDateCreated(new Date());
    }

    private User(String id, Date dateCreated) {
        this.setId(id);
        this.dateCreated = dateCreated;
    }

    public static User getDBUser(String id, String dateCreated) {return new User(id, new Date(dateCreated));};

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

    public void resetPassword() {password = "password";}

    public boolean changePassword(String oldPassword, String newPassword) {
        if (checkPassword(oldPassword)) {
            password = newPassword;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPassword(String password) {
        return this.password == password ? true : false;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    private void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getImage() {
        return Storage.getPictureByOwnerId(null, getImageId());
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public static abstract class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "userTable";
        public static final String COLUMN_ID = "userId";
        public static final String COLUMN_NAME = "userName";
        public static final String COLUMN_EMAIL = "userEmail";
        public static final String COLUMN_IMAGE_ID = "imageId";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_DATECREATED = "dateCreated";

        public static final String CREATE_TABLE =
                Constants.createTableString(
                        TABLE_NAME,
                        COLUMN_ID + Constants.TEXT_TYPE,
                        COLUMN_NAME + Constants.TEXT_TYPE,
                        COLUMN_EMAIL + Constants.TEXT_TYPE,
                        COLUMN_IMAGE_ID + Constants.TEXT_TYPE,
                        COLUMN_PASSWORD + Constants.TEXT_TYPE,
                        COLUMN_DATECREATED + Constants.TEXT_TYPE
                );
        }

    public static abstract class UserLoggedInTable implements BaseColumns {
        public static final String TABLE_NAME = "loggedInUser";

        public static final String CREATE_TABLE =
                Constants.createTableString(
                        TABLE_NAME,
                        UserTable.COLUMN_ID + Constants.TEXT_TYPE);
    }
}
