package com.team29.cse110.team29dejaphoto;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Noah on 5/31/2017.
 */

public class FirebasePhotosHelper {

    private PhotoLoader photoLoader = new DejaPhotoLoader();

    //Firebase reference for accessing stored media
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    //Firebase reference for getting user information
    private FirebaseDatabase database;
    private DatabaseReference myFirebaseRef;
    private FirebaseUser user;

    //For checking if upload was successful or not
    private UploadTask uploadTask;


    public void upload(DejaPhoto photo)
    {
        //Gets current User
        database = FirebaseDatabase.getInstance();
        myFirebaseRef = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("Loader", "Current User Email: "+ user.getEmail());


        //Sets reference to current user
        StorageReference userRef = storageRef.child(
                user.getEmail().substring(0, user.getEmail().indexOf('@')));

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpg")
                .setCustomMetadata("Karma", "0").build();


        //Creates new child reference of current user for photo and uploads photo
            StorageReference photoRef = userRef.child(photo.getPhotoUri().getLastPathSegment());
            photoRef.updateMetadata(metadata);
            uploadTask = photoRef.putFile(photo.getPhotoUri(), metadata);

    }
}
