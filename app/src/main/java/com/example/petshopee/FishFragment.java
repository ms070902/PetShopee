package com.example.petshopee;

import android.app.ProgressDialog;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FishFragment extends Fragment {
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private RecyclerViewAdapter petAdapter;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fish, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewFish);
        setUpRecyclerView();

        return v;
    }

    private void setUpRecyclerView() {

        Query query = db.collection("Fishes").orderBy("petName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<PetsInfo> options = new FirestoreRecyclerOptions.Builder<PetsInfo>()
                .setQuery(query,PetsInfo.class)
                .build();

        petAdapter = new RecyclerViewAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(petAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        petAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        petAdapter.stopListening();
    }
}