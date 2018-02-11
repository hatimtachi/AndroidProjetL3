package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.htmx.pro.R;
import com.example.htmx.pro.emploi.Main_Emploi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by htmx on 11/12/16.
 */
/*TODO il faut faire toute le reste qui va avec le view et merci*/
public class Main_Employeur extends Activity {
    long nb_info=0,nb_com=0,nb_bio=0;
    String String_job="";
    ArrayList<String> listJob=new ArrayList<String>();
    DatabaseReference databaseReference;
    private Button btn_create,btn_connected;
    Context context=this;
    private RecyclerView recyclerView;
    private EditText E_email;
    private EditText E_MDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!connecter()) {
            setContentView(R.layout.non_conneter);
            Button btn_retry = (Button) findViewById(R.id.btn_non_connecter);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Main_Employeur.class);
                    startActivity(intent);
                }
            }
            );

        } else {
            setContentView(R.layout.main_employeur);
            /**decalartion**/
            databaseReference = FirebaseDatabase.getInstance().getReference();
            btn_create = (Button) findViewById(R.id.main_employeur_btn_create);
            btn_connected = (Button) findViewById(R.id.main_employeur_btn_login);
            recyclerView = (RecyclerView) findViewById(R.id.main_employeur_RecycleV);
            E_email = (EditText) findViewById(R.id.main_employeur_E_email);
            E_MDP = (EditText) findViewById(R.id.main_employeur_E_mdp);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            /**action btn**/
            btn_connected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count_ok = 3;
                    String email = E_email.getText().toString().trim();
                    String MDP = E_MDP.getText().toString().trim();
                    if (email.equals("")) {
                        E_email.setError(getResources().getString(R.string.obligatoire));
                        count_ok--;
                    }
                    if (!isValideEmail(email)) {
                        E_email.setError(getResources().getString(R.string.email_nn_valide));
                        count_ok--;
                    }
                    if ((MDP.equals("")) || MDP.length() <= 5) {
                        E_MDP.setError(getResources().getString(R.string.MDP_cara_Problem));
                        count_ok--;
                    }
                    if (count_ok == 3) {
                        if_exists_recherche(email, MDP);
                    }
                }
            });

            btn_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Emp_creation.class);
                    startActivity(intent);
                }
            });
            getAllJob();
        }
    }
    private boolean connecter() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void if_exists_recherche(final String id_email,final String id_MDP){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(id_email.hashCode());
                int I_id= Math.abs(id_email.hashCode());
                String id=Integer.toString(I_id);
                String password;

                if (dataSnapshot.child("employeur").child(id).exists()){
                    password=dataSnapshot.child("employeur").child(id).child("password").getValue().toString();
                    if (password.equals(id_MDP)){
                        Intent intent=new Intent(Main_Employeur.this,Employeur_accueil_main.class);
                        startActivity(intent);
                    }
                    else {
                        E_MDP.setError(getResources().getString(R.string.MDP_nn_valide));
                    }
                }
                else {
                    E_email.setError(getResources().getString(R.string.email_nn_valide));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static boolean isValideEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**TODO
                    *
                   *|*
                  **|**
                 ***.***
                *********
     * cette fonction y a dedant que de la merde a refaire pour quelle soit globale

     * **/
    public void getAllJob(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("job").exists()){

                    /***informatique***/
                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info1)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info1)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info2)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info2)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info3)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info3)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info4)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info4)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info5)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info5)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info6)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info6)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info7)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info7)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info8)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info8)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info9)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info9)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.info10)).exists()){
                        nb_info+=dataSnapshot.child("job").child(getResources().getString(R.string.info10)).getChildrenCount();
                    }

                    System.out.println("//**"+nb_info);


                    /***comm**/

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com1)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com1)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com2)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com2)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com3)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com3)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com4)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com4)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com5)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com5)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com6)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com6)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com7)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com7)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.com8)).exists()){
                        nb_com+=dataSnapshot.child("job").child(getResources().getString(R.string.com8)).getChildrenCount();
                    }

                    System.out.println("//**"+nb_com);

                    /**bio*/

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio1)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio1)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio2)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio2)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio3)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio3)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio4)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio4)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio5)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio5)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio6)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio6)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio7)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio7)).getChildrenCount();
                    }

                    if(dataSnapshot.child("job").child(getResources().getString(R.string.bio8)).exists()){
                        nb_bio+=dataSnapshot.child("job").child(getResources().getString(R.string.bio8)).getChildrenCount();
                        listJob.add(getResources().getString(R.string.info)+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+nb_info);
                        listJob.add(getResources().getString(R.string.comm)+"\t\t\t\t\t\t\t\t\t\t"+nb_com);
                        listJob.add(getResources().getString(R.string.bio)+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+nb_bio);
                    }

                    System.out.println("//**"+getResources().getString(R.string.bio)+"\t\t\t"+nb_bio);
                    mettre_contenu_job(listJob);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ddsfdslfmsflsdfmdflùdmfqùsmlfd*mfdsmqflqsdfùmsdmlfqsdmlf*s");
            }
        });
    }

    public void mettre_contenu_job(ArrayList<String> listJob){
        AllinfoJob allinfoJob=new AllinfoJob(context,listJob);
        recyclerView.setAdapter(allinfoJob);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

}
