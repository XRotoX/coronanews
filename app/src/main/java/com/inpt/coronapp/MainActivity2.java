package com.inpt.coronapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity2 extends AppCompatActivity {
    EditText etCIN, etNom, etPrenom, etLieu, etJour, etMois, etAnnee;
    Button btValider;
    String cin, nom, prenom, lieu, jour, mois, annee;
    TextView tvGoLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //FirebaseApp.InitializeApp(MainActivity.this);
        FirebaseApp.initializeApp(MainActivity2.this);

        etCIN=findViewById(R.id.etCin);
        etNom=findViewById(R.id.etNom);
        etPrenom=findViewById(R.id.etPrenom);
        etLieu=findViewById(R.id.etLieu);
        etJour=findViewById(R.id.etJour);
        etMois=findViewById(R.id.etMois);
        etAnnee=findViewById(R.id.etAnnee);
        btValider=findViewById(R.id.btValider);
        tvGoLogIn=findViewById(R.id.tvGoLogIn);



        btValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cin = etCIN.getText().toString();
                nom = etNom.getText().toString();
                prenom = etPrenom.getText().toString();
                lieu = etLieu.getText().toString();
                jour = etJour.getText().toString();
                mois = etMois.getText().toString();
                annee = etAnnee.getText().toString();


                if(jour.isEmpty() || mois.isEmpty() || annee.isEmpty() || cin.isEmpty() || nom.isEmpty() || prenom.isEmpty() || lieu.isEmpty() ){
                    Toast.makeText(MainActivity2.this,"Il faut remplir tout les champs",Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("Client").document(cin);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()){
                                    String n = doc.getData().get("nom").toString();
                                    String p = doc.getData().get("prenom").toString();
                                    String ldn = doc.getData().get("lieu_de_naiss").toString();
                                    int j= Integer.parseInt(doc.getData().get("jour").toString()) ;
                                    int m= Integer.parseInt(doc.getData().get("mois").toString());
                                    int a= Integer.parseInt(doc.getData().get("annee").toString());

                                    if ( n.equals(nom) && p.equals(prenom) && ldn.equals(lieu) &&
                                            (j == Integer.parseInt(jour)) && m==Integer.parseInt(mois) && a==Integer.parseInt(annee) ){
                                        Intent i = new Intent(MainActivity2.this,PhoneActivity.class);
                                        i.putExtra("cin",cin );
                                        startActivity(i);

                                    }
                                    else {
                                        Toast.makeText(MainActivity2.this,"Les Informations entrées sont incorrectes",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity2.this,"CIN entrée est introuvable!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });

    }

    //L'User va passer directement à l'activité "QrCode" s'il est déjà connecté.
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this,QrCodeActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
        }
    }


    public void goLogin(View view) {
        Intent intent = new Intent(this,LogInActivity.class );
        startActivity(intent);
    }
}
