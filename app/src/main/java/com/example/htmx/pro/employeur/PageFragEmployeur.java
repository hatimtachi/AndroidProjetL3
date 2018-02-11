package com.example.htmx.pro.employeur;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.htmx.pro.R;

/**
 * Created by htmx on 20/12/16.
 */
public class PageFragEmployeur extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container
            ,Bundle savedInstanceState) {
        ViewGroup root =(ViewGroup)inflater.inflate(R.layout.employeur_accueil_contenu_view_pager,container,false);
        return root;
    }
}
