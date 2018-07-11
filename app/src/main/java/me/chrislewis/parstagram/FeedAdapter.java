package me.chrislewis.parstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);

        viewHolder.tvCaption.setText(post.getDescription());
        viewHolder.tvUser.setText(post.getUser().getUsername());

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
        TextView tvUser;
        TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post tweet = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}
