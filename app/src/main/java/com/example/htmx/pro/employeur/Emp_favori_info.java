package com.example.htmx.pro.employeur;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.htmx.pro.R;

import java.util.List;


public class Emp_favori_info extends RecyclerView.Adapter {
    List<Favori> list;
    LayoutInflater inflater;
    Context context;

    public Emp_favori_info(Context context, List<Favori> list){
        this.context=context;
        this.list=list;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View view= inflater.inflate(R.layout.emp_favori_contenu,viewGroup,false);
        View_emp_favori viewEmpFavori=new View_emp_favori(view,context,list);
        return viewEmpFavori;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final View_emp_favori viewEmpFavori=(View_emp_favori)holder;
        viewEmpFavori.T_domaine.setText(list.get(position).getDomaine());
        viewEmpFavori.T_nom.setText(list.get(position).getNom());
        viewEmpFavori.T_prenom.setText(list.get(position).getPrenom());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

/**cette dans le  onBindViewHolder la je lui a fait une activity a la palce de AlertDialog**/
/**
 * final String nom=viewEmpFavori.T_nom.getText().toString().trim();
 final String prenom=viewEmpFavori.T_prenom.getText().toString().trim();
 get_user_form_id_email(nom,prenom);

 LayoutInflater inflater=LayoutInflater.from(Emp_favori.getAppContext());
 final View view=inflater.inflate(R.layout.emp_item_selected,null);
 final AlertDialog.Builder alertBD=new AlertDialog.Builder(Emp_favori.getAppContext());
 alertBD.setView(view);
 btn_call=(Button)view.findViewById(R.id.emp_item_selected_btn_call);
 btn_send=(Button)view.findViewById(R.id.emp_item_selected_btn_send);
 btn_remove=(Button)view.findViewById(R.id.emp_item_selected_btn_delete_candidat);
 tx_email=(TextView)view.findViewById(R.id.emp_item_selected_tx_email);
 ed_objet=(EditText)view.findViewById(R.id.emp_item_selected_edt_sujet);
 ed_sujet=(EditText)view.findViewById(R.id.emp_item_selected_edtt_body);
 ed_from=(EditText)view.findViewById(R.id.emp_item_selected_edt_from);

 btn_send.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
int count=3;
String from,sujet,object;
from=ed_from.getText().toString().trim();
sujet=ed_sujet.getText().toString().trim();
object=ed_objet.getText().toString().trim();
if (!isValideEmail(from)){
ed_from.setError(view.getResources().getString(R.string.email_nn_valide));
count--;
}
if (sujet.equals("")){
ed_sujet.setError(view.getResources().getString(R.string.champ_vide));
count--;
}
if (object.equals("")){
ed_objet.setError(view.getResources().getString(R.string.champ_vide));
}
if (count==3){
Intent emailintent = new Intent(Intent.ACTION_SEND);
emailintent.setData(Uri.parse("mailto:"));
emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
emailintent.putExtra(Intent.EXTRA_SUBJECT, sujet);
emailintent.putExtra(Intent.EXTRA_TEXT, object);
emailintent.setType("message/rfc822");
context.startActivity(Intent.createChooser(emailintent,"Send Email"));
}
}
});
 btn_call.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
System.out.println(num_tel);
Intent callIntent=new Intent(Intent.ACTION_CALL);
callIntent.setData(Uri.parse("tel:"+num_tel));
context.startActivity(callIntent);

}
});
 alertBD.setCancelable(true);
 android.app.AlertDialog dialog =alertBD.create();
 //   dialog.getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
 dialog.show();
 */
