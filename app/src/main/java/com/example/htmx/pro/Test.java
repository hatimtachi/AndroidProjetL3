package com.example.htmx.pro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * Created by htmx on 16/11/16.
 */

public class Test extends Activity {
    Button btn_image;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private static final int GALLERY_INTENT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        storageReference= FirebaseStorage.getInstance().getReference();
        btn_image=(Button) findViewById(R.id.button2);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                String[] to={"hatim_neneo@live.fr"};
                intent.putExtra(Intent.EXTRA_EMAIL,to);
                intent.putExtra(Intent.EXTRA_SUBJECT,"fdsffsdfd");
                intent.putExtra(Intent.EXTRA_TEXT,"lmfdlfmsdùmflsdfùms");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Send Email"));
            }
        });

    }
}
