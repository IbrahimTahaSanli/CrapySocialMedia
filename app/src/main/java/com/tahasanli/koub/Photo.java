package com.tahasanli.koub;

import android.app.NativeActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Photo {
    public int ID;
    public Uri photo;
    public String name;
    public String user;
    public String date;

    public Photo(){}
    public Photo(Uri p, String n, String u, String d){
        this.photo = p;
        this.name = n;
        this.user = u;
        this.date = d;
    }

    public static ArrayList<Photo> GetAllPhotos(){
        ArrayList<Photo> retArr = new ArrayList<Photo>();

        return retArr;

    }

    public String getImageAbsPath(String fn){
        return "/images/" + fn + ".jpg";
    }

    public String getImageAbsPath(String fn, String fe) {
        return "/images/" + fn + "." + fe;
    }

    public void InsertPhoto(Context cont, UploadSuccess suc){
        Photo currentObj = this;
        this.user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        UUID uuid = UUID.randomUUID();
        String filePath = getImageAbsPath(uuid.toString(), "jpg"); //this.photo.toString().substring(this.photo.toString().lastIndexOf(".") + 1)

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child(filePath).putFile(this.photo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                HashMap<String, Object> obj = new HashMap<String, Object>();

                                obj.put("User", currentObj.user);
                                obj.put("Name", currentObj.name);
                                obj.put("Photo", task.getResult().toString());
                                obj.put("Date", FieldValue.serverTimestamp());

                                FirebaseFirestore.getInstance().collection("db").add(obj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            suc.onSuccess();
                                        }
                                        else{
                                            Toast.makeText(cont, cont.getText(R.string.AddScreenUploadFailed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                task.getResult().toString();
                            }
                            else{
                                Toast.makeText(cont, cont.getText(R.string.AddScreenUploadFailed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(cont, cont.getText(R.string.AddScreenUploadFailed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap MakeSmallerBitmaps(Bitmap bm, int MaximumEdge){
        int width = bm.getWidth();
        int height = bm.getHeight();

        float AspectRatio = (float)width / (float)height;

        if(AspectRatio > 1){
            width = MaximumEdge;
            height = (int)(MaximumEdge / AspectRatio);
        }
        else {
            height = MaximumEdge;
            width = (int)(MaximumEdge * AspectRatio);
        }

        return bm.createScaledBitmap(bm, width, height, true);
    }
}
