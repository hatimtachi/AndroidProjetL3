package com.example.htmx.pro.emploi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.AlertDialog;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.htmx.pro.Base_donnee_interne;
import com.example.htmx.pro.R;
import com.example.htmx.pro.Use_BD_interne;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by htmx on 28/11/16.
 */

public class E_Competence extends Activity {
    private RecyclerView recyclerView;
    private String id_email,connect,competence,E_input="";
    private DatabaseReference databaseReference;
    Context context=this;
    Button btn_back,btn_add;
    Intent intent;
    private ArrayList List_competence=new ArrayList();
    Use_BD_interne useBdInterne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_emploi_competence);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recyclerView=(RecyclerView)findViewById(R.id.List_main_emploi_dialog_competence_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        useBdInterne = new Use_BD_interne(context);
        btn_back=(Button)findViewById(R.id.Btn_main_emploi_dialog_competence_return);
        btn_add=(Button)findViewById(R.id.Btn_main_emploi_dialog_competence_ajouter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(context,Main_Emploi.class);
                startActivity(intent);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=LayoutInflater.from(context);
                View view=inflater.inflate(R.layout.e_competence_alert_dialog,null);
                AlertDialog.Builder alertBD=new AlertDialog.Builder(context);
                alertBD.setView(view);

                final EditText input=(EditText)view.findViewById(R.id.e_competence_alert_dialog_editText);
                input.setHint(getResources().getString(R.string.nouvelle_comp));

                alertBD.setCancelable(false).setPositiveButton(getResources().getString(R.string.enregistre).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        E_input=input.getText().toString().trim();
                        updata_competence(E_input);
                    }
                }).setNegativeButton(getResources().getString(R.string.annuler).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                android.app.AlertDialog dialog =alertBD.create();
                dialog.show();
            }
        });
        getUserFromData();
        getAllinfoCompetence(id_email);
    }

    public void updata_competence(final String data){
        databaseReference.addValueEventListener(new ValueEventListener() {
            final String newElement=data;
            final int hashnewElement=newElement.hashCode();
            final String FnewElement=Integer.toString(hashnewElement);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(id_email).exists()){
                    if (!dataSnapshot.child("users").child(id_email).child("competence").child(FnewElement).exists()) {
                        System.out.println("cest la comp::::  "+dataSnapshot.child("users").child(id_email).child("competence").child(FnewElement).exists());
                        databaseReference.child("users").child(id_email).child("competence").child(FnewElement).setValue("\t" + data + "\n");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserFromData(){
        useBdInterne.open();
        Cursor res = useBdInterne.getAll();
        res.moveToFirst();
        id_email = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_SAIS_QUI_E));
        connect = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_CONNECT_OR_NOT_E));
        System.out.println("sais::" + id_email + "\n dsq:: " + connect);
        useBdInterne.close();
    }
/**
 pour le bug il faut faire une reanilisation de la connexion car on peut pas sauvgarder et suprimer on meme temps car il ecrit sur le serveur sont arret
 **/
    public void getAllinfoCompetence(final String id_email){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(id_email).child("competence").exists()){
                    competence=dataSnapshot.child("users").child(id_email).child("competence").getValue().toString();
                    List_competence=recuperString_to_Array(competence);
                }
                List_competence=degager_zero_du_null(List_competence);
                mettre_ContenuList_ListView_Competence(List_competence);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public ArrayList degager_zero_du_null(ArrayList arrayList){
        ArrayList<String> list =new ArrayList<>();
        Iterator it=arrayList.iterator();
        Object n_it;
        while(it.hasNext()){
            n_it=it.next();
            if (!n_it.toString().trim().equals("0")){
                list.add(n_it.toString());
            }
        }
        return list;
    }
    public ArrayList recuperString_to_Array(String lestring){
        int i;
        char restemp;
        String sretemp="";
        Iterator it;
        ArrayList<String>ListTextString=new ArrayList<String>();
        for(i=0;i<lestring.length();i++){
            restemp=lestring.charAt(i);
            sretemp+=restemp;
            switch (lestring.charAt(i)) {
                case '\n':
                    ListTextString.add(sretemp);
                    sretemp="";
                    break;
                case '\t':
                    sretemp="";
                    break;
            }
        }
        it=ListTextString.iterator();
        while (it.hasNext()){
            System.out.println(it.next().toString());

        }
        return ListTextString;
    }

    public void mettre_ContenuList_ListView_Competence(ArrayList list_competence){
        AllinfoCompetence_Supression allinfoCompetence =new AllinfoCompetence_Supression(context,list_competence);
        recyclerView.setAdapter(allinfoCompetence);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    interface ClickListener{
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


}
