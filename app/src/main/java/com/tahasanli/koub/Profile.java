package com.tahasanli.koub;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tahasanli.koub.databinding.MainScreenBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {
    public static Profile instance;
    private MainScreenBinding binding;

    private FirebaseAuth mAuth;

    public ArrayList<Photo> photos;

    public MainScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        binding = MainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        photos = new ArrayList<Photo>();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
            this.finish();

        binding.MainScreenRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainScreenAdapter();

        binding.MainScreenRecycler.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("db").whereEqualTo("User", FirebaseAuth.getInstance().getCurrentUser().getEmail()).orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(Profile.this, getString(R.string.MainScreenLoadError), Toast.LENGTH_LONG).show();
                    return;
                }
                if(value != null){

                    for(DocumentSnapshot snap : value.getDocuments() ){
                        Map<String, Object> data = snap.getData();

                        Photo tmp = new Photo();
                        tmp.photo = Uri.parse((String)data.get("Photo"));
                        tmp.name = (String) data.get("Name");
                        tmp.user = (String) data.get("User");
                        photos.add(tmp);
                    }
                }

                adapter.notifyDataSetChanged();


            }
        });
    }

    public void LogOut(View view) {
        mAuth.signOut();
        this.finish();
    }

    public void Share(View view) {
        startActivity(new Intent(this, AddScreen.class));
    }
}
