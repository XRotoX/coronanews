package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeMDPActivity extends AppCompatActivity {
    private EditText etOMot, etOMot2;
    private Button btOValiderMDP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_m_d_p);

        etOMot=findViewById(R.id.etOMot);
        etOMot2=findViewById(R.id.etOMot2);
        btOValiderMDP=findViewById(R.id.btOValiderMDP);

        btOValiderMDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdp = etOMot.getText().toString().trim();
                String mdp_c = etOMot2.getText().toString().trim();

                if (!mdp_c.equals(mdp)) {
                    etOMot2.setError("Le mot de passe confirmé est incorrect.");
                    etOMot2.requestFocus();
                } else if (mdp.isEmpty() || mdp_c.isEmpty() || mdp.length()<6) {
                    etOMot2.setError("Entrer un mot de passe d'au moins six caractères.");
                    etOMot2.requestFocus();
                    etOMot.setError("Entrer un mot de passe d'au moins six caractères.");
                    etOMot.requestFocus();
                }

                String cin = getIntent().getStringExtra("cin");
                Intent intent = new Intent(ChangeMDPActivity.this, Activity.class);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference washingtonRef = db.collection("Client").document(cin);
                washingtonRef.update("pass",mdp);
                Toast.makeText(ChangeMDPActivity.this,"Mot de passe Changé! Reconnectez-vous!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }
}
