package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.htmx.pro.R;


/**
 * Created by htmx on 28/12/16.
 */

public class Emp_item_selected extends Activity {


    Use_BD_employeur employeurBd;
    private Button btn_call;
    private Button btn_send;
    private TextView tx_email;
    private Button btn_remove;
    private EditText ed_objet;
    private EditText ed_sujet;
    String nom,prenom,email,num,domaine;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_item_selected);

        employeurBd=new Use_BD_employeur(this);
        btn_call=(Button)findViewById(R.id.emp_item_selected_btn_call);
        btn_send=(Button)findViewById(R.id.emp_item_selected_btn_send);
        btn_remove=(Button)findViewById(R.id.emp_item_selected_btn_delete_candidat);
        tx_email=(TextView)findViewById(R.id.emp_item_selected_tx_email);
        ed_objet=(EditText)findViewById(R.id.emp_item_selected_edt_sujet);
        ed_sujet=(EditText)findViewById(R.id.emp_item_selected_edtt_body);

        /*la connexion avant l'autre fichier le getter de donnee*/
        SharedPreferences preferences=context.getSharedPreferences(getResources().getString(R.string.prefrence_id2),Context.MODE_PRIVATE);
        nom=preferences.getString(getString(R.string.prefrence_nom),null);
        prenom=preferences.getString(getString(R.string.prefrence_prenom),null);
        email=preferences.getString(getString(R.string.prefrence_email),null);
        num=preferences.getString(getString(R.string.prefrence_num),null);
        domaine=preferences.getString(getString(R.string.prefrence_domaine),null);
        tx_email.setText(email);
        System.out.println(nom+prenom+email+num+domaine);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=3;
                String sujet,object;
                sujet=ed_sujet.getText().toString().trim();
                object=ed_objet.getText().toString().trim();
                if (sujet.equals("")){
                    ed_sujet.setError(getResources().getString(R.string.champ_vide));
                    count--;
                }
                if (object.equals("")){
                    ed_objet.setError(getResources().getString(R.string.champ_vide));
                }
                if (count==3){
                    Intent emailintent = new Intent(Intent.ACTION_SEND);
                    emailintent.setData(Uri.parse("mailto:"));
                    emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailintent.putExtra(Intent.EXTRA_SUBJECT, sujet);
                    emailintent.putExtra(Intent.EXTRA_TEXT, object);
                    emailintent.setType("message/rfc822");
                    startActivity(Intent.createChooser(emailintent,"Send Email"));
                }
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(num);
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+num));
                context.startActivity(callIntent);

            }
        });
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeurBd.open();
                String hsemail,hsresu;
                int has_email=email.hashCode();
                hsemail=Integer.toString(has_email).trim();
                hsresu=hsemail+domaine;
                int has_unique=hsresu.hashCode();
                Cursor res = employeurBd.getAll();
                String re1,re2;

                System.out.println("*************************************"+res.getCount());
                for (int i=0;i<res.getCount();i++) {
                    res.moveToNext();
                    re1 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_DOMAINE));
                    re2 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_HASCODE_DOM_ID));
                    System.out.println("sais////////::" + re1 + "\n dsq////////:: " + re2);
                }

                System.out.println(employeurBd.delete_cv(has_unique));
                System.out.println(has_unique);
                res = employeurBd.getAll();
                System.out.println("*************************************"+res.getCount());
                for (int i=0;i<res.getCount();i++) {
                    res.moveToNext();
                    re1 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_DOMAINE));
                    re2 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_HASCODE_DOM_ID));
                    System.out.println("sais////////::" + re1 + "\n dsq////////:: " + re2);
                }
                employeurBd.close();
                Intent intent=new Intent(context,Emp_favori.class);
                startActivity(intent);
            }
        });
    }
/**TODO le truc de connection comment le emploi*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(context,Emp_favori.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String sujet,object;
        String hsemail,hsresu;
        int has_email=email.hashCode();
        hsemail=Integer.toString(has_email).trim();
        hsresu=hsemail+domaine;
        int has_unique=hsresu.hashCode();
        sujet=ed_sujet.getText().toString().trim();
        object=ed_objet.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getResources().getString(R.string.prefrence_id3),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_object),object);
        editor.putString(getString(R.string.prefrence_sujet),sujet);
        editor.putInt(getString(R.string.prefrence_hcode),has_unique);
        editor.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String sujet,object;
        String hsemail,hsresu;
        int has_email=email.hashCode();
        hsemail=Integer.toString(has_email).trim();
        hsresu=hsemail+domaine;
        int has_unique=hsresu.hashCode();
        sujet=ed_sujet.getText().toString().trim();
        object=ed_objet.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getResources().getString(R.string.prefrence_id3),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_object),object);
        editor.putString(getString(R.string.prefrence_sujet),sujet);
        editor.putInt(getString(R.string.prefrence_hcode),has_unique);
        editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        String sujet,object;
        String hsemail,hsresu;
        int has_email=email.hashCode();
        hsemail=Integer.toString(has_email).trim();
        hsresu=hsemail+domaine;
        int has_unique=hsresu.hashCode();
        int hcode;
        SharedPreferences preferences=context.getSharedPreferences(getResources().getString(R.string.prefrence_id3),Context.MODE_PRIVATE);
        object=preferences.getString(getString(R.string.prefrence_object),null);
        sujet =preferences.getString(getString(R.string.prefrence_sujet),null);
        hcode =preferences.getInt(getString(R.string.prefrence_hcode),0);
        System.out.println(hcode);
        System.out.println(has_unique);
        if (hcode==has_unique){
            ed_objet.setText(object);
            ed_sujet.setText(sujet);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String sujet,object;
        String hsemail,hsresu;
        int has_email=email.hashCode();
        hsemail=Integer.toString(has_email).trim();
        hsresu=hsemail+domaine;
        int has_unique=hsresu.hashCode();
        int hcode;
        SharedPreferences preferences=context.getSharedPreferences(getResources().getString(R.string.prefrence_id3),Context.MODE_PRIVATE);
        object=preferences.getString(getString(R.string.prefrence_object),null);
        sujet =preferences.getString(getString(R.string.prefrence_sujet),null);
        hcode =preferences.getInt(getString(R.string.prefrence_hcode),0);
        System.out.println(hcode);
        System.out.println(has_unique);
        if (hcode==has_unique){
            ed_objet.setText(object);
            ed_sujet.setText(sujet);
        }
    }
}
