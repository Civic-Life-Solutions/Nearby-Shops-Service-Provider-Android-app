package org.nearbyshops.serviceprovider.ModelItemSubmission;



import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.Model.ItemImage;

import java.sql.Timestamp;

/**
 * Created by sumeet on 22/3/17.
 */
public class ItemImageSubmission {


    // Table Name
    public static final String TABLE_NAME = "ITEM_IMAGE_SUBMISSION";

    // image columns
    public static final String ITEM_ID = "ITEM_ID";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";
    public static final String GIDB_IMAGE_ID = "GIDB_IMAGE_ID";
    public static final String GIDB_SERVICE_URL = "GIDB_SERVICE_URL";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";

    public static final String CAPTION_TITLE = "CAPTION_TITLE";
    public static final String CAPTION = "CAPTION";
    public static final String IMAGE_COPYRIGHTS = "IMAGE_COPYRIGHTS";
    public static final String IMAGE_ORDER = "IMAGE_ORDER";


    // column names : Submission Details
    public static final String ITEM_IMAGE_SUBMISSION_ID = "ITEM_IMAGE_SUBMISSION_ID";
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String TIMESTAMP_SUBMITTED = "TIMESTAMP_SUBMITTED";
    public static final String TIMESTAMP_APPROVED = "TIMESTAMP_APPROVED";
    public static final String STATUS = "STATUS";
    public static final String USER_ID = "USER_ID";



    // create table statement
    public static final String createTableItemImagesPostgres = "CREATE TABLE IF NOT EXISTS "

            + ItemImageSubmission.TABLE_NAME + "("

            + " " + ItemImageSubmission.ITEM_IMAGE_SUBMISSION_ID + " SERIAL PRIMARY KEY,"

            + " " + ItemImageSubmission.IMAGE_FILENAME + " text,"
            + " " + ItemImageSubmission.GIDB_IMAGE_ID + " int,"
            + " " + ItemImageSubmission.GIDB_SERVICE_URL + " text,"

            + " " + ItemImageSubmission.CAPTION_TITLE + " text,"
            + " " + ItemImageSubmission.CAPTION + " text,"
            + " " + ItemImageSubmission.IMAGE_COPYRIGHTS + " text,"
            + " " + ItemImageSubmission.IMAGE_ORDER + " int,"

            + " " + ItemImageSubmission.ITEM_ID + " int not null,"

            + " " + ItemImageSubmission.IMAGE_ID + " int,"
            + " " + ItemImageSubmission.TIMESTAMP_SUBMITTED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + ItemImageSubmission.TIMESTAMP_APPROVED + "  timestamp with time zone ,"
            + " " + ItemImageSubmission.STATUS + " int,"
            + " " + ItemImageSubmission.USER_ID + " int,"

            + " FOREIGN KEY(" + ItemImageSubmission.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + ") ON DELETE SET NULL,"
            + " FOREIGN KEY(" + ItemImageSubmission.IMAGE_ID +") REFERENCES " + ItemImage.TABLE_NAME + "(" + ItemImage.IMAGE_ID + ") ON DELETE SET NULL"
            + ")";





    // instance variables

    private ItemImage itemImage;

    private Integer itemImageID;
    private int itemImageSubmissionID;
    private Timestamp timestampApproved;
    private Timestamp timestampSubmitted;
    private int status;
    private int userID;


    public ItemImage getItemImage() {
        return itemImage;
    }

    public void setItemImage(ItemImage itemImage) {
        this.itemImage = itemImage;
    }

    public Integer getItemImageID() {
        return itemImageID;
    }

    public void setItemImageID(Integer itemImageID) {
        this.itemImageID = itemImageID;
    }

    public int getItemImageSubmissionID() {
        return itemImageSubmissionID;
    }

    public void setItemImageSubmissionID(int itemImageSubmissionID) {
        this.itemImageSubmissionID = itemImageSubmissionID;
    }

    public Timestamp getTimestampApproved() {
        return timestampApproved;
    }

    public void setTimestampApproved(Timestamp timestampApproved) {
        this.timestampApproved = timestampApproved;
    }

    public Timestamp getTimestampSubmitted() {
        return timestampSubmitted;
    }

    public void setTimestampSubmitted(Timestamp timestampSubmitted) {
        this.timestampSubmitted = timestampSubmitted;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
