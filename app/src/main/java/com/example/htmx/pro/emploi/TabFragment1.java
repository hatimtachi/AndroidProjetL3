package com.example.htmx.pro.emploi;

/**
 * Created by htmx on 11/11/16.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.htmx.pro.Base_donnee_interne;
import com.example.htmx.pro.R;
import com.example.htmx.pro.Use_BD_interne;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;


public class TabFragment1 extends Fragment  implements View.OnClickListener{
    Intent intent;
    private Use_BD_interne useBdInterne;
    DatabaseReference databaseReference;
    private View T_Nom_Prenom,T_email,T_titre_resume,T_dateN,T_tele;
    private String nom,nom_prenom,email,prenom,id_email,connect;
    private Button btn_pen1,btn_pen2,btn_pen3,btn_pen4;
    private ListView listView_formation;
    Context context;
    private RecyclerView recyclerView;
    private ImageView img_profil;
    private List List_formation=new ArrayList();
    private ArrayList List_competence=new ArrayList();
    private String formation="",titre_profil="",resume="",competence="",dateString;
    private StorageReference storageReference;
    String dateAn="",numTele="";
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.e_fragment_accueil_1,container,false);
        context=v.getContext();
        /**
         * declaration
         */
        storageReference= FirebaseStorage.getInstance().getReference();
        img_profil=(ImageView)v.findViewById(R.id.profile_image);
        useBdInterne = new Use_BD_interne(context);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        btn_pen1=(Button) v.findViewById(R.id.btn_pen_main_emploi_1);
        btn_pen1.setOnClickListener(this);
        btn_pen2=(Button) v.findViewById(R.id.btn_pen_main_emploi_2);
        btn_pen2.setOnClickListener(this);
        btn_pen3=(Button) v.findViewById(R.id.btn_pen_main_emploi_3);
        btn_pen3.setOnClickListener(this);
        btn_pen4=(Button) v.findViewById(R.id.btn_pen_main_emploi_4);
        btn_pen4.setOnClickListener(this);
        listView_formation=(ListView)v.findViewById(R.id.list_View_formation);
        T_Nom_Prenom=v.findViewById(R.id.textView111);
        T_email=v.findViewById(R.id.TextV_main_emploi_email);
        T_dateN=v.findViewById(R.id.TextV_main_emploi_anniv);
        T_tele=v.findViewById(R.id.TextV_main_emploi_tele);
        T_titre_resume=v.findViewById(R.id.TextView_fragment_emploi_main_titre_resume);
        recyclerView=(RecyclerView)v.findViewById(R.id.fragment_accueil_listView_competence);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Button btn_contact=(Button)v.findViewById(R.id.btn_pen_main_emploi_4);



        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View views = inflater.inflate(R.layout.tab1_contact_emploi,container,false);
                final EditText E_tele=(EditText)views.findViewById(R.id.tab1_contact_emploi_num_tele);
                final EditText E_dateN=(EditText)views.findViewById(R.id.tab1_contact_emploi_date);
                if (!dateAn.equals(""))
                    E_dateN.setText(dateAn);
                if (!numTele.equals(""))
                    E_tele.setText(numTele);
                AlertDialog.Builder alertDB = new AlertDialog.Builder(context);
                alertDB.setView(views);
                final Calendar c=Calendar.getInstance();
                final int annee=c.get(Calendar.YEAR);
                final int mois=c.get(Calendar.MONTH);
                final int jours=c.get(Calendar.DAY_OF_MONTH);
                final EditText date = (EditText) views.findViewById(R.id.tab1_contact_emploi_date);
                date.setFocusable(false);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog=new DatePickerDialog(context,R.style.AppTheme,new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month+=1;
                                dateString=dayOfMonth+"/"+month+"/"+year;
                                date.setText(dateString);
                            }
                        },annee,mois+1,jours);
                        datePickerDialog.show();
                    }
                });
                alertDB.setCancelable(false).setPositiveButton(getResources().getString(R.string.enregistre).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateAn=E_dateN.getText().toString().trim();
                        numTele=E_tele.getText().toString().trim();
                        updata_tele_dateN(id_email,dateAn,numTele);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.annuler).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog =alertDB.create();

                dialog.show();
            }
        });

        getUserFromData();
        getAllInfoUser(id_email);

        storageReference.child("photo").child(id_email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(img_profil);
            }
        });
        return v;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pen_main_emploi_3:
                intent = new Intent(getActivity(), E_Competence.class);
                startActivity(intent);
                break;
            case R.id.btn_pen_main_emploi_2:
            case R.id.btn_pen_main_emploi_1:
                intent = new Intent(getActivity(), Formulaire.class);
                startActivity(intent);
                break;
        }
    }


    public void updata_tele_dateN(final String id_email,final String dateN,final String tele){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(id_email).exists()){
                    databaseReference.child("users").child(id_email).child("dateN").setValue(dateN);
                    databaseReference.child("users").child(id_email).child("tele").setValue(tele);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllInfoUser(final String id_email){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nom=dataSnapshot.child("users").child(id_email).child("nom").getValue().toString();
                prenom=dataSnapshot.child("users").child(id_email).child("prenom").getValue().toString();
                email=dataSnapshot.child("users").child(id_email).child("email").getValue().toString();
                if (dataSnapshot.child("users").child(id_email).child("formation").exists()){
                    formation=dataSnapshot.child("users").child(id_email).child("formation").getValue().toString();
                    List_formation=recuperString_to_Array(formation);
                }
                if (dataSnapshot.child("users").child(id_email).child("titre_profil").exists()){
                    titre_profil=dataSnapshot.child("users").child(id_email).child("titre_profil").getValue().toString();
                }
                if(dataSnapshot.child("users").child(id_email).child("resume").exists()){
                    resume=dataSnapshot.child("users").child(id_email).child("resume").getValue().toString();
                }
                if(dataSnapshot.child("users").child(id_email).child("competence").exists()){
                    competence=dataSnapshot.child("users").child(id_email).child("competence").getValue().toString();
                    List_competence=recuperString_to_Array(competence);
                }
                if(dataSnapshot.child("users").child(id_email).child("dateN").exists()){
                    dateAn=dataSnapshot.child("users").child(id_email).child("dateN").getValue().toString();
                    ((TextView)T_dateN).setText(dateAn);
                }
                if(dataSnapshot.child("users").child(id_email).child("tele").exists()){
                    numTele=dataSnapshot.child("users").child(id_email).child("tele").getValue().toString();
                    ((TextView)T_tele).setText(numTele);
                }

                List_formation=recupere_le_titre_Formation(List_formation);
                System.out.println(nom+"\n"+prenom+"\n"+email);
                nom_prenom =nom+" "+prenom;
                String formation_text=recuper_last_String(List_formation);
                ((TextView)T_Nom_Prenom).setText("\n"+nom_prenom);
                ((TextView)T_email).setText(email);
                ((TextView)T_titre_resume).setText(titre_profil+" "+getResources().getString(R.string.en)+" "+formation_text+"\n\t\t"+resume);
                mettre_ContenuList_ListView(List_formation);
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
    public void mettre_ContenuList_ListView_Competence(ArrayList<String> list_competence){
        AllinfoCompetence allinfoCompetence =new AllinfoCompetence(context,list_competence);
        recyclerView.setAdapter(allinfoCompetence);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void mettre_ContenuList_ListView(List list_formation){
        ArrayAdapter<String>adapter=new ArrayAdapter<>(context,R.layout.tabfragment1_formation_textlist,R.id.TextV_tabfragment1_formation_textlist,list_formation);
        listView_formation.setAdapter(adapter);
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
     * entrer c'est un string sortie sais une liste de formation car je l'ai fait pour ca
     * @param lestring
     * @return
     */
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
    public List recupere_le_titre_Formation(List list){
        String tmp;
        String tab[]=new String[list.size()];
        int tab2[]=new int[list.size()];
        List<String> le_bonne=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            tmp=list.get(i).toString();
            tab[i]=tmp;
            System.out.println(tab[i]);
        }
        /**
         * pour mettre les date dans un tableau et les changer en int pour les triee
         */
        for(int i=0;i<list.size();i++){
            String re=tab[i].charAt(tab[i].length()-6)+""+tab[i].charAt(tab[i].length()-5)+""+tab[i].charAt(tab[i].length()-4)+""+tab[i].charAt(tab[i].length()-3);
            int letruc =Integer.parseInt(re);
            tab2[i]=letruc;
        }
        Arrays.sort(tab2);
        for(int i=0;i<list.size();i++){
            System.out.println(tab2[i]);
        }
        /**
         * trier le vrai tableau qui contienu tout
         */
        for (int i=0;i<list.size();i++){
            for (int j=0;j<list.size();j++) {
                String re = tab[j].charAt(tab[j].length() - 6) + "" + tab[j].charAt(tab[j].length() - 5) + "" + tab[j].charAt(tab[j].length() - 4) + "" + tab[j].charAt(tab[j].length() - 3);
                int letruc = Integer.parseInt(re);
                if (tab2[i] == letruc) {
                    le_bonne.add(tab[j]);
                    System.out.println(le_bonne.get(i));
                }
            }
        }
        /**
         * c'est fait
         */
        return le_bonne;
    }

    public String recuper_last_String(List list){
        String res="";
        if(list.size()!=0)
            res=list.get(list.size()-1).toString();
        return res;
    }
}
