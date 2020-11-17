package com.example.duantn.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duantn.morder.KeyLanguage;

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

    public List<KeyLanguage> getAll() {


        List<KeyLanguage> keyLanguageList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getReadableDatabase();

        String SQL = "SELECT * FROM " + NN_TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(SQL, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    KeyLanguage keyLanguage = new KeyLanguage();

                    keyLanguage.setPk(cursor.getString(cursor.getColumnIndex(PK)));
                    keyLanguage.setValue(cursor.getString(cursor.getColumnIndex(VALUE)));

                    keyLanguageList.add(keyLanguage);
                    cursor.moveToNext();

                }
                cursor.close();
            }
        }

        return keyLanguageList;
    }


    public void update(KeyLanguage keyLanguage) {
        SQLiteDatabase sqLiteDatabase = mySqliteOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PK, keyLanguage.getPk());
        contentValues.put(VALUE, keyLanguage.getValue());

        sqLiteDatabase.update(NN_TABLE, contentValues, PK + "=?", new String[]{keyLanguage.getPk()});
        sqLiteDatabase.close();
    }
}
