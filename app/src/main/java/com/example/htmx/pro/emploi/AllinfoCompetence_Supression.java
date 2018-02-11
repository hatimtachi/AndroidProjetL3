package com.example.htmx.pro.emploi;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmx.pro.Base_donnee_interne;
import com.example.htmx.pro.R;
import com.example.htmx.pro.Use_BD_interne;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by htmx on 27/11/16.
 */
public class AllinfoCompetence_Supression extends RecyclerView.Adapter {
    private Context context;
    String id_email,connect;
    private Use_BD_interne useBdInterne;
    ArrayList<String> list;
    DatabaseReference databaseReference;
    public AllinfoCompetence_Supression(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_emploi_dialog_competence_listview_contenu, null);
       CompetenceViewHolder viewHolder = new CompetenceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        CompetenceViewHolder holder = (CompetenceViewHolder) viewHolder;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        useBdInterne = new Use_BD_interne(context);
        holder.txcompetence.setText(list.get(position));
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item=list.get(position);
                System.out.println(item);
                supression_element(item);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size());
            }
        });
    }
    /**
     *je ne sais pas c'est que le problem donc j'ai ecrit cette fonction pour le regle car je donne 'lisp' il me renvoie 5 pour ca size
     **/
    public String enleve_le_truc_en_plus(String s){
        String res="";
        for (int i=0;i<s.length()-1;i++){
            res+=s.charAt(i);
            System.out.println(res.charAt(i));
            System.out.println(res.length());
        }
        return res;
    }
    public void supression_element(final String element){
        getUserFromData();
        final String newElement=enleve_le_truc_en_plus(element);
        final int hashnewElement=newElement.hashCode();
        final String FnewElement=Integer.toString(hashnewElement);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id_email).child("competence").exists()) {
                    if(dataSnapshot.child("users").child(id_email).child("competence").child(FnewElement).exists()){
                        dataSnapshot.child("users").child(id_email).child("competence").child(FnewElement).getRef().setValue(null);
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
    @Override
    public int getItemCount() {
        return list.size();
    }
}
