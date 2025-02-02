package com.example.pingo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView chatRecView;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final ArrayList <ChatList> chatLists = new ArrayList<>();
    private ChatListRecViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View chatView = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecView = chatView.findViewById(R.id.chatRecView);

        //retrieve friend list
        String myEmail = mAuth.getCurrentUser().getEmail();
        String myUsername = myEmail.substring(0, myEmail.length()-10);

        adapter = new ChatListRecViewAdapter(getContext());
        chatRecView.setAdapter(adapter);
        chatRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRef.child("user").child(myUsername).child("chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatLists.clear();
                        if(snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                            try {
                                String friend = snap.child("friend").getValue().toString();
                                int unread = Integer.parseInt(snap.child("unread").getValue().toString());
                                chatLists.add(new ChatList(friend, unread));
                            }catch (Exception e){
                                Log.d("CHAT FRAGMENT ERROR", snap.toString());
                            }
                            }
                        }
                        else{
                            Log.d("CHAT FRAGMENT", "no chat fragment exist");
                        }
                        adapter.setChatList(chatLists);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return chatView;
    }
}