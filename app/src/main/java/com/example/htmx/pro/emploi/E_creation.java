package com.example.htmx.pro.emploi;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by htmx on 26/10/16.
 */



public class E_creation extends Activity {
    private DatabaseReference databaseReference;
    private EditText E_nom,E_prenom,E_password,E_passwordC,E_email;
    private Button Btn_save;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emploi_creation_compte);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        E_nom=(EditText)findViewById(R.id.E_Nom);
        E_prenom=(EditText) findViewById(R.id.E_Prenom);
        E_password=(EditText) findViewById(R.id.E_MDP);
        E_passwordC=(EditText) findViewById(R.id.E_MDPC);
        E_email=(EditText) findViewById(R.id.E_Email);

        Btn_save=(Button) findViewById(R.id.Btn_save);
        Btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CreateUser();
            }

        });

    }

    private static boolean isValideEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * pour recupere les info du database (email ) et insert le nouveau utilisateur s'il n'exists pas
     */
    boolean problem_email=false;
    public void chercher_email(final String id_email,final User_emploi user){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("users").child(id_email).exists()){
                    databaseReference.child("users").child(id_email).setValue(user);
                    AlertDialog alertDialog = new AlertDialog.Builder(E_creation.this).create();

                    alertDialog.setTitle(getResources().getString(R.string.cree_avec_scuces));

                    alertDialog.setIcon(R.drawable.icondevalidation);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(E_creation.this,Emploi.class);
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

    private boolean CreateUser(){
        int count_is_ok=4;
        String nom=E_nom.getText().toString().trim();
        String prenom=E_prenom.getText().toString().trim();
        String password=E_password.getText().toString().trim();
        String passwordC=E_passwordC.getText().toString().trim();
        String email=E_email.getText().toString().trim();

        if (nom.equals("")){
            E_nom.setError(getResources().getString(R.string.obligatoire));
            count_is_ok--;
        }
        if (prenom.equals("")){
            E_prenom.setError(getResources().getString(R.string.obligatoire));
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
            E_password.setError(getResources().getString(R.string.MDP_nn_valide));
            E_passwordC.setError(getResources().getString(R.string.MDP_nn_valide));
            count_is_ok--;
        }
        if(password.length()<=5){
            E_password.setError(getResources().getString(R.string.MDP_cara_Problem));
            count_is_ok--;
        }

        /**
         *
         * hashcode==email c'est l'identifiant de utilisateur et il est unique
         *le hashcode je l'ai mis donner une valeur positif pour pouvoir le stoker dans la base
         *
         * toute est ok
         * la je fait la verification du hashcode est ce qu'il exists deja dans la base ou pas
         * si nn je le place dans la base
         *
         *
         */

        if (count_is_ok==4){
            System.out.println(email.hashCode());
            int I_id= Math.abs(email.hashCode());
            String id=Integer.toString(I_id);
            User_emploi user =new User_emploi(nom,prenom,password,email);
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
        String nom,prenom,email,passe,passec;
        nom=E_nom.getText().toString().trim();
        email=E_email.getText().toString().trim();
        prenom=E_prenom.getText().toString().trim();
        passe=E_password.getText().toString().trim();
        passec=E_passwordC.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id4),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_nom_emploi),nom);
        editor.putString(getString(R.string.prefrence_prenom_emploi),prenom);
        editor.putString(getString(R.string.prefrence_email_emploi),email);
        editor.putString(getString(R.string.prefrence_pass_emploi),passe);
        editor.putString(getString(R.string.prefrence_passC_emploi),passec);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String nom,prenom,email,passe,passec;
        nom=E_nom.getText().toString().trim();
        email=E_email.getText().toString().trim();
        prenom=E_prenom.getText().toString().trim();
        passe=E_password.getText().toString().trim();
        passec=E_passwordC.getText().toString().trim();
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id4),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(getString(R.string.prefrence_nom_emploi),nom);
        editor.putString(getString(R.string.prefrence_prenom_emploi),prenom);
        editor.putString(getString(R.string.prefrence_email_emploi),email);
        editor.putString(getString(R.string.prefrence_pass_emploi),passe);
        editor.putString(getString(R.string.prefrence_passC_emploi),passec);
        editor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String nom,prenom,email,passe,passec;
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id4),Context.MODE_PRIVATE);
        nom= preferences.getString(getString(R.string.prefrence_nom_emploi),null);
        prenom=preferences.getString(getString(R.string.prefrence_prenom_emploi),null);
        email=preferences.getString(getString(R.string.prefrence_email_emploi),null);
        passe=preferences.getString(getString(R.string.prefrence_pass_emploi),null);
        passec=preferences.getString(getString(R.string.prefrence_passC_emploi),null);
        E_nom.setText(nom);
        E_prenom.setText(prenom);
        E_email.setText(email);
        E_password.setText(passe);
        E_passwordC.setText(passec);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String nom,prenom,email,passe,passec;
        SharedPreferences preferences=context.getSharedPreferences(getString(R.string.prefrence_id4),Context.MODE_PRIVATE);
        nom= preferences.getString(getString(R.string.prefrence_nom_emploi),null);
        prenom=preferences.getString(getString(R.string.prefrence_prenom_emploi),null);
        email=preferences.getString(getString(R.string.prefrence_email_emploi),null);
        passe=preferences.getString(getString(R.string.prefrence_pass_emploi),null);
        passec=preferences.getString(getString(R.string.prefrence_passC_emploi),null);
        E_nom.setText(nom);
        E_prenom.setText(prenom);
        E_email.setText(email);
        E_password.setText(passe);
        E_passwordC.setText(passec);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        E_nom.setText(null);
        E_prenom.setText(null);
        E_email.setText(null);
        E_password.setText(null);
        E_passwordC.setText(null);
    }
}
