package com.example.htmx.pro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by htmx on 10/11/16.
 */

public class Base_donnee_interne extends SQLiteOpenHelper {
    public static final String TABLE_BASE="users";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_SAIS_QUI_E="saisQE";
    public static final String COLUMN_CONNECT_OR_NOT_E="ConnectORNonE";
    private static final String DATABASE_NAME="base.db";
    private static final int DATABASE_VERSION=3;

    private static final String DTABASE_CREATE="create table "
            +TABLE_BASE+" ( "+COLUMN_ID+" integer primary key asc,"
            +COLUMN_SAIS_QUI_E+" text unique not null,"
            +COLUMN_CONNECT_OR_NOT_E+" text not null);";

    public Base_donnee_interne (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DTABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Base_donnee_interne.class.getName(),
                "Upgrading database from version"+oldVersion+"to"+
                        newVersion+",which will destroy all old data");
        db.execSQL("drop table if exists "+TABLE_BASE);
        onCreate(db);
    }
}
