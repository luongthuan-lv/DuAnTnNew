package com.example.duantn.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.duantn.morder.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private MySqliteOpenHelper mySqliteOpenHelper;

    public AccountDAO(Context context) {
        this.mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    private String AC_TABLE = "account";
    private String PK = "pk";
    private String ID = "id";
    private String URL_AVT = "url_avt";
    private String NAME = "name";

    public List<Account> getAll() {


        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + AC_TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    Account account = new Account();

                    account.setPk(cursor.getString(cursor.getColumnIndex(PK)));
                    account.setId(cursor.getString(cursor.getColumnIndex(ID)));
                    account.setUrl_avt(cursor.getString(cursor.getColumnIndex(URL_AVT)));
                    account.setName(cursor.getString(cursor.getColumnIndex(NAME)));

                    accountList.add(account);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return accountList;
    }


    public void update(Account account) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PK, account.getPk());
        contentValues.put(ID, account.getId());
        contentValues.put(URL_AVT, account.getUrl_avt());
        contentValues.put(NAME, account.getName());

        sqLiteDatabase.update(AC_TABLE, contentValues, PK + "=?", new String[]{account.getPk()});
        sqLiteDatabase.close();
    }
}
