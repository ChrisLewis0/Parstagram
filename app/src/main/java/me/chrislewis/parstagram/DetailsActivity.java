package me.chrislewis.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    ImageView ivPost;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = getApplicationContext();

        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        ivPost = findViewById(R.id.ivPost);
        ivProfileImage = findViewById(R.id.ivProfileImage);

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());

        String createdAt = post.getRelativeTimeAgo();

        tvCreatedAt.setText(createdAt);

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(ivPost);

        Glide.with(context)
                .load(post.getUser().getParseFile("profilePic").getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(ivProfileImage);
    }
}
