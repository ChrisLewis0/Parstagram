package me.chrislewis.parstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;

import me.chrislewis.parstagram.models.Post;

public class ProfileAdapter extends  RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    ArrayList<Post> posts;
    Context context;

    public ProfileAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View feedView = inflater.inflate(R.layout.item_grid, viewGroup, false);
        return new ViewHolder(feedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPost);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivPost;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post tweet = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(tweet));
                view.getContext().startActivity(intent);
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}
