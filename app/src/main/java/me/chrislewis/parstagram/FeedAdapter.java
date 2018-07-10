package me.chrislewis.parstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.chrislewis.parstagram.models.Post;

public class FeedAdapter extends  RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    // list of movies
    ArrayList<Post> posts;
    // context for rendering;
    Context context;

    // initialize the list
    public FeedAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }


    // creates and inflates new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_post, viewGroup, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the movie data at the specified position
        Post post = posts.get(i);
        // populate the view with the movie data
        viewHolder.tvCaption.setText(post.getDescription());

    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // class cannot be static
    // implements View.OnClickListener
    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        // track view objects
        ImageView ivPost;
        TextView tvUser;
        TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvCaption = itemView.findViewById(R.id.tvCaption);
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}
