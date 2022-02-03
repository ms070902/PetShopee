package com.example.petshopee;

import android.net.Uri;

import java.util.UUID;

public class PetsInfo {
    public String petImage;
    public String petName;
    public String petBreed;
    public String petDescription;
    public String petCategory;

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }



    String petId = UUID.randomUUID().toString();

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public PetsInfo(){}

    public PetsInfo(String imageResource, String name, String breed, String description, String id){
        petImage = imageResource;
        petName = name;
        petBreed = breed;
        petDescription = description;
        petId = id;
    }

    public String getPetImage() {
        return petImage;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public String getPetDescription() {
        return petDescription;
    }
}
