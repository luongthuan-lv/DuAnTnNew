package com.example.duantn.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duantn.morder.KeyLangguage;

import java.util.ArrayList;
import java.util.List;

public class LanguageDAO {

    private MySqliteOpenHelper mySqliteOpenHelper;

    public LanguageDAO(Context context) {
        this.mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    private String NN_TABLE = "language";
    private String PK = "pk";
    private String VALUE = "value";

    public List<KeyLangguage> getAll() {


        List<KeyLangguage> keyLangguageList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + NN_TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    KeyLangguage keyLangguage = new KeyLangguage();

                    keyLangguage.setPk(cursor.getString(cursor.getColumnIndex(PK)));
                    keyLangguage.setValue(cursor.getString(cursor.getColumnIndex(VALUE)));

                    keyLangguageList.add(keyLangguage);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return keyLangguageList;
    }


    public void update(KeyLangguage keyLangguage) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PK, keyLangguage.getPk());
        contentValues.put(VALUE, keyLangguage.getValue());

        sqLiteDatabase.update(NN_TABLE, contentValues, PK + "=?", new String[]{keyLangguage.getPk()});
        sqLiteDatabase.close();
    }
}
