package com.example.phonebook.DbHelper;

public class Constants {

    public static final String DATABASE_NAME = "CONTACT_DB";
    public static final int DATABASE_VERSION = 1;
    public static final String CONTACT_TABLE = "CONTACT_TABLE";

    //column names
    public static final String COL_ID = "ID";
    public static final String COL_IMAGE = "IMAGE";
    public static final String COL_NAME = "NAME";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_NOTES = "NOTES";
    public static final String COL_ADDED_TIME = "ADDED_TIME";
    public static final String COL_UPDATED_TIME = "UPDATED_TIME";

    //table creating query
    public static final String CREATE_TABLE = " CREATE TABLE "+ CONTACT_TABLE +" ("
            +COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_IMAGE+ " TEXT, "
            +COL_NAME+ " TEXT, "
            +COL_PHONE+ " TEXT, "
            +COL_EMAIL+ " TEXT, "
            +COL_NOTES+ " TEXT, "
            +COL_ADDED_TIME+ " TEXT, "
            +COL_UPDATED_TIME+ " TEXT ); ";

}
