package com.example.htmx.pro.employeur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.htmx.pro.R;
import com.example.htmx.pro.emploi.Emploi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by htmx on 25/12/16.
 */
public class Employeur_accueil_main extends Activity implements NavigationView.OnNavigationItemSelectedListener{
    String[] info,bio,com;
    List<String> groupList;
    List<String> childList;
    DrawerLayout drawerLayout;
    Map<String,List<String>> itemcollection;
    ExpandableListView expandableListView;
    Context context=this;
    Activity activity=Employeur_accueil_main.this;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeur_accueil_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_l_employeur);
        ActionBarDrawerToggle actionBarDrawerLayout = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerLayout);
        actionBarDrawerLayout.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.E_Menu_silde);
        navigationView.setNavigationItemSelectedListener(this);


        createGroupList();
        createCollection();
        preferences=context.getSharedPreferences(getString(R.string.prefrence_id),Context.MODE_PRIVATE);
        expandableListView=(ExpandableListView)findViewById(R.id.employeur_accueil_main_Ex_listView);
        final ListAdapterEmp listAdapterEmp=new ListAdapterEmp(activity,groupList,itemcollection);
        expandableListView.setAdapter(listAdapterEmp);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) listAdapterEmp.getChild(groupPosition, childPosition);
                if (selected_and_add(selected)){
                    Intent intent=new Intent(context,Employeur_Accueil.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    public boolean selected_and_add(String add){
        try{
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString(getString(R.string.prefrence_quoi),add);
            editor.commit();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(context,Employeur_accueil_main.class);
        startActivity(intent);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.emp_slide, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Emp_favori) {
            System.out.println("fdksdlmfsdkfsdlmfkfdsflmksdmkfsdlfsdmlkfdksdmkl");
            Intent intent=new Intent(Employeur_accueil_main.this,Emp_favori.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_l_employeur);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
