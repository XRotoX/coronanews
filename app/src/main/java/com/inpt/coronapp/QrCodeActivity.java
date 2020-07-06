package com.inpt.coronapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QrCodeActivity extends AppCompatActivity {
    Button btDec;
    TextView textView2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        textView2=findViewById(R.id.textView2);
        btDec=findViewById(R.id.btDec);
        imageView = findViewById(R.id.imageView);

        String cin = getIntent().getStringExtra("cin");
        if (cin != null) {
            textView2.setVisibility(View.VISIBLE);
            textView2.setText("Voilà votre Code Qr enregistre le!");
            new ImageDownloaderTask(imageView).execute("https://api.qrserver.com/v1/create-qr-code/?size=1000x1000&data="+cin);
                }




        btDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(QrCodeActivity.this,MainActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
    }
}
