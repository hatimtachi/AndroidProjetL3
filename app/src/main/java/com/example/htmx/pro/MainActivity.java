package com.example.htmx.pro;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.htmx.pro.emploi.Emploi;
import com.example.htmx.pro.emploi.Main_Emploi;
import com.example.htmx.pro.employeur.Main_Employeur;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private Use_BD_interne useBdInterne;
    Button btn_Emploi;
    Button btn_Employeur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Emploi=(Button) findViewById(R.id.Btn_emploi);
        btn_Employeur=(Button) findViewById(R.id.Btn_employeur);
        useBdInterne =new Use_BD_interne(this);

        btn_Employeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(MainActivity.this,Main_Employeur.class);
                startActivity(intent);
            }
        });

        btn_Emploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String re1,re2="";
                useBdInterne.open();
                Cursor res = useBdInterne.getAll();

                if(useBdInterne.getAllBoolean()){
                    intent=new Intent(MainActivity.this,Emploi.class);
                    startActivity(intent);
                }
                else {
                    res.moveToFirst();
                    re1 = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_SAIS_QUI_E));
                    re2 = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_CONNECT_OR_NOT_E));
                    System.out.println("sais::" + re1 + "\n dsq:: " + re2);
                    useBdInterne.close();
                }
                if (re2.equals("1")){
                    intent =new Intent(MainActivity.this,Main_Emploi.class);
                    startActivity(intent);
                }
                else {
                 intent=new Intent(MainActivity.this,Emploi.class);
                  startActivity(intent);
                }
            }
        });
    }

}
