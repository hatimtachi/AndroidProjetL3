package com.example.htmx.pro.employeur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.htmx.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htmx on 26/12/16.
 */
public class View_emp_favori extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView T_domaine,T_nom,T_prenom;
    LinearLayout line;
    List<Favori> favoris=new ArrayList<>();
    Context context;
    String num_tel="";
    String email;
    Use_BD_employeur employeurBd;
    String domaine="";
    private DatabaseReference databaseReference;


    public View_emp_favori(View view, Context context, List<Favori>favoris){
        super(view);
        employeurBd=new Use_BD_employeur(Emp_favori.getAppContext());
        databaseReference= FirebaseDatabase.getInstance().getReference();
        this.context=context;
        this.favoris=favoris;
        view.setOnClickListener(this);
        line=(LinearLayout)view.findViewById(R.id.line1);
        T_domaine=(TextView)view.findViewById(R.id.emp_favori_contenu_domaine);
        T_nom=(TextView)view.findViewById(R.id.emp_favori_contenu_nom);
        T_prenom=(TextView)view.findViewById(R.id.emp_favori_contenu_prenom);
    }



    @Override
    public void onClick(View v) {
        System.out.println("hello merdeeeeeeeeeeeeeeeeeeeee");
        int position =getAdapterPosition();
        Favori favori=this.favoris.get(position);
        final String nom=T_nom.getText().toString().trim();
        final String prenom=T_prenom.getText().toString().trim();
        domaine=T_domaine.getText().toString().trim();
        get_user_form_id_email(nom,prenom);
        Intent intent=new Intent(this.context,Emp_item_selected.class);
        this.context.startActivity(intent);
    }
    public void get_user_form_id_email(final String nom,final String prenom){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                employeurBd.open();
                Cursor res = employeurBd.getAll();

                for (int i=0;i<res.getCount();i++) {
                    res.moveToNext();
                    String x = res.getString(res.getColumnIndex(Employeur_bd.COLUMN_ID_CV));


                    if (dataSnapshot.child("users").child(x).exists()){

                        if ((dataSnapshot.child("users").child(x).child("nom").getValue().equals(nom))
                                &&(dataSnapshot.child("users").child(x).child("prenom").getValue().equals(prenom))){
                            /***
                             * c'est possible qu'un utilisateur a le meme nom et prenom mais il ne va jamais avoir le meme email
                             */
                            email=dataSnapshot.child("users").child(x).child("email").getValue().toString().trim();
                            int I_id= Math.abs(email.hashCode());
                            String id=Integer.toString(I_id);

                            if (x.equals(id)){

                                if (dataSnapshot.child("users").child(id).child("tele").exists()) {
                                    num_tel = dataSnapshot.child("users").child(id).child("tele").getValue().toString().trim();
                                    System.out.println(num_tel);
                                }
                                else{
                                    /**TODO  disabled  le btn_appel car il n'y a pas de num*/
                                    num_tel="123456789";

                                }
                            }
                        }
                    }
                }
                SharedPreferences preferences=context.getSharedPreferences(context.getResources().getString(R.string.prefrence_id2)
                        ,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(context.getResources().getString(R.string.prefrence_email),email);
                editor.putString(context.getResources().getString(R.string.prefrence_num),num_tel);
                editor.putString(context.getResources().getString(R.string.prefrence_prenom),prenom);
                editor.putString(context.getResources().getString(R.string.prefrence_nom),nom);
                editor.putString(context.getResources().getString(R.string.prefrence_domaine),domaine);

                editor.commit();
                employeurBd.close();
                //   tx_email.setText(email);
               /* if (num_tel.equals("123456789")){
                    btn_call.setVisibility(View.INVISIBLE);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}