package com.example.petshopee;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends FirestoreRecyclerAdapter<PetsInfo,RecyclerViewAdapter.RecyclerViewHolder> {
    public FirestoreRecyclerOptions<PetsInfo> mList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;





    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView petImage;
        public TextView petName;
        public TextView petBreed;
        public LinearLayout parentLayout;



        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.card_Image);
            petName = itemView.findViewById(R.id.card_name);
            petBreed = itemView.findViewById(R.id.card_breed);
            parentLayout = itemView.findViewById(R.id.parentLinear);


        }



    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v);
        return recyclerViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull PetsInfo model) {

        holder.petName.setText(model.getPetName());
        holder.petBreed.setText(model.getPetBreed());
        Picasso.get().load(model.getPetImage()).into(holder.petImage);
         holder.parentLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(holder.parentLayout.getContext(),AdoptPet.class);
                 intent.putExtra("name",model.getPetName());
                 intent.putExtra("breed",model.getPetBreed());
                 intent.putExtra("description",model.getPetDescription());
                 intent.putExtra("image",model.getPetImage());
                 intent.putExtra("category",model.getPetCategory());
                 intent.putExtra("id", getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getId());

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                 holder.parentLayout.getContext().startActivity(intent);

             }
         });

    }

    public RecyclerViewAdapter(FirestoreRecyclerOptions<PetsInfo> petList){
        super(petList);
        mList = petList;

    }
}
