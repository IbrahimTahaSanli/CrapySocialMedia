package com.tahasanli.koub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tahasanli.koub.databinding.SignupScreenBinding;


public class SignupScreen extends AppCompatActivity {
    private SignupScreenBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        binding = SignupScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            this.finish();

    }

    public void Register(View view) {
        if(binding.SignupScreenUsername.getText().toString().equals("") || binding.SignupScreenPassword.getText().toString().equals("")){
            Toast.makeText(this, getString(R.string.ShouldNotBeEmpty), Toast.LENGTH_SHORT).show();
            return;
        }

        if(binding.SignupScreenPassword.getText().toString().equals(binding.SignupScreenVerifyPass.getText().toString()))
            mAuth.createUserWithEmailAndPassword(
                    binding.SignupScreenUsername.getText().toString(),
                    binding.SignupScreenPassword.getText().toString()
            ).addOnCompleteListener(
                    this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SignupScreen.this.finish();
                            }
                            else{
                                Toast.makeText(SignupScreen.this,getString(R.string.SignupScreenFailSignup), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
    }
}
