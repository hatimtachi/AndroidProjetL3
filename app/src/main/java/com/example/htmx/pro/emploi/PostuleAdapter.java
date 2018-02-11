package com.example.htmx.pro.emploi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.htmx.pro.R;

import java.util.List;

/**
 * Created by htmx on 09/12/16.
 */
public class PostuleAdapter extends RecyclerView.Adapter<PostuleAdapter.ViewHolder> {
    private List<String> dataset;
    private Context context;

    public PostuleAdapter(Context c,List<String> list){
        this.dataset=list;
        this.context=c;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.layout_postule_contenu_textV);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.layout_postule_contenu_rel);
        }
    }

    @Override
    public PostuleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.layout_postule_contenu,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int postion){
        holder.textView.setText((String)dataset.get(postion));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemLabel =dataset.get(postion);
                dataset.remove(postion);
                notifyItemRemoved(postion);
                notifyItemRangeChanged(postion,dataset.size());
            }
        });
    }
    @Override
    public int getItemCount(){
        return dataset.size();
    }
}
