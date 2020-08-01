package com.example.pingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtTxtFriendUsername;
    private TextView btnSearchFriend, txtFriendUsername;
    private ImageView imgFriendProfile;
    private LinearLayout containerUserFound;
    private Button btnAddFriend;
    private boolean isFriend = false;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    String friendUsername, uidFound = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        edtTxtFriendUsername = findViewById(R.id.edtTxtFriendUsername);
        btnSearchFriend = findViewById(R.id.btnSearchFriend);
        imgFriendProfile = findViewById(R.id.imgFriendProfile);
        txtFriendUsername = findViewById(R.id.txtFriendUsername);
        containerUserFound = findViewById(R.id.containerUserFound);
        btnAddFriend = findViewById(R.id.btnAddFriend);

        btnSearchFriend.setOnClickListener(this);
        btnAddFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearchFriend:
                searchFriend();
                break;
            case R.id.btnAddFriend:
                addFriend();
                break;
        }
    }

    public void searchFriend(){
        containerUserFound.setVisibility(View.GONE); //reset

        friendUsername = edtTxtFriendUsername.getText().toString();

        String userEmail = mAuth.getCurrentUser().getEmail();
        String username = userEmail.substring(0, userEmail.length()-10);

        if(username.equals(friendUsername)){
            Toast.makeText(this, "Please enter friend username", Toast.LENGTH_SHORT).show();
            return;
        }
        //check is already friend
        mRef.child("user").child(username).child("friend")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot singleSnapshot: snapshot.getChildren()){
                            if(singleSnapshot.getKey().toString().equals(friendUsername)){
                                Toast.makeText(AddFriendActivity.this, "Already friend", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //add friend
                        mRef.child("user").child(friendUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Toast.makeText(AddFriendActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                if(!snapshot.exists()){
                                    Toast.makeText(AddFriendActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                txtFriendUsername.setText(friendUsername);
                                if(!snapshot.child("imgUrl").equals("default")){
                                    // TODO: change profile pic
                                }
                                containerUserFound.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    public void addFriend(){
        String userEmail = mAuth.getCurrentUser().getEmail();
        String username = userEmail.substring(0, userEmail.length()-10);
        mRef.child("user").child(username).child("friend").child(friendUsername).setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(AddFriendActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddFriendActivity.this, "Cannot add", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}