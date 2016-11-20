package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "140268N";

    //table names
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_TRANSACTION = "transaction";

    //Account table columns
    public static final String KEY_ACCOUNT_NO = "accountNo";
    public static final String KEY_BANK_NAME = "bankName";
    public static final String KEY_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String KEY_BALANCE = "balance";

    //Transaction table columns
    public static final String KEY_DATE = "date";
    public static final String KEY_AMOUNT = "amount";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNT + "("
            + KEY_ACCOUNT_NO + " TEXT PRIMARY KEY," + KEY_BANK_NAME + " TEXT,"
            + KEY_BALANCE + " DOUBLE,"
            + KEY_ACCOUNT_HOLDER_NAME + " TEXT," + ")";

    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
            + KEY_DATE+ " DATE," + KEY_AMOUNT + " DOUBLE,"
            + KEY_ACCOUNT_NO +","
            +"FOREIGN KEY (" + KEY_ACCOUNT_NO + ") REFERENCES " +
            TABLE_ACCOUNT + "(" + KEY_ACCOUNT_NO + "))";

    private static final String DELETE_ACCOUNT_TABLE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;

    private static final String DELETE_TRANSACTION_TABLE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_TRANSACTION;

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DELETE_ACCOUNT_TABLE_ENTRIES);
        db.execSQL(DELETE_TRANSACTION_TABLE_ENTRIES);

        // Create tables again
        onCreate(db);
    }
}
