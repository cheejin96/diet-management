package com.example.cheejin.fyp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheejin.fyp.PostPackage.Post;
import com.example.cheejin.fyp.PostPackage.PostAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    DatabaseReference databaseStories;
    StorageReference storageImages;
    private RecyclerView recyclerView;
    private List<Post> posts = new ArrayList<>();
    private List<Post> latestPosts = new ArrayList<>();
    private PostAdapter pa;
    private FloatingActionButton fab;
    private View view;
    private String id;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri selectedImageUrl, captureImageUrl;
    TextView ownername, username, statusPost;
    EditText statusAdd;
    Button addPostBtn;
    ImageButton addImageBtn;
    ImageView imageView;
    User currentUser;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    ProgressBar progressBar;

    public ChatFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        storageImages = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fypdatabase-3834c.appspot.com");
        databaseStories = FirebaseDatabase.getInstance().getReference("stories");
        id = databaseStories.push().getKey();

        currentUser = (User) getArguments().getSerializable("currentUserBundle");

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.strView);
        fab = (FloatingActionButton) view.findViewById(R.id.addStrBtn);
        progressBar = view.findViewById(R.id.progressBar);

        final ChatFragment context = ChatFragment.this;

        databaseStories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(posts!= null || latestPosts != null){
                    posts.clear();
                    latestPosts.clear();
                }
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot c : children) {
                    posts.add(c.getValue(Post.class));
                }

                for(int i = posts.size() -1; i >=0 ;i--){
                    latestPosts.add(posts.get(i));
                }


                LinearLayoutManager lim = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(lim);
                pa = new PostAdapter(latestPosts, context);
                recyclerView.setAdapter(pa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View addStrView = getLayoutInflater().inflate(R.layout.activity_story, null);
                ownername = (TextView) addStrView.findViewById(R.id.userName);
                statusAdd = (EditText) addStrView.findViewById(R.id.statusText);
                addPostBtn = (Button) addStrView.findViewById(R.id.postBtn);
                addImageBtn = (ImageButton) addStrView.findViewById(R.id.imageBtn);
                imageView = (ImageView) addStrView.findViewById(R.id.imageView);

                if(currentUser != null) {
                    ownername.setText(currentUser.getUsername().toString());
                }

                builder.setView(addStrView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                addImageBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        SelectImage();
                    }
                });

                addPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        AddPost();
                        dialog.dismiss();
                    }
                });


            }
        });

        return view;
    }

    public void SelectImage(){
        final CharSequence[] items = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder imageBuilder = new AlertDialog.Builder(getContext());
        imageBuilder.setTitle("Add Image");
        imageBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")){

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);

                }else if (items[i].equals("Gallery")){

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(intent,SELECT_FILE);

                }else if (items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });

        imageBuilder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE){

            selectedImageUrl = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUrl);
                //Picasso.with(this.getContext()).load(selectedImageUrl).into(imageView);
                imageView.setImageURI(selectedImageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA){
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bmp, null, null);
            captureImageUrl = Uri.parse(path);
            imageView.setImageURI(captureImageUrl);
        }
    }

    public void AddPost(){
        if(selectedImageUrl != null || captureImageUrl != null){
            if (captureImageUrl != null){

                final StorageReference childRef = storageImages.child("images" + UUID.randomUUID().toString());
                //Start upload image
                final UploadTask uploadTask = childRef.putFile(captureImageUrl);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return childRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Uri downloadUri = task.getResult();
                            Toast.makeText(getContext(), "Successful upload", Toast.LENGTH_SHORT).show();
                            Post post = new Post(currentUser.getUsername().toString(), statusAdd.getText().toString(), downloadUri.toString());
                            String uploadId = databaseStories.push().getKey();
                            databaseStories.child(uploadId).setValue(post);
                        }else{
                            Toast.makeText(getContext(), "Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                //Method 2
                final StorageReference childRef = storageImages.child("images" + UUID.randomUUID().toString());
                //Start upload image
                final UploadTask uploadTask = childRef.putFile(selectedImageUrl);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return childRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Uri downloadUri = task.getResult();
                            Toast.makeText(getContext(), "Successful upload", Toast.LENGTH_SHORT).show();
                            Post post = new Post(currentUser.getUsername().toString(), statusAdd.getText().toString(), downloadUri.toString());
                            String uploadId = databaseStories.push().getKey();
                            databaseStories.child(uploadId).setValue(post);
                        }else{
                            Toast.makeText(getContext(), "Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(),"Successful upload",Toast.LENGTH_SHORT).show();
            Post postWithoutImage = new Post(currentUser.getUsername().toString(),statusAdd.getText().toString());
            String uploadIdWithoutImage = databaseStories.push().getKey();
            databaseStories.child(uploadIdWithoutImage).setValue(postWithoutImage);
        }
    }
}
