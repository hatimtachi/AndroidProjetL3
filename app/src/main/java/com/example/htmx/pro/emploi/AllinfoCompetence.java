package com.example.htmx.pro.emploi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmx.pro.R;

import java.util.ArrayList;

public class AllinfoCompetence extends RecyclerView.Adapter{
    private Context context;
   ArrayList<String> list;
    public AllinfoCompetence(Context context,ArrayList<String> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(context).inflate(R.layout.tabfragment1_competence_listview_contenu,null);
        UserViewHolder viewHolder =new UserViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,int i){
        UserViewHolder holder =(UserViewHolder) viewHolder;
        holder.txcompetence.setText(list.get(i));
    }
    @Override
    public int getItemCount(){
        return list.size();
    }
}
