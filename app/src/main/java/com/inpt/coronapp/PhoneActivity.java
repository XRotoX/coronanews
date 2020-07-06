package com.inpt.coronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneActivity extends AppCompatActivity {
    EditText etPhone, etMot, etMot2;
    Button btValider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        etPhone = findViewById(R.id.etPhone);
        etMot=findViewById(R.id.etMot);
        etMot2=findViewById(R.id.etMot2);
        btValider2=findViewById(R.id.btValider2);

        btValider2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etPhone.getText().toString().trim();
                String mdp = etMot.getText().toString().trim();
                String mdp_c = etMot2.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length()!=13){
                    etPhone.setError("Entrer un numéro de téléphone valide! (+2126########)");
                    etPhone.requestFocus();
                    return;
                }
                else if (!mdp_c.equals(mdp)) {
                    etMot2.setError("Le mot de passe confirmé est incorrect.");
                    etMot2.requestFocus();
                    return;
                } else if (mdp.isEmpty() || mdp_c.isEmpty() || mdp.length()<6) {
                    etMot2.setError("Entrer un mot de passe d'au moins six caractères.");
                    etMot2.requestFocus();
                    etMot.setError("Entrer un mot de passe d'au moins six caractères.");
                    etMot.requestFocus();
                    return;
                }

                String cin = getIntent().getStringExtra("cin");
                Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", mobile);
                intent.putExtra("cin", cin);
                intent.putExtra("pass", mdp);
                startActivity(intent);
            }
        });
    }

}