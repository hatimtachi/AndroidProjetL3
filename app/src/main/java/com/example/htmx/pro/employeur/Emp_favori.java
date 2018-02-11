package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.htmx.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htmx on 22/12/16.
 */
public class Emp_favori extends Activity {
    private DatabaseReference databaseReference;

    Use_BD_employeur employeurBd;
    public List<Favori> favoriList=new ArrayList<>();
    public RecyclerView recyclerView;
    Context context=this;
    static Context context1;
    String[] Nom;
    String[] Domaine;
    String[] Prenom;
    String[] Domaine_avant;
    String[] id_email;
    int taille=0;
    public static Context getAppContext(){
        return context1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp_favori);

        context1=this.getApplicationContext();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recyclerView=(RecyclerView)findViewById(R.id.emp_favori_recycleV_info);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        employeurBd=new Use_BD_employeur(this);


        getData_de_db();
        Emp_favori_info favori_info=new Emp_favori_info(context,favoriList);
        recyclerView.setAdapter(favori_info);

    }

    public void getData_de_db(){
        employeurBd.open();
        Cursor res = employeurBd.getAll();
        Domaine_avant=new String[res.getCount()];
        id_email=new String[res.getCount()];
        taille=res.getCount();
        Nom=new String[taille];
        Prenom=new String[taille];
        Domaine=new String[taille];
        for (int i=0;i<res.getCount();i++) {
            res.moveToNext();
            Domaine_avant[i]=res.getString(res.getColumnIndex(Employeur_bd.COLUMN_DOMAINE));
            id_email[i] = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_ID_CV));
            get_user_form_id_email(id_email[i],Domaine_avant[i],i);
            System.out.println("i.......///"+i+"sais////////::" + Domaine_avant[i] + "\n dsq////////:: " +id_email[i]);
            System.out.println("nom "+Nom[i]+" Prenom "+Prenom[i]+" Domaine "+Domaine[i]);

        }
        employeurBd.close();

    }

    public void get_user_form_id_email(final String id_email, final String domaine, final int position){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id_email).exists()){
                    Nom[position]=dataSnapshot.child("users").child(id_email).child("nom").getValue().toString().trim();
                    Prenom[position]=dataSnapshot.child("users").child(id_email).child("prenom").getValue().toString().trim();
                    Domaine[position]=domaine;
                    Favori c=new Favori(Nom[position],Prenom[position],Domaine[position]);
                    favoriList.add(c);
                    System.out.println("nom "+Nom[position]+" Prenom "+Prenom[position]+" Domaine "+Domaine[position]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

