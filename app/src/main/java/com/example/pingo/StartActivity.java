package com.example.pingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabStart;
    private MaterialButton btnSignIn, btnRegister;
    private FirebaseAuth mAuth;
    private TextInputEditText edtTxtEmail, edtTxtPassword, edtTxtUsername;
    private TextInputLayout containerUsername;
    private DatabaseReference mRef;
    public static String currentUsername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        containerUsername = findViewById(R.id.containerUsername);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtUsername = findViewById(R.id.edtTxtUsername);
        tabStart = (TabLayout) findViewById(R.id.tabStart);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        tabStart.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int currentPage = tab.getPosition();
                        if (currentPage == 0) {
                            btnSignIn.setVisibility(View.VISIBLE);
                            btnRegister.setVisibility(View.GONE);
                        } else {
                            btnRegister.setVisibility(View.VISIBLE);
                            btnSignIn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn:
                signIn(edtTxtUsername.getText().toString(), edtTxtPassword.getText().toString());
                break;
            case R.id.btnRegister:
                createAccount(edtTxtUsername.getText().toString(), edtTxtPassword.getText().toString());
                break;
            default:
                break;

        }
    }

    private boolean validateForm(String email, String password){
        //Check that email and password are not blank
        return !(email.length()==0 || password.length() == 0);
    }

    private void updateUI(){
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        StartActivity.this.startActivity(intent);
    }

    private void signIn(String username, String password){
        if (!validateForm(username, password)) {
            Toast.makeText(this, "Username and Password fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(username+"@pingo.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI();

                        } else {
                            Toast.makeText(StartActivity.this, "Username and password do not match",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount(final String username , final String password) {
        if (!validateForm(username, password)) {
            Toast.makeText(this, "Username and password fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(username+"@pingo.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mRef.child("user").child(username).child("imgUrl").setValue("default").addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                            updateUI();
                                    }
                                }
                            );

                        } else {
                            Toast.makeText(StartActivity.this, "Username is already taken",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }
    }
}