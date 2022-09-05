package com.tahasanli.koub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.type.DateTime;
import com.tahasanli.koub.databinding.AddscreenBinding;


public class AddScreen extends AppCompatActivity {
    private AddscreenBinding binding;

    public static final String IDKey = "INTENTID";

    private Photo currentPhoto;

    private ActivityResultLauncher<Intent> resultIntent;
    private ActivityResultLauncher<String> resultString;

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        binding = AddscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resultIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null) {
                        Uri data = intentFromResult.getData();
                        currentPhoto.photo = data;
                        changePhoto(data);
                        }
                    }

                }
            }
        );

        resultString = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultIntent.launch(galeryIntent);
                }
                else{
                    Toast.makeText(AddScreen.this, R.string.AddScreenPermissionNeed, Toast.LENGTH_LONG).show();
                }
            }
        });

        currentPhoto = new Photo();
    }

    public void changePhoto(Uri uri){
        binding.AddScreenSelectText.setEnabled(false);
        binding.AddScreenImage.bringToFront();
        binding.AddScreenImage.setImageURI(uri);
    }

    public void selectPhoto(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, R.string.AddScreenPermissionNeed,Snackbar.LENGTH_INDEFINITE).setAction(R.string.AddScreenGrantPermission, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultString.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                resultString.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            return;
        }
        else{
            Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultIntent.launch(galeryIntent);
        }


    }



    public void addPhoto(View view) {
        AddScreen currentContext = this;
        if(currentPhoto.photo == null) {
            Toast.makeText(this, getString(R.string.AddScreenNoPhoto), Toast.LENGTH_SHORT).show();
            return;
        }

        currentPhoto.name = binding.AddScreenName.getText().toString();
        currentPhoto.date = String.valueOf((Long)System.currentTimeMillis()/1000);

        currentPhoto.InsertPhoto(this, new UploadSuccess() {
            @Override
            public void onSuccess() {
                currentContext.finish();
            }
        });

        this.finish();
    }
}
