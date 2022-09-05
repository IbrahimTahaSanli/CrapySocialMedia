package com.tahasanli.koub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tahasanli.koub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences pref;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            startActivity(new Intent(this, MainScreen.class));

        binding.ActivityMainUsername.setText(pref.getString(getString(R.string.PREFUSERNAMEKEY),""));
    }

    public void SignIn(View view) {
        if(binding.ActivityMainUsername.getText().toString().equals("") || binding.ActivityMainPassword.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.ShouldNotBeEmpty), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(
                binding.ActivityMainUsername.getText().toString(),
                binding.ActivityMainPassword.getText().toString()
                ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(binding.ActivityMainRememberMe.isChecked())
                                pref.edit().putString(getString(R.string.PREFUSERNAMEKEY), binding.ActivityMainUsername.getText().toString()).apply();
                            startActivity(new Intent(MainActivity.this, MainScreen.class));
                        }
                        else{
                            Toast.makeText( MainActivity.this, getString(R.string.ActivityMainFail), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }

    public void Register(View view) {
        startActivity(new Intent(this, SignupScreen.class));
    }
}