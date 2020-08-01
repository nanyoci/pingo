package com.example.pingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ChatActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private RecyclerView chatRecView;
    private ChatRecViewAdapter chatAdapter;
    private TextView txtChatFriend;
    private TextInputEditText edtTxtChatMessage;
    private ImageButton btnSendChat;
    private String chatId;
    private int unreadCount;
    private final ArrayList<Chat> chats = new ArrayList<>();
    private String friendUsername, myUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar chat_toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(chat_toolbar);

        txtChatFriend = findViewById(R.id.txtChatFriend);
        chatRecView = findViewById(R.id.chatRecView);
        edtTxtChatMessage = findViewById(R.id.edtTxtChatMessage);
        btnSendChat = findViewById(R.id.btnSendChat);

        Intent intent = getIntent();
        friendUsername = intent.getStringExtra("friendUsername");
        txtChatFriend.setText(friendUsername);


        //compare 2 usernames to get chatId
        String myUserEmail = mAuth.getCurrentUser().getEmail();
        myUsername = myUserEmail.substring(0, myUserEmail.length()-10);

        if(myUsername.compareTo(friendUsername) <0){
            chatId = myUsername +'-'+ friendUsername;
        }
        else{
            chatId = friendUsername + '-' + myUsername;
        }

        chatAdapter = new ChatRecViewAdapter(myUsername);
        chatRecView.setAdapter(chatAdapter);
        chatRecView.setLayoutManager(new LinearLayoutManager(this));
        chatRecView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                chatRecView.scrollToPosition(chats.size()-1);
            }
        });


        //retrieve chats
        mRef.child("chat").child(chatId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chats.clear();
                        if(snapshot.exists()){
                            Log.d("CHAT ACTIVITY", "logging7\n"+snapshot.toString());
                            for(DataSnapshot snap: snapshot.getChildren()){
                                String message = snap.child("message").getValue().toString();
                                String sender = snap.child("sender").getValue().toString();
                                String receiver = snap.child("receiver").getValue().toString();
                                chats.add(new Chat(sender, receiver, message));
                            }

                            // set unread = 0
                            mRef.child("user").child(myUsername).child("chat").child(chatId).child("unread").setValue(0);
                        }
                        chatAdapter.setChats(chats);
                        if(chats.size()>0) {
                            chatRecView.scrollToPosition(chats.size() - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        //handle sending chat messages
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtTxtChatMessage.getText().toString();
                String sender = myUsername;
                String receiver = friendUsername;
                final Chat chat = new Chat(sender, receiver, message);
                //set unread += 1
                mRef.child("user").child(friendUsername).child("chat").child(chatId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    Log.d("CHAT ACTIVITY", friendUsername+" snapshot exists\n"+snapshot.toString());
                                    unreadCount = Integer.parseInt(snapshot.child("unread").getValue().toString());
                                    mRef.child("user").child(friendUsername).child("chat").child(chatId).child("unread").setValue(unreadCount+1)
                                            .addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            mRef.child("chat").child(chatId).child(""+chats.size()).setValue(chat);
                                                        }
                                                    }
                                            );;
                                }
                                else{
                                    FirstTimeChatHandler firstTimeChatHandler = new FirstTimeChatHandler();
                                    firstTimeChatHandler.execute(chat);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        }
                );

                edtTxtChatMessage.setText("");
            }
        });
    }

    private class FirstTimeChatHandler extends AsyncTask<Chat, Void, Chat>{

        @Override
        protected Chat doInBackground(Chat... chats) {
            Log.d("CHAT ACTIVITY", friendUsername+" snapshot does not exist");
//            Log.d("CHAT ACTIVITY", "logging1");
            mRef.child("user").child(friendUsername).child("chat").child(chatId).setValue(new ChatList(myUsername,1));
//            Log.d("CHAT ACTIVITY", "logging2");
            mRef.child("user").child(myUsername).child("chat").child(chatId).setValue(new ChatList(friendUsername, 0));
//            Log.d("CHAT ACTIVITY", "logging3");
            return chats[0];
        }

        @Override
        protected void onPostExecute(Chat chat) {
//            Log.d("CHAT ACTIVITY", "logging4");
            mRef.child("chat").child(chatId).child(""+chats.size()).setValue(chat);
//            Log.d("CHAT ACTIVITY", "logging5");
            super.onPostExecute(chat);
        }
    }



}