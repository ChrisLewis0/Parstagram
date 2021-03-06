package me.chrislewis.parstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

import me.chrislewis.parstagram.models.Post;

public class FeedAdapter extends  RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    ArrayList<Post> posts;
    Context context;

    public FeedAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View feedView = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(feedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Post post = posts.get(i);

        viewHolder.tvCaption.setText(post.getDescription());
        viewHolder.tvUser.setText(post.getUser().getUsername());
        viewHolder.tvCreatedAt.setText(post.getRelativeTimeAgo());
        viewHolder.tvLikes.setText(post.getLikes() + " likes");

        if (post.isLiked()){
            viewHolder.ibLike.setBackgroundResource(R.drawable.ufi_heart_active);
        }
        else
        {
            viewHolder.ibLike.setBackgroundResource(R.drawable.ufi_heart);
        }

        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.onLike();
                if (post.isLiked()){
                    viewHolder.ibLike.setBackgroundResource(R.drawable.ufi_heart_active);
                }
                else
                {
                    viewHolder.ibLike.setBackgroundResource(R.drawable.ufi_heart);
                }
                viewHolder.tvLikes.setText(post.getLikes() + " likes");
            }
        });

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPost);
        Glide.with(context)
                .load(post.getUser().getParseFile("profilePic").getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(viewHolder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivPost;
        ImageView ivProfilePic;
        ImageButton ibLike;
        TextView tvUser;
        TextView tvCaption;
        TextView tvCreatedAt;
        TextView tvLikes;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            ivProfilePic = itemView.findViewById(R.id.ivProfileImage);
            ibLike = itemView.findViewById(R.id.ibLike);
            tvUser = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvLikes = itemView.findViewById(R.id.tvLikes);

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
