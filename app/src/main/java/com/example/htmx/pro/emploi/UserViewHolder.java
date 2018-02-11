package com.example.htmx.pro.emploi;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.htmx.pro.R;

public class UserViewHolder extends  RecyclerView.ViewHolder{
    public TextView txcompetence;
    public UserViewHolder(View view){
        super(view);
        txcompetence=(TextView)view.findViewById(R.id.TextV_tabfragment1_competence_textlist);
    }
}
