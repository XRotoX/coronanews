package com.inpt.coronapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {
    EditText etCin2, etPass;
    TextView tvRegister, tvOublie;
    ImageButton btConnect;
    public String cin, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etCin2=findViewById(R.id.etCin2);
        etPass=findViewById(R.id.etPass);
        tvRegister=findViewById(R.id.tvRegister);
        tvOublie=findViewById(R.id.tvOublie);
        btConnect=findViewById(R.id.btConnect);



        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cin = etCin2.getText().toString();
                pass = etPass.getText().toString();
                if (cin.isEmpty() && pass.isEmpty() ){
                    Toast.makeText(LogInActivity.this,"Il faut remplir tout les champs!",Toast.LENGTH_SHORT).show();
                }
                else if (cin.isEmpty()){
                    Toast.makeText(LogInActivity.this,"Entrer votre CIN!",Toast.LENGTH_SHORT).show();
                }
                else if (pass.isEmpty()){
                    Toast.makeText(LogInActivity.this,"Entrer votre password!",Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("Client").document(cin);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()){
                                    String p = doc.getData().get("pass").toString();

                                    if ( pass.equals(p) ){
                                        Intent i = new Intent(LogInActivity.this,QrCodeActivity.class);
                                        i.putExtra("cin", cin);
                                        startActivity(i);
                                    }
                                    else {
                                        Toast.makeText(LogInActivity.this,"Mot de passe est incorrecte!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(LogInActivity.this,"CIN est invalide!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });

    }

    public void goOublie(View view) {
        Intent intent = new Intent(this,MdpOublieActivity.class );
        startActivity(intent);
    }

    public void goRegister(View view) {
        Intent intent = new Intent(this,MainActivity2.class );
        startActivity(intent);
    }

}
