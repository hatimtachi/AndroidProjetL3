package com.example.htmx.pro;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by htmx on 10/11/16.
 */

/**
 * *
 * *
 *
 * c'est pour savoir est ce que le client a rester conntecter ou pas
 *
 * avec un petit test de connect_or_not(sais dans la base de donnee) '1' c'est un emploi '2' c'est un employeur '0' c'est utilisateur il est deconnecter
 *
 * *
 * *
 * **/

public class Activity_test extends Activity {
    private Use_BD_interne useBdInterne;
    private Button btn_create;
    private Button btn_getI;
    private EditText sais_qui;
    private EditText connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        sais_qui=(EditText)findViewById(R.id.E_saisqui);
        connect =(EditText)findViewById(R.id.E_connect);

        btn_create=(Button)findViewById(R.id.Btn_create);
        btn_getI=(Button)findViewById(R.id.Btn_get);

        useBdInterne =new Use_BD_interne(this);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useBdInterne.open();
                String S_sais_qui = sais_qui.getText().toString().trim();
                String S_connect = connect.getText().toString().trim();

                useBdInterne.U_create(S_sais_qui, S_connect);

                Cursor res = useBdInterne.getAll();
                res.moveToFirst();
                String re1 = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_SAIS_QUI_E));
                String re2 = res.getString(res.getColumnIndex(Base_donnee_interne.COLUMN_CONNECT_OR_NOT_E));
                System.out.println("sais::" + re1 + "\n dsq:: " + re2);
                useBdInterne.close();
            }
        });
        btn_getI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useBdInterne.open();
                useBdInterne.deleteContenuAll();
                useBdInterne.close();
            }
        });

    }
}
