package com.example.htmx.pro.employeur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by htmx on 25/12/16.
 *

 */
public class Use_BD_employeur {
    private SQLiteDatabase sqLiteDatabase;
    private Employeur_bd employeurBd;

    public Use_BD_employeur(Context context){
        employeurBd=new Employeur_bd(context);
    }

    public void open() throws SQLException{
        sqLiteDatabase=employeurBd.getWritableDatabase();
    }
    public void close(){
        employeurBd.close();
    }
    public boolean Emp_bd_create(String domaine,String sais_qui,int hascode){
        ContentValues values=new ContentValues();
        values.put(Employeur_bd.COLUMN_DOMAINE,domaine);
        values.put(Employeur_bd.COLUMN_ID_CV,sais_qui);
        values.put(Employeur_bd.COLUMN_HASCODE_DOM_ID,hascode);
        sqLiteDatabase.insert(Employeur_bd.TABLE_BASE,null,values);
        return true;
    }
    public Cursor getAll(){
        sqLiteDatabase=employeurBd.getReadableDatabase();
        Cursor res=sqLiteDatabase.rawQuery("select * from "+Employeur_bd.TABLE_BASE,null);
        return res;
    }
    public Cursor get_Domaine_cv(String domaine){
        sqLiteDatabase=employeurBd.getReadableDatabase();
        Cursor res=sqLiteDatabase.rawQuery("select * from "+Employeur_bd.TABLE_BASE+" where "+Employeur_bd.COLUMN_DOMAINE+" = "+domaine,null);
        return res;
    }
    public boolean delete_cv(int hasC){
        sqLiteDatabase=employeurBd.getWritableDatabase();
        return sqLiteDatabase.delete(Employeur_bd.TABLE_BASE,Employeur_bd.COLUMN_HASCODE_DOM_ID+" = "+hasC,null)>0;
    }
}
