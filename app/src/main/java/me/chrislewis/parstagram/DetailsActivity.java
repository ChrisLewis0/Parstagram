package me.chrislewis.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import me.chrislewis.parstagram.models.Post;

public class DetailsActivity extends AppCompatActivity {

    Post post;

    Context context;

    TextView tvUsername;
    TextView tvCaption;
    TextView tvCreatedAt;
    TextView tvLikes;

    ImageView ivPost;
    ImageView ivProfileImage;

    ImageButton ibLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = getApplicationContext();

        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvLikes = findViewById(R.id.tvLikes);
        ivPost = findViewById(R.id.ivPost);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ibLike = findViewById(R.id.ibLike);

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvLikes.setText(post.getLikes() + " likes");

        String createdAt = post.getRelativeTimeAgo();

        tvCreatedAt.setText(createdAt);

        if (post.isLiked()){
            ibLike.setBackgroundResource(R.drawable.ufi_heart_active);
        }
        else
        {
            ibLike.setBackgroundResource(R.drawable.ufi_heart);
        }

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.onLike();
                if (post.isLiked()){
                    ibLike.setBackgroundResource(R.drawable.ufi_heart_active);
                }
                else
                {
                    ibLike.setBackgroundResource(R.drawable.ufi_heart);
                }
                tvLikes.setText(post.getLikes() + " likes");
            }
        });

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(ivPost);

        Glide.with(context)
                .load(post.getUser().getParseFile("profilePic").getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(ivProfileImage);
    }
}
