package com.example.htmx.pro.employeur;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by htmx on 25/12/16.
 */
public class Employeur_bd extends SQLiteOpenHelper {
    public static final String TABLE_BASE="employeur";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_DOMAINE="domain";
    public static final String COLUMN_ID_CV="idcv";
    public static final String COLUMN_HASCODE_DOM_ID="hshco";
    private static final String DATABASE_NAME="emp.db";
    private static final int DATABASE_VERSION=5;

    private static final String DATABASE_CREATE="create table "
            +TABLE_BASE+" ( "+COLUMN_ID+" integer primary key asc,"
            +COLUMN_DOMAINE+" text not null,"
            +COLUMN_ID_CV+" text not null,"
            +COLUMN_HASCODE_DOM_ID+" int unique not null );";

    public Employeur_bd (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_BASE);
        onCreate(db);
    }
}
