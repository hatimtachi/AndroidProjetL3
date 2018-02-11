package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htmx.pro.Base_donnee_interne;
import com.example.htmx.pro.R;
import com.example.htmx.pro.emploi.AllinfoCompetence;
import com.example.htmx.pro.emploi.Emploi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by htmx on 14/12/16.
 */

public class Employeur_Accueil extends Activity {
    private DatabaseReference databaseReference;
    Use_BD_employeur employeurBd;
    RecyclerView recyV_foramtion,recyV_comptence;
    DrawerLayout drawerLayout;
    Context context=this;
    String all_job="";
    private ImageView img_profil;
    private StorageReference storageReference;
    String nom,prenom,formation,comptence;
    TextView tx_nom,tx_prenom;
    ArrayList List_formation=new ArrayList<>();
    ArrayList List_comptence=new ArrayList();
    ArrayList<String>list_cv=new ArrayList<>();
    int n_eme=0;
    Button btn_next, btn_fuck_out;
    SharedPreferences sharedPreferences;
    String domaine;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeur_accueil);

        sharedPreferences=context.getSharedPreferences(getString(R.string.prefrence_id),Context.MODE_PRIVATE);
        storageReference= FirebaseStorage.getInstance().getReference();
        employeurBd=new Use_BD_employeur(this);

        tx_nom=(TextView)findViewById(R.id.employeur_accueil_textview_nom);
        tx_prenom=(TextView)findViewById(R.id.employeur_accueil_textview_prenom);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        recyV_foramtion=(RecyclerView)findViewById(R.id.employeur_accueil_recyclerview_formation);
        recyV_comptence=(RecyclerView)findViewById(R.id.employeur_accueil_recyclerview_competence);
        img_profil=(ImageView)findViewById(R.id.profile_image);
        btn_next=(Button)findViewById(R.id.employeur_accueil_btn_ajouter);
        btn_fuck_out=(Button)findViewById(R.id.employeur_accueil_btn_delete);


        recyV_foramtion.setLayoutManager(new LinearLayoutManager(context));
        recyV_comptence.setLayoutManager(new LinearLayoutManager(context));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_l_employeur);
        ActionBarDrawerToggle actionBarDrawerLayout = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerLayout);
        actionBarDrawerLayout.syncState();

        String re1,re2="";
        employeurBd.open();
        Cursor res = employeurBd.getAll();
        System.out.println("*************************************"+res.getCount());
        for (int i=0;i<res.getCount();i++) {
            res.moveToNext();
            re1 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_DOMAINE));
            re2 = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_ID_CV));
            System.out.println("sais////////::" + re1 + "\n dsq////////:: " + re2);
        }
        employeurBd.close();

        domaine=sharedPreferences.getString(getString(R.string.prefrence_quoi),null);
        chercher_all_cv(domaine);

    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(context,Employeur_accueil_main.class);
        startActivity(intent);
    }

    public void chercher_all_cv(final String id_job){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("job").child(id_job).exists()){
                    all_job=dataSnapshot.child("job").child(id_job).getValue().toString().trim();
                    System.out.println(all_job);
                    list_cv=get_list_cv(all_job);
                    get_user_form_id_email(list_cv.get(n_eme));
                    /**
                     * onClick btn
                     */
                    btn_fuck_out.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            n_eme++;
                            if (n_eme>=list_cv.size()){
                                Intent intent=new Intent(context,Employeur_accueil_main.class);
                                startActivity(intent);
                            }
                            else {
                                get_user_form_id_email(list_cv.get(n_eme));
                            }
                        }
                    });/**
                    *TODO faire le reste la page favori et les truc qui va avec et thanks
                    */
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String unique=list_cv.get(n_eme)+domaine;
                            int has_unique=unique.hashCode();
                            System.out.println("**********///////////////////>>>>>>>>>>>>><"+list_cv.get(n_eme));
                            employeurBd.open();
                            employeurBd.Emp_bd_create(domaine,list_cv.get(n_eme),has_unique);
                            employeurBd.close();
                            n_eme++;
                            if (n_eme>=list_cv.size()){
                                Intent intent=new Intent(context,Employeur_accueil_main.class);
                                startActivity(intent);
                            }
                            else {
                                get_user_form_id_email(list_cv.get(n_eme));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(context,getString(R.string.domaine_vide),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context,Employeur_accueil_main.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void get_user_form_id_email(final String id_email){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id_email).exists()){
                    nom=dataSnapshot.child("users").child(id_email).child("nom").getValue().toString().trim();
                    prenom=dataSnapshot.child("users").child(id_email).child("prenom").getValue().toString().trim();
                    if(dataSnapshot.child("users").child(id_email).child("formation").exists()){
                        formation=dataSnapshot.child("users").child(id_email).child("formation").getValue().toString();
                        List_formation=recuperString_to_Array(formation);
                    }
                    if(dataSnapshot.child("users").child(id_email).child("competence").exists()){
                        comptence=dataSnapshot.child("users").child(id_email).child("competence").getValue().toString();
                        List_comptence=recuperString_to_Array(comptence);
                    }
                    List_comptence=degager_zero_du_null(List_comptence);
                    mettre_ContenuList_comptence(List_comptence);
                    List_formation=recupere_le_titre_Formation(List_formation);
                    mettre_ContenuList_formation(List_formation);
                }
                tx_prenom.setText(prenom);
                tx_nom.setText(nom);

                storageReference.child("photo").child(id_email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(uri).into(img_profil);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void mettre_ContenuList_comptence(ArrayList<String> list_comptence){
        InfoCompetence infoCompetence =new InfoCompetence(context,list_comptence);
        recyV_comptence.setAdapter(infoCompetence);
        recyV_comptence.setItemAnimator(new DefaultItemAnimator());
    }
    public void mettre_ContenuList_formation(ArrayList<String> list_formation){
        InfoFormation infoFormation =new InfoFormation(context,list_formation);
        recyV_foramtion.setAdapter(infoFormation);
        recyV_foramtion.setItemAnimator(new DefaultItemAnimator());
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

    /**
     *pour avoir juste les id_email
     */
    public ArrayList get_list_cv(String cv){
        ArrayList<String> l_cv=new ArrayList<>();
        String enreg_cv="";
        char restemp;
        for (int i=0;i<cv.length();i++){
            restemp=cv.charAt(i);
            enreg_cv+=restemp;
            System.out.println(enreg_cv);
            switch (cv.charAt(i)){
                case '{':
                    enreg_cv="";
                    break;
                case ',':
                    enreg_cv="";
                    break;
                case '=':
                    char chartemp;
                    String enr_cv="";
                    for (int j=0;j<enreg_cv.length();j++){
                        switch (enreg_cv.charAt(j)){
                            case '=':
                                break;
                            case ' ':
                                break;
                            default:
                                chartemp=enreg_cv.charAt(j);
                                enr_cv+=chartemp;
                                break;
                        }

                    }
                    l_cv.add(enr_cv);
                    enreg_cv="";
                    break;
            }

        }
        Iterator it=l_cv.iterator();
        while (it.hasNext()){
            System.out.println("------------>>>>>>>>"+it.next());

        }
        return l_cv;
    }
    public ArrayList recupere_le_titre_Formation(ArrayList list){
        String tmp;
        String tab[]=new String[list.size()];
        int tab2[]=new int[list.size()];
        ArrayList<String> le_bonne=new ArrayList<>();
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
        /***************
         *  c'est fait *
         ***************/
        return le_bonne;
    }

}
