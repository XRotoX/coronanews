package com.inpt.coronapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private EditText etCode;
    Button btValider3;
    private ProgressBar progressBar;
    private  String verificationId;
    private FirebaseAuth mAuth;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        etCode=findViewById(R.id.etCode);
        btValider3=findViewById(R.id.btValider3);

        mAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        phonenumber= getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        btValider3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString().trim();

                if (code.isEmpty() || code.length()<6){
                    etCode.setError("Code erroné!");
                    etCode.requestFocus();
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
                            Intent intent = new Intent(VerifyPhoneActivity.this,QrCodeActivity.class );
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK );
                            //intent.putExtra("id", verificationId.substring(0,10)); /////////======>+>+>+>>+>+>+>+>+
                            String cin = getIntent().getStringExtra("cin");
                            String mdp = getIntent().getStringExtra("pass");
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference washingtonRef = db.collection("Client").document(cin);
                            washingtonRef.update("tel", phonenumber);
                            washingtonRef.update("pass",mdp);
                            intent.putExtra("cin", cin);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(VerifyPhoneActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String num){
        progressBar.setVisibility(View.VISIBLE);
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
                etCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


}
