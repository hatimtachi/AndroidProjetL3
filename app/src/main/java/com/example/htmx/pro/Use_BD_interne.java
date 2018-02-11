package com.example.htmx.pro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by htmx on 10/11/16.
 */

public class Use_BD_interne {
    private SQLiteDatabase database;
    private Base_donnee_interne base_donnee;

    public Use_BD_interne(Context context){
        base_donnee=new Base_donnee_interne(context);
    }
    public void open() throws SQLException{
        database=base_donnee.getWritableDatabase();
    }
    public void close(){
        base_donnee.close();
    }

    public boolean U_create(String sais_qui,String connect_or_not){
        ContentValues values=new ContentValues();
        values.put(Base_donnee_interne.COLUMN_SAIS_QUI_E,sais_qui);
        values.put(Base_donnee_interne.COLUMN_CONNECT_OR_NOT_E,connect_or_not);
        database.insert(Base_donnee_interne.TABLE_BASE,null,values);
        return true;
    }

    public Cursor getAll(){
        database=base_donnee.getReadableDatabase();
        Cursor res=database.rawQuery("select * from "+Base_donnee_interne.TABLE_BASE,null);
        return res;
    }
    public Boolean getAllBoolean(){
        database=base_donnee.getReadableDatabase();
        Cursor res=database.rawQuery("select * from "+Base_donnee_interne.TABLE_BASE,null);
        int tmp=res.getCount();
        if (tmp==0){
            return true;
        }
    return false;
    }
    public void deleteContenuAll(){
        database.delete(Base_donnee_interne.TABLE_BASE,null,null);
    }
}
