package com.example.lyy.androidexperiment_notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper{

    private Context context;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =  "create table category(" +
                "id integer primary key autoincrement," +
                "name text)";

        db.execSQL(sql);

        sql = "create table note(" +
                "id integer primary key autoincrement," +
                "title text ," +
                "content text," +
                "cid integer," +
                "time text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
