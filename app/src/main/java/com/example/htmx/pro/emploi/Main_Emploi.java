package com.example.htmx.pro.emploi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.example.htmx.pro.Base_donnee_interne;
import com.example.htmx.pro.R;
import com.example.htmx.pro.Use_BD_interne;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main_Emploi extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Use_BD_interne useBdInterne;
    private Intent intent;
    private DatabaseReference databaseReference;
    private String nom,prenom,email,nom_prenom;
    private String id_email,connect;
    private  TextView T_Nom_Prenom;
    private TextView T_Email;
    private Boolean trythis=false;
    /*TODO le truc de non_connection */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!connecter()) {
            setContentView(R.layout.non_conneter);
            Button btn_retry=(Button)findViewById(R.id.btn_non_connecter);
            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Main_Emploi.this,Main_Emploi.class);
                    startActivity(intent);
                }
            }
            );

        }
        else {
            setContentView(R.layout.main_emploi);
            useBdInterne = new Use_BD_interne(this);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            T_Nom_Prenom = (TextView) findViewById(R.id.Menu_Emploi_H_TextV_Nom_Prenom);
            T_Email = (TextView) findViewById(R.id.Menu_Emploi_H_TextV_Email);
            /**
             * pour les silde inisalition
             */
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
            ActionBarDrawerToggle actionBarDrawerLayout = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.setDrawerListener(actionBarDrawerLayout);
            actionBarDrawerLayout.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.E_Menu_silde);
            navigationView.setNavigationItemSelectedListener(this);

            /**
             * fragment tab 1 et 2
             */

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_main_emploi));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.postule_main_emploi));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final PagerAdapter adapter = new PagerAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            /**
             * get le nom prenom email et le mettre dans le silde
             */
            getUserFromData();
            getAllInfoUser(id_email);
        }
    }
    /**
     * pour le silde le menu
     */
    @Override
    public void onBackPressed() {
       DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.main_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.e_slide, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * test de connection
     * @return
     */
    private boolean connecter() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /**
     * les trucs de menu
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.E_Menu_deconnect) {
            useBdInterne.open();
            useBdInterne.deleteContenuAll();
            String S_sais_qui = "0";
            String S_connect = "0";
            useBdInterne.U_create(S_sais_qui, S_connect);
            useBdInterne.close();
            intent =new Intent(Main_Emploi.this,Emploi.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAllInfoUser(final String id_email){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nom=dataSnapshot.child("users").child(id_email).child("nom").getValue().toString();
                prenom=dataSnapshot.child("users").child(id_email).child("prenom").getValue().toString();
                email=dataSnapshot.child("users").child(id_email).child("email").getValue().toString();
                nom_prenom =nom+" "+prenom;
                T_Nom_Prenom.setText(nom_prenom);
                T_Email.setText(email);

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


}