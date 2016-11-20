package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistantDemoExpenseManager extends ExpenseManager {

    private transient Context context;

    public PersistantDemoExpenseManager(Context context) {
        this.context = context;
        setup();
    }

//    @Override
//    public void setup() {
//        /*** Begin generating dummy data for In-Memory implementation ***/
//
//        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(context);
//        setTransactionsDAO(persistantTransactionDAO);
//
//        AccountDAO persistantAccountDAO = new PersistantAccountDAO(context);
//        setAccountsDAO(persistantAccountDAO);
//
//        // dummy data
//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);
//        /*** End ***/
//    }
    @Override
    public void setup() {
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("140268N", context.MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "accountNo VARCHAR PRIMARY KEY," +
                "bankName VARCHAR," +
                "accountHolderName VARCHAR," +
                "balance DOUBLE" +
                " );");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "date DATE," +
                "accountNo VARCHAR," +
                "expenseType VARCHAR," +
                "amount DOUBLE," +
                "FOREIGN KEY (accountNo) REFERENCES Account(accountNo)" +
                ");");

        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(context);
        setTransactionsDAO(persistantTransactionDAO);

        AccountDAO persistantAccountDAO = new PersistantAccountDAO(context);
        setAccountsDAO(persistantAccountDAO);
        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);
        /*** End ***/
    }

}
