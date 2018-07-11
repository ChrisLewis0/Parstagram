package me.chrislewis.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import me.chrislewis.parstagram.models.Post;

public class DetailsActivity extends AppCompatActivity {

    Post post;

    TextView tvUsername;
    TextView tvCaption;
    ImageView ivPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        ivPost = findViewById(R.id.ivPost);

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        Glide.with(getApplicationContext())
                .load(post.getImage().getUrl())
                .into(ivPost);
    }
}
