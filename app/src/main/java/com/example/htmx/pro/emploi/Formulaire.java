package com.example.htmx.pro.emploi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by htmx on 20/11/16.
 */


public class Formulaire extends AppCompatActivity {
    private List <String>List_formation=new ArrayList<String>();
    private Use_BD_interne useBdInterne;
    private DatabaseReference databaseReference;
    private String E_F_ecole,E_F_diplome,E_F_description,E_F_domaineetude,result_to_formation="",E_F_debut,E_F_fin;
    private Button btn_back,btn_save,btn_ajouterFormation;
    private EditText E_prenom,E_nom,E_formation,E_titre_profil,E_resume;
    final Context context = this;
    private static final int GALLERY_INTENT = 2;
    private StorageReference storageReference;
    private ImageButton imageBtn;
    private ImageView img_profil;
    private String nom,prenom,email,formation,titre_profil,description,id_email,connect,E_F_titre_profil,E_F_resume,E_F_nom,E_F_prenom,E_F_formation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formul_main_emploi);

        /**
         * inisalisation de tout
         */
        storageReference= FirebaseStorage.getInstance().getReference();
        useBdInterne = new Use_BD_interne(this);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        img_profil=(ImageView)findViewById(R.id.profile_image_formulaire);
        btn_back=(Button)findViewById(R.id.btn_formule_main_emploi_back);
        btn_save=(Button)findViewById(R.id.btn_formule_main_emploi_save);
        btn_ajouterFormation=(Button)findViewById(R.id.btn_formule_main_emploi_ajouter_une_formation);
        imageBtn=(ImageButton)findViewById(R.id.btn_image_change_main_emploi_formulaire);
        E_nom=(EditText)findViewById(R.id.E_formule_main_emploi_nom);
        E_prenom=(EditText)findViewById(R.id.E_formule_main_emploi_prenom);
        E_formation=(EditText)findViewById(R.id.E_formule_main_emploi_formation);
        E_titre_profil=(EditText)findViewById(R.id.E_formule_main_emploi_titre_du_profil);
        E_resume=(EditText)findViewById(R.id.E_formule_main_emploi_resume);

        /*
        fin
         */

        /**
         * recuper et mettre devant
         */

        E_formation.setFocusable(false);
        getUserFromData();
        getAllInfoUser(id_email);
        storageReference.child("photo").child(id_email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(img_profil);
            }
        });
        /**
         * les setonclick de tout les button
         */
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Formulaire.this,Main_Emploi.class);
                startActivity(intent);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                E_F_nom=E_nom.getText().toString().trim();
                E_F_prenom=E_prenom.getText().toString().trim();
                E_F_titre_profil=E_titre_profil.getText().toString().trim();
                E_F_formation=E_formation.getText().toString().trim();
                E_F_resume=E_resume.getText().toString().trim();
                updateData_Serveur(id_email,E_F_nom,E_F_prenom,E_F_titre_profil,E_F_resume);
                updateData_Formation(id_email,result_to_formation);
                getUserFromData();
                getAllInfoUser(id_email);
                Intent intent=new Intent(Formulaire.this,Main_Emploi.class);
                startActivity(intent);
            }
        });


        btn_ajouterFormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *creation de la dialog avec le layout qui va avec que j'ai cree
                 *
                 *avec le btn save pour sauvgarder
                 */
                LayoutInflater inflater =LayoutInflater.from(context);
                View view =inflater.inflate(R.layout.main_emploi_dialgo_formule,null);

                AlertDialog.Builder alertDB=new AlertDialog.Builder(context);
                alertDB.setView(view);
                /**declaration des input**/
                final  EditText E_ecole=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_ecole);
                final  EditText E_diplome=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_diplome);
                final  EditText E_domaine_etude=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_domaine_etude);
                final  EditText E_description=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_description);
                final  EditText E_debut=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_date_debut);
                final  EditText E_fin=(EditText)view.findViewById(R.id.E_formule_main_emploi_dialog_date_fin);

                E_ecole.setText(E_F_ecole);
                E_diplome.setText(E_F_diplome);
                E_description.setText(E_F_description);
                E_domaine_etude.setText(E_F_domaineetude);

                alertDB.setCancelable(false).setPositiveButton(getResources().getString(R.string.enregistre).toString(), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        E_F_ecole=E_ecole.getText().toString().trim();
                        E_F_diplome=E_diplome.getText().toString().trim();
                        E_F_description=E_description.getText().toString().trim();
                        E_F_domaineetude=E_domaine_etude.getText().toString().trim();
                        E_F_debut=E_debut.getText().toString().trim();
                        E_F_fin=E_fin.getText().toString().trim();

                        result_to_formation=E_F_diplome+" "+E_F_domaineetude+" "+E_F_ecole+" "+E_F_description+"("+E_F_debut+" "+E_F_fin+")";
                        E_formation.setText(result_to_formation);
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
    }

    /**
     *
     * pour sauvgarder tout les information sans la formation c'est dans une autre fonction
     *
     */
    public void updateData_Serveur(final String id_email,final String nom,final String prenom,final String titre_profil,final String resume){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id_email).exists()){
                    databaseReference.child("users").child(id_email).child("nom").setValue(nom);
                    databaseReference.child("users").child(id_email).child("prenom").setValue(prenom);
                    databaseReference.child("users").child(id_email).child("titre_profil").setValue(titre_profil);
                    databaseReference.child("users").child(id_email).child("resume").setValue(resume);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateData_Formation(final String id_email,final String formationParam){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id_email).exists()){
                    String x_to_xString="";
                    if(!formationParam.equals("")){
                        int x=formationParam.hashCode();
                        x_to_xString=Integer.toString(x);
                    }
                    if(!dataSnapshot.child("users").child(id_email).child("formation").child(x_to_xString).exists()){
                        databaseReference.child("users").child(id_email).child("formation").child(x_to_xString).setValue("\t"+formationParam+"\n");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List recuperString_to_Array(String lestring){
        int i;
        char restemp;
        String sretemp="";
        Iterator it;
        List<String>ListTextString=new ArrayList<String>();
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
     *
     * pour avoir le contenu du serveur
     *
     */
    Iterator iterator;
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
                    List_formation=recupere_le_titre_Formation(List_formation);
                    iterator=List_formation.iterator();
                }
                if (dataSnapshot.child("users").child(id_email).child("titre_profil").exists()){
                    titre_profil=dataSnapshot.child("users").child(id_email).child("titre_profil").getValue().toString();
                }
                if(dataSnapshot.child("users").child(id_email).child("resume").exists()){
                    description=dataSnapshot.child("users").child(id_email).child("resume").getValue().toString();
                }
          //      System.out.println("tout les info :\n"+nom+"\n"+prenom+"\n"+email+"\n"+formation+"\n"+titre_profil+"\n"+description);
                E_nom.setText(nom);
                E_prenom.setText(prenom);
                if(List_formation.size()!=0) {
                    while (iterator.hasNext()) {
                        E_formation.setText(iterator.next().toString());
                    }
                }
                E_titre_profil.setText(titre_profil);
                E_resume.setText(description);
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

    public List recupere_le_titre_Formation(List list){
        String res="",tmp;
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

    /**
     * le upload l'image to storage
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            Uri uri =data.getData();
            final StorageReference filepath=storageReference.child("photo").child(id_email);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context,"Telechargement is ok",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
