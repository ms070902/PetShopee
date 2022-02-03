package com.example.petshopee.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopee.PetsInfo;
import com.example.petshopee.R;
import com.example.petshopee.RecyclerViewAdapter;
import com.example.petshopee.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private RecyclerViewAdapter petAdapter;
    RecyclerView recyclerView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recyclerViewDogs);
        setUpRecyclerView();
        return v;
    }
    private void setUpRecyclerView() {

        Query query = db.collection("Dogs").orderBy("petName", Query.Direction.ASCENDING);

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