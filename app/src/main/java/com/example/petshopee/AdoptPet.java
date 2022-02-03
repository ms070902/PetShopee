package com.example.petshopee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdoptPet extends AppCompatActivity {
    public Button petAdopt;

    String category;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_pet);
        Intent intent = getIntent();
        intent.getExtras();
        String name = getIntent().getStringExtra("name");
        String breed = getIntent().getStringExtra("breed");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");
        String id = getIntent().getStringExtra("id");
        category = getIntent().getStringExtra("category");

        TextView petName = findViewById(R.id.petName);
        TextView petBreed = findViewById(R.id.petBreed);
        TextView petDescription = findViewById(R.id.petDescription);
        ImageView petImage = findViewById(R.id.petImage);

        petName.setText(name);
        petBreed.setText(breed);
        petDescription.setText(description);
        Picasso.get().load(image).into(petImage);




        petAdopt = findViewById(R.id.btn_adopt);
        petAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(id,category);
            }
        });

    }

    private void deleteData(String id, String category) {

        db.collection(category).document(id).delete()

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdoptPet.this, "Thank you for adopting a life. " +
                                        "You will shortly receive a mail from PetShopee for further adoption procedure.",
                                Toast.LENGTH_LONG).show();
                        Intent dataDeleted = new Intent(AdoptPet.this, MainActivity.class);
                        startActivity(dataDeleted);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
