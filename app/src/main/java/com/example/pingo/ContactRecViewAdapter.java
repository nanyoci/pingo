package com.example.pingo;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactRecViewAdapter extends RecyclerView.Adapter<ContactRecViewAdapter.ViewHolder> {

    private ArrayList<User> contacts = new ArrayList<>();

    private Context context;
    public ContactRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.txtContactUsername.setText(contacts.get(position).getUsername());
            holder.contactListLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("friendUsername", holder.txtContactUsername.getText().toString());
                            context.startActivity(intent);
                        }
                    }
            );
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(ArrayList<User> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout contactListLayout;
        private ImageView imgContactProfile;
        private TextView txtContactUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactListLayout = itemView.findViewById(R.id.contactListLayout);
            imgContactProfile = itemView.findViewById(R.id.imgContactProfile);
            txtContactUsername = itemView.findViewById(R.id.txtContactUsername);
        }
    }
}
