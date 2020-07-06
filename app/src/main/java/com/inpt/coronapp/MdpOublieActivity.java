package com.inpt.coronapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MdpOublieActivity extends AppCompatActivity {

    private EditText etOCode, etOPhone, etOCin;
    private TextView tvOPhone,tvOMot;
    Button btOValider2, btOValiderNum;
    private ProgressBar progressBar2;
    private  String verificationId;
    private FirebaseAuth mAuth;
    String phonenumber, cin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdp_oublie);

        tvOPhone=findViewById(R.id.tvOPhone);
        tvOMot=findViewById(R.id.tvOMot);

        etOCode=findViewById(R.id.etOCode);
        etOPhone = findViewById(R.id.etOPhone);
        etOCin=findViewById(R.id.etOCin);

        btOValider2=findViewById(R.id.btOValider2);
        btOValiderNum=findViewById(R.id.btOValiderNum);


        mAuth= FirebaseAuth.getInstance();
        progressBar2=findViewById(R.id.progressBar2);

        btOValiderNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber=etOPhone.getText().toString().trim();
                cin=etOCin.getText().toString().trim();
                if (phonenumber.isEmpty() || phonenumber.length()<13) {
                    etOPhone.setError("Numéro incorect! (+2126########)");
                    etOPhone.requestFocus();
                    return;
                } else if (cin.isEmpty()) {
                    etOCin.setError("Entrer votre CIN!");
                    etOCin.requestFocus();
                    return;
                }

                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("Client").document(cin);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()){
                                    String t = doc.getData().get("tel").toString();

                                    if ( t.equals(phonenumber)  ){
                                        tvOPhone.setVisibility(View.INVISIBLE);
                                        etOPhone.setVisibility(View.INVISIBLE);
                                        etOCin.setVisibility(View.INVISIBLE);
                                        btOValiderNum.setVisibility(View.INVISIBLE);

                                        sendVerificationCode(phonenumber);

                                        progressBar2.setVisibility(View.VISIBLE);
                                        etOCode.setVisibility(View.VISIBLE);
                                        btOValider2.setVisibility(View.VISIBLE);

                                    }
                                    else {
                                        Toast.makeText(MdpOublieActivity.this,"Le numéro entré ne correspond pas à votre CIN !!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MdpOublieActivity.this,"CIN entrée est invalide!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            }
        });

        btOValider2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etOCode.getText().toString().trim();

                if (code.isEmpty() || code.length()<6){
                    etOCode.setError("Code erroné!");
                    etOCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private  void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(MdpOublieActivity.this, ChangeMDPActivity.class);
                            intent.putExtra("phonenumber", phonenumber);
                            intent.putExtra("cin", cin);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(MdpOublieActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private void sendVerificationCode(String num){
        progressBar2.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,        //Le num à verifier
                60,                 // Le durée du timeout
                TimeUnit.SECONDS,   // L'unité du timeout
                this,               // Activité
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode(); // Detect le code automatiquement
            if (code != null) {
                etOCode.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(MdpOublieActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
