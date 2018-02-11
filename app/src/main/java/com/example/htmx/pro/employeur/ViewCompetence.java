package com.example.htmx.pro.employeur;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.htmx.pro.R;

/**
 * Created by htmx on 24/12/16.
 */
public class ViewCompetence extends RecyclerView.ViewHolder {
    public TextView txcompetence;
    public ViewCompetence(View view){
        super(view);
        txcompetence=(TextView)view.findViewById(R.id.employeur_accueil_recyclerview_contenu_competence);
    }
}
