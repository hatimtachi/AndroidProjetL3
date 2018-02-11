package com.example.htmx.pro.employeur;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmx.pro.R;

import java.util.ArrayList;

/**
 * Created by htmx on 24/12/16.
 */
public class InfoCompetence extends RecyclerView.Adapter {
    private Context context;
    ArrayList<String> list;
    public InfoCompetence(Context context,ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(context).inflate(R.layout.employeur_accueil_recyv_contenu_competence,null);
        ViewCompetence infoComp=new ViewCompetence(view);
        return infoComp;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,int i){
        ViewCompetence viewCompetence=(ViewCompetence) viewHolder;
        viewCompetence.txcompetence.setText(list.get(i));
    }
    @Override
    public int getItemCount(){
        return list.size();
    }
}
