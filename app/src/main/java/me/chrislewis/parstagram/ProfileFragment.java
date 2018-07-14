package me.chrislewis.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.parstagram.models.Post;

public class ProfileFragment extends Fragment {

    ParseUser user;

    private static final String KEY_IMAGE = "profilePic";

    ArrayList<Post> posts;
    RecyclerView rvGrid;
    ProfileAdapter adapter;

    TextView tvUsername;
    ImageView ivProfilePic;
    Button bLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        posts = new ArrayList<>();
        adapter = new ProfileAdapter(posts);

        rvGrid = view.findViewById(R.id.rvGrid);
        rvGrid.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        rvGrid.setAdapter(adapter);

        user = ParseUser.getCurrentUser();

        tvUsername = view.findViewById(R.id.tvUsername);
        tvUsername.setText(user.getUsername());

        ivProfilePic = view.findViewById(R.id.ivProfileImage);
        if (user.getParseFile("profilePic") != null) {
            Glide.with(view)
                    .load(user.getParseFile("profilePic").getUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(ivProfilePic);
        }

        bLogOut = view.findViewById(R.id.bLogOut);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        loadPosts();

    }

    private void loadPosts() {
        ParseQuery<Post> postQuery = ParseQuery.getQuery(Post.class);
        postQuery.whereEqualTo("user", user);

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    for(int i = 0; i < objects.size(); i++) {
                        posts.add(objects.get(i));
                        adapter.notifyItemInserted(objects.size() - 1);
                    }
                }
                else {
                    Log.d("HomeActivity", "Post Callback Unsuccessful");
                    e.printStackTrace();
                }
            }
        });
    }
}
