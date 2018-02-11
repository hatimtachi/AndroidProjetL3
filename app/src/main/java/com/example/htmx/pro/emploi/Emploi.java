package com.example.htmx.pro.emploi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.htmx.pro.R;
import com.example.htmx.pro.Use_BD_interne;
import com.example.htmx.pro.employeur.Emp_creation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by htmx on 25/10/16.
 */

public class Emploi extends Activity{
    private Use_BD_interne useBdInterne;
    private DatabaseReference databaseReference;
    private Button btn_MDP_O;
    private EditText E_email;
    private EditText E_MDP;
    Intent intent;
    String email;
    String MDP;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!connecter()) {
            setContentView(R.layout.non_conneter);
            Button btn_retry = (Button) findViewById(R.id.btn_non_connecter);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Emploi.this, Emploi.class);
                    startActivity(intent);
                }
            }
            );

        } else {
            setContentView(R.layout.l_emploi);
            Button btn_creation;
            Button btn_connexion;
            databaseReference = FirebaseDatabase.getInstance().getReference();
            btn_connexion = (Button) findViewById(R.id.Btn_connexion);
            btn_creation = (Button) findViewById(R.id.Btn_creation);
            btn_MDP_O = (Button) findViewById(R.id.Btn_MDP_O);
            E_email = (EditText) findViewById(R.id.E_email);
            E_MDP = (EditText) findViewById(R.id.E_MDP);
            useBdInterne = new Use_BD_interne(this);


            btn_creation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Emploi.this, E_creation.class);
                    startActivity(intent);
                }
            });
            btn_MDP_O.setVisibility(View.INVISIBLE);
            btn_MDP_O.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Emploi.this, E_mdp_O.class);
                    startActivity(intent);
                }
            });

            btn_connexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count_ok = 3;
                    email = E_email.getText().toString().trim();
                    MDP = E_MDP.getText().toString().trim();
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

        }
    }

    /**
     *
     * 1 est emploi et 2 est employeur 0 est rien
     *
     * @param id_email
     * @param id_MDP
     */
    public void if_exists_recherche(final String id_email,final String id_MDP){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(id_email.hashCode());
                int I_id= Math.abs(id_email.hashCode());
                String id=Integer.toString(I_id);
                String password;

                if (dataSnapshot.child("users").child(id).exists()){

                    password=dataSnapshot.child("users").child(id).child("password").getValue().toString();
                    if (password.equals(id_MDP)){
                        databaseReference.child("users").child("sais_qui").setValue(id);
                        useBdInterne.open();
                        useBdInterne.deleteContenuAll();
                        String S_sais_qui = id;
                        String S_connect = "1";
                        useBdInterne.U_create(S_sais_qui, S_connect);
                        useBdInterne.close();
                        Intent intent=new Intent(Emploi.this,Main_Emploi.class);
                        startActivity(intent);
                    }
                    else{
                        E_MDP.setError(getResources().getString(R.string.MDP_nn_valide));
                        btn_MDP_O.setVisibility(View.VISIBLE);
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
    private boolean connecter() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(this, Emploi.class);
        startActivity(intent);
    }
}
