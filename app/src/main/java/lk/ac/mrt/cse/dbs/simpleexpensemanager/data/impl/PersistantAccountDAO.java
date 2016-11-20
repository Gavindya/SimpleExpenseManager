package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistantAccountDAO implements AccountDAO {

    private SQLiteDatabase sql_db;
    private SQLiteOpenHelper dbHelper;

    public PersistantAccountDAO(Context context) {

        this.dbHelper = new DBHandler(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> results = new ArrayList<>();
        sql_db = dbHelper.getReadableDatabase();
        String query = String.format(
                "SELECT "+DBHandler.KEY_ACCOUNT_NO+
                "FROM "+DBHandler.TABLE_ACCOUNT);
        final Cursor cursor = sql_db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    @Override
    public List<Account> getAccountsList() {

        List<Account> results = new ArrayList<>();
        sql_db = dbHelper.getReadableDatabase();
        String query = String.format(
                "SELECT * FROM "+DBHandler.TABLE_ACCOUNT);
        final Cursor cursor = sql_db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(0);
                String bankName=cursor.getString(1);
                String accountHolderName=cursor.getString(2);
                double balance=Double.parseDouble(cursor.getString(3));

                Account account=new Account(accountNo,bankName,accountHolderName,balance);
                results.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account=null;
        String bankName;
        String accountHolderName;
        double balance;
        sql_db = dbHelper.getReadableDatabase();
        String query = String.format(
                "SELECT * FROM "+DBHandler.TABLE_ACCOUNT)+
                " WHERE accountNo="+accountNo;
        final Cursor cursor = sql_db.rawQuery(query, new String[]{accountNo});
        if (cursor.moveToFirst()) {
            bankName=cursor.getString(1);
            accountHolderName=cursor.getString(2);
            balance=Double.parseDouble(cursor.getString(3));
        }else{
            String error="Account "+accountNo+" was not found!";
            throw new InvalidAccountException(error);
        }
        account=new Account(accountNo,bankName,accountHolderName,balance);
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        sql_db = dbHelper.getWritableDatabase();
        String query = String.format(
                "INSERT INTO "+DBHandler.TABLE_ACCOUNT)+
                " VALUES ("+account.getAccountNo()+","
                +account.getBankName()+","
                +account.getAccountHolderName()+","
                +account.getBalance()+");";
        sql_db.execSQL(query);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        sql_db = dbHelper.getReadableDatabase();
        String query = String.format(
                "DELETE FROM "+DBHandler.TABLE_ACCOUNT+
                "WHERE accountNo =" +accountNo);
        sql_db.execSQL(query);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = getAccount(accountNo);
        Double newBalance=0.00;
        if (expenseType==ExpenseType.EXPENSE){
            newBalance=account.getBalance()-amount;
        }else if(expenseType==ExpenseType.INCOME){
            newBalance=account.getBalance()+amount;
        }
        sql_db = dbHelper.getWritableDatabase();
        String query = String.format(
                "UPDATE "+DBHandler.TABLE_ACCOUNT+
                " SET balance="+newBalance +
                " WHERE accountNo="+accountNo);
        sql_db.execSQL(query);
    }
}
