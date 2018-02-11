package com.example.htmx.pro.emploi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htmx on 11/11/16.
 */
public class TabFragment2 extends Fragment {
    String[] info,bio,com;
    private DatabaseReference databaseReference;
    List<String> groupList;
    List<String> childList;
    Map <String,List<String>> itemcollection;
    ExpandableListView expandableListView;
    Activity activity;
    Context context;
    Button btn_ok;
    Use_BD_interne useBdInterne;
    String id_email,connect;
    private ArrayList List_selected=new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.e_fragment_accueil_2,container,false);
        activity=getActivity();
        context=v.getContext();
        createGroupList();
        createCollection();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        useBdInterne = new Use_BD_interne(context);
        btn_ok=(Button)v.findViewById(R.id.fragment_accueil_2_Ex_Btn);
        expandableListView=(ExpandableListView)v.findViewById(R.id.fragment_accueil_2_Ex_listView);
        final ExpandableListAdapter expandableListAdapter =new ExpandableListAdapter(activity,groupList,itemcollection);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) expandableListAdapter.getChild(groupPosition, childPosition);
                if(getAll_of_selected_and_add(selected)){
                    Snackbar bar = Snackbar.make(v, selected+" "+getResources().getString(R.string.ajouter_a_liste),Snackbar.LENGTH_SHORT);
                    bar.show();
                }
                else{
                    Snackbar bar = Snackbar.make(v, selected+" "+getResources().getString(R.string.deja_dans_la_liste),Snackbar.LENGTH_SHORT);
                    bar.show();
                }
                return true;
            }
        });
        getUserFromData();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=LayoutInflater.from(context);
                View view=inflater.inflate(R.layout.layout_postule,null);
                AlertDialog.Builder alertBD=new AlertDialog.Builder(context);
                alertBD.setView(view);

                RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.potule_recycleV);

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                final PostuleAdapter adapter=new PostuleAdapter(context,List_selected);
                recyclerView.setAdapter(adapter);

                alertBD.setCancelable(false).setPositiveButton(getResources().getString(R.string.postuler).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(context);
                        alert.setTitle(R.string.merci);
                        alert.setIcon(R.drawable.valide);
                        alert.setMessage(getResources().getString(R.string.phrase_magice));
                        AlertDialog aD=alert.create();
                        updata_Job(List_selected);
                        aD.show();
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
        return v;
    }
    public boolean getAll_of_selected_and_add(String add){
        Iterator it;
        it=List_selected.iterator();
        Object next;
        boolean trouver=false;
        while(it.hasNext()){
            next=it.next();
            if(next.toString().equals(add)){
                trouver=true;
            }
        }
        if (trouver==false){
            List_selected.add(add);
            return true;
        }
        return false;
    }
    public void updata_Job(ArrayList<String> list){
        final Iterator it=list.iterator();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("job").exists()) {
                    while(it.hasNext()) {
                        databaseReference.child("job").child(it.next().toString().trim()).child(id_email).setValue("");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void createGroupList(){
        groupList =new ArrayList<String>();
        groupList.add(getResources().getString(R.string.info));
        groupList.add(getResources().getString(R.string.bio));
        groupList.add(getResources().getString(R.string.comm));
    }

    private void createCollection(){
        info= new String[]{getResources().getString(R.string.info1).toString(), getResources().getString(R.string.info2).toString()
                , getResources().getString(R.string.info3).toString(), getResources().getString(R.string.info4).toString()
                , getResources().getString(R.string.info5).toString(), getResources().getString(R.string.info6).toString()
                , getResources().getString(R.string.info7).toString(), getResources().getString(R.string.info8).toString()
                , getResources().getString(R.string.info9).toString(), getResources().getString(R.string.info10).toString()
        };
        bio= new String[]{getResources().getString(R.string.bio1).toString(), getResources().getString(R.string.bio2).toString()
                , getResources().getString(R.string.bio3).toString(), getResources().getString(R.string.bio4).toString()
                , getResources().getString(R.string.bio5).toString(), getResources().getString(R.string.bio6).toString()
                , getResources().getString(R.string.bio7).toString(), getResources().getString(R.string.bio8).toString()
        };
       com= new String[]{getResources().getString(R.string.com1).toString(), getResources().getString(R.string.com2).toString()
               , getResources().getString(R.string.com3).toString(), getResources().getString(R.string.com4).toString()
               , getResources().getString(R.string.com5).toString(), getResources().getString(R.string.com6).toString()
               , getResources().getString(R.string.com7).toString(), getResources().getString(R.string.com8).toString()
       };
        itemcollection=new LinkedHashMap<String, List<String>>();
        for (String item:groupList){
            if (item.equals(getResources().getString(R.string.info))){
                loadChild(info);
            }
            else if (item.equals(getResources().getString(R.string.bio))){
                loadChild(bio);
            }
            else if (item.equals(getResources().getString(R.string.comm))){
                loadChild(com);
            }
            itemcollection.put(item,childList);
        }
    }
    private void loadChild(String[] items){
        childList =new ArrayList<String>();
        for (String item:items){
            childList.add(item);
        }
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
}