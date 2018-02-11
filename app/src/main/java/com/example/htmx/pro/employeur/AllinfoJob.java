package com.example.htmx.pro.employeur;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmx.pro.R;

import java.util.ArrayList;

/**
 * Created by htmx on 13/12/16.
 */
public class AllinfoJob extends RecyclerView.Adapter {
    private Context context;
    ArrayList<String>list;
    public AllinfoJob(Context context,ArrayList<String> list){
        this.list=list;
        this.context=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(context).inflate(R.layout.employeur_listeview_job,null);
        JobViewHolder jobViewHolder=new JobViewHolder(view);
        return jobViewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,int i){
        JobViewHolder holder =(JobViewHolder) viewHolder;
        holder.txcompetence.setText(list.get(i));
    }
    @Override
    public int getItemCount(){
        return list.size();
    }
}
