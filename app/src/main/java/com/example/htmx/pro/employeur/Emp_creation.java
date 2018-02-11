package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.htmx.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by htmx on 11/12/16.
 */
public class Emp_creation extends Activity {

    private DatabaseReference databaseReference;
    Context context=this;
    Button btn_create;
    private EditText E_nom,E_prenom,E_societe,E_mdp,E_mdpC,E_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeur_creation);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        E_nom=(EditText)findViewById(R.id.employeur_cretion_Nom);
        E_prenom=(EditText)findViewById(R.id.employeur_cretion_Prenom);
        E_societe=(EditText)findViewById(R.id.employeur_cretion_societe);
        E_mdp=(EditText)findViewById(R.id.employeur_cretion_MDP);
        E_mdpC=(EditText)findViewById(R.id.employeur_cretion_MDPC);
        E_email=(EditText)findViewById(R.id.employeur_cretion_Email);

        btn_create=(Button)findViewById(R.id.employeur_creation_btn_create);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();

            }
        });
    }

    private static boolean isValideEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    boolean problem_email=false;
    public void chercher_email(final String id_email,final User_employeur user){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("employeur").child(id_email).exists()){
                    databaseReference.child("employeur").child(id_email).setValue(user);
                    AlertDialog alertDialog = new AlertDialog.Builder(Emp_creation.this).create();

                    alertDialog.setTitle(getResources().getString(R.string.cree_avec_scuces));

                    alertDialog.setIcon(R.drawable.icondevalidation);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Emp_creation.this,Main_Employeur.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
                else{
                    problem_email=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean CreateUser(){
        int count_is_ok=5;
        String nom=E_nom.getText().toString().trim();
        String prenom=E_prenom.getText().toString().trim();
        String societe=E_societe.getText().toString().trim();
        String password=E_mdp.getText().toString().trim();
        String passwordC=E_mdpC.getText().toString().trim();
        String email=E_email.getText().toString().trim();

        if (nom.equals("")){
            E_nom.setError(getResources().getString(R.string.obligatoire));
            count_is_ok--;
        }
        if (prenom.equals("")){
            E_prenom.setError(getResources().getString(R.string.obligatoire));
            count_is_ok--;
        }
        if (societe.equals("")){
            E_societe.setError(getResources().getString(R.string.obligatoire));
            count_is_ok--;
        }
        if (email.equals("")){
            E_email.setError(getResources().getString(R.string.obligatoire));
            count_is_ok--;
        }
        if((!isValideEmail(email))||(problem_email==true)){
            E_email.setError(getResources().getString(R.string.email_nn_valide));
            count_is_ok--;
        }
        if((password.equals("") && password.equals(passwordC))||(!password.equals(passwordC))){
            E_mdp.setError(getResources().getString(R.string.MDP_nn_valide));
            E_mdpC.setError(getResources().getString(R.string.MDP_nn_valide));
            count_is_ok--;
        }
        if(password.length()<=5){
            E_mdp.setError(getResources().getString(R.string.MDP_cara_Problem));
            count_is_ok--;
        }

        if (count_is_ok==5){
            System.out.println(email.hashCode());
            int I_id= Math.abs(email.hashCode());
            String id=Integer.toString(I_id);
            User_employeur user =new User_employeur(nom,prenom,societe,password,email);
            Log.e("id",id);
            chercher_email(id,user);
            return true;
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.compte_exsits_deja),Toast.LENGTH_SHORT).show();
        }
        problem_email=false;
        return false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        String nom,prenom,email,passe,passec,sauce;
        nom=E_nom.getText().toString().trim();
        email=E_email.getText().toString().trim();
        prenom=E_prenom.getText().toString().trim();
        passe=E_mdp.getText().toString().trim();
        passec=E_mdpC.getText().toString().trim();
        sauce=E_societe.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id5), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_nom_emp),nom);
        editor.putString(getString(R.string.prefrence_prenom_emp),prenom);
        editor.putString(getString(R.string.prefrence_email_emp),email);
        editor.putString(getString(R.string.prefrence_pass_emp),passe);
        editor.putString(getString(R.string.prefrence_passC_emp),passec);
        editor.putString(getString(R.string.prefrence_soci_emp),sauce);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String nom,prenom,email,passe,passec,sauce;
        nom=E_nom.getText().toString().trim();
        email=E_email.getText().toString().trim();
        prenom=E_prenom.getText().toString().trim();
        passe=E_mdp.getText().toString().trim();
        passec=E_mdpC.getText().toString().trim();
        sauce=E_societe.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id5), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_nom_emp),nom);
        editor.putString(getString(R.string.prefrence_prenom_emp),prenom);
        editor.putString(getString(R.string.prefrence_email_emp),email);
        editor.putString(getString(R.string.prefrence_pass_emp),passe);
        editor.putString(getString(R.string.prefrence_passC_emp),passec);
        editor.putString(getString(R.string.prefrence_soci_emp),sauce);
        editor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String nom,prenom,email,passe,passec,sauce;
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id5),Context.MODE_PRIVATE);
        nom= preferences.getString(getString(R.string.prefrence_nom_emp),null);
        prenom=preferences.getString(getString(R.string.prefrence_prenom_emp),null);
        email=preferences.getString(getString(R.string.prefrence_email_emp),null);
        passe=preferences.getString(getString(R.string.prefrence_pass_emp),null);
        passec=preferences.getString(getString(R.string.prefrence_passC_emp),null);
        sauce=preferences.getString(getString(R.string.prefrence_soci_emp),null);
        E_nom.setText(nom);
        E_prenom.setText(prenom);
        E_email.setText(email);
        E_mdp.setText(passe);
        E_mdpC.setText(passec);
        E_societe.setText(sauce);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String nom,prenom,email,passe,passec,sauce;
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id5),Context.MODE_PRIVATE);
        nom= preferences.getString(getString(R.string.prefrence_nom_emp),null);
        prenom=preferences.getString(getString(R.string.prefrence_prenom_emp),null);
        email=preferences.getString(getString(R.string.prefrence_email_emp),null);
        passe=preferences.getString(getString(R.string.prefrence_pass_emp),null);
        passec=preferences.getString(getString(R.string.prefrence_passC_emp),null);
        sauce=preferences.getString(getString(R.string.prefrence_soci_emp),null);
        E_nom.setText(nom);
        E_prenom.setText(prenom);
        E_email.setText(email);
        E_mdp.setText(passe);
        E_mdpC.setText(passec);
        E_societe.setText(sauce);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        E_nom.setText(null);
        E_prenom.setText(null);
        E_email.setText(null);
        E_mdp.setText(null);
        E_mdpC.setText(null);
        E_societe.setText(null);
    }
}
