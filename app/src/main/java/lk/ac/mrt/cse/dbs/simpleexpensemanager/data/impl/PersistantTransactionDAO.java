package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantTransactionDAO  implements TransactionDAO {
    private SQLiteDatabase sql_db;
    private SQLiteOpenHelper dbHelper;

    public PersistantTransactionDAO(Context context) {
        this.dbHelper = new DBHandler(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        sql_db = dbHelper.getWritableDatabase();
        String query = String.format(
                "INSERT INTO "+DBHandler.TABLE_TRANSACTION)+
                " VALUES ("+date+","
                +accountNo+","
                +expenseType+","
                +amount+");";
        sql_db.execSQL(query);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> results = new ArrayList<>();
        sql_db = dbHelper.getReadableDatabase();
        String query = String.format(
                "SELECT * FROM "+DBHandler.TABLE_TRANSACTION);
        final Cursor cursor = sql_db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Date date =  new Date(cursor.getLong(0));
                String accountNo=cursor.getString(1);
                ExpenseType expenseType= Enum.valueOf(ExpenseType.class, cursor.getString(2));
                double amount=cursor.getDouble(3);

                Transaction transaction=new Transaction(date,accountNo,expenseType,amount);
                results.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = getAllTransactionLogs().size();
        if (size <= limit) {
            return getAllTransactionLogs();
        }
        // return the last <code>limit</code> number of transaction logs
        return getAllTransactionLogs().subList(size - limit, size);
    }
}
