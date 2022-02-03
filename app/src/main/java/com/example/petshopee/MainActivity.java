package com.example.petshopee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopee.ui.gallery.GalleryFragment;
import com.example.petshopee.ui.home.HomeFragment;
import com.example.petshopee.ui.slideshow.SlideshowFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petshopee.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity   {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    TextView userEmail;
    TextView userName;
    ImageView userProfile;
    Uri profileUri;
    DrawerLayout drawer;
    Bitmap bitmap;
    private final int REQ = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.fishFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationView nView = findViewById(R.id.nav_view);

        View v = nView.getHeaderView(0);
        int i = getIntent().getIntExtra("profile",3);

        userEmail = v.findViewById(R.id.userEmail);
        userName = v.findViewById(R.id.userName);
        userProfile = v.findViewById(R.id.userProfile);



        if(i==1){
            SharedPreferences preferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
            String email = preferences.getString("userEmail","");
            String name = preferences.getString("userName","");
            String profile = preferences.getString("userProfile","ic_menu_camera");
            userEmail.setText(email);
            userName.setText(name);
            Picasso.get().load(profile).into(userProfile);
        }
        else{
            SharedPreferences prefs = getSharedPreferences("emailId",MODE_PRIVATE);
            String uEmail = prefs.getString("email","");
            userEmail.setText(uEmail);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.actionLogOut:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Log Out?")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getApplicationContext(), "Logged out successfully.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,RegisterUserActivity.class));
                                SharedPreferences.Editor editor = getApplicationContext()
                                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
                                        .edit();
                                editor.clear();
                                editor.apply();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();

                break;

            case R.id.actionProfileImage:
                changeProfileImage();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this,RegisterUserActivity.class));
        }

    }


    private void changeProfileImage(){
        Intent pickImage = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(pickImage,REQ);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK){
            profileUri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),profileUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            userProfile.setImageBitmap(bitmap);
            Log.d("pic", "onActivityResult: image set");

        }
    }




}