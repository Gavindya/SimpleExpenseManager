package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class DBHandler extends SQLiteOpenHelper implements Serializable{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "140268N";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Tables
        db.execSQL(
                "CREATE TABLE Account(" +
                "accountNo VARCHAR PRIMARY KEY, " +
                "bankName VARCHAR, " +
                "accountHolderName VARCHAR, " +
                "balance DOUBLE)"
        );

        db.execSQL(
                "CREATE TABLE TransactionLog(" +
                "date INT, " +
                "accountNo VARCHAR, " +
                "expenseType VARCHAR, " +
                "amount DOUBLE, " +
                "FOREIGN KEY (accountNo) REFERENCES Account(accountNo))"
        );
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS TransactionLog");

        // Create tables again
        onCreate(db);
    }
}
