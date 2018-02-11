package com.example.htmx.pro.employeur;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.htmx.pro.R;

/**
 * Created by htmx on 13/12/16.
 */
public class JobViewHolder extends RecyclerView.ViewHolder {
    public TextView txcompetence;
    public JobViewHolder(View view){
        super(view);
        txcompetence=(TextView)view.findViewById(R.id.employeur_listeview_textlist);
    }
}
