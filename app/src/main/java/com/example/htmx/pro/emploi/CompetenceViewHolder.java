package com.example.htmx.pro.emploi;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.htmx.pro.R;

/**
 * Created by htmx on 27/11/16.
 */
public class CompetenceViewHolder extends  RecyclerView.ViewHolder{
    public TextView txcompetence;
    public ImageButton imgRemove;
    public CompetenceViewHolder(View view){
        super(view);
        txcompetence=(TextView)view.findViewById(R.id.TextV_main_emploi_Competence_textlist);
        imgRemove=(ImageButton)view.findViewById(R.id.Btn_main_emploi_Competence_textlist);
    }
}
