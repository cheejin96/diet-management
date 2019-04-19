package com.example.cheejin.fyp.PostPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cheejin.fyp.ChatFragment;
import com.example.cheejin.fyp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> posts;
    private ChatFragment context;

    public PostAdapter(List<Post> posts, ChatFragment context){
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        holder.username.setText(posts.get(position).username);
        holder.status.setText(posts.get(position).status);
        if(!posts.get(position).photo.isEmpty()) {
            Picasso.with(context.getContext()).load(posts.get(position).getPhoto()).fit().centerCrop().into(holder.photo);
        }else{
            holder.photo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView username;
        protected TextView status;
        protected ImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.userName);
            status = (TextView) itemView.findViewById(R.id.statusText);
            photo = (ImageView) itemView.findViewById(R.id.photoShow);
        }
    }
}
