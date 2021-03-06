package me.chrislewis.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.chrislewis.parstagram.models.Post;

public class FeedFragment extends Fragment {

    ArrayList<Post> posts;
    RecyclerView rvFeed;
    FeedAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        posts = new ArrayList<>();
        adapter = new FeedAdapter(posts);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvFeed = view.findViewById(R.id.rvFeed);
        rvFeed.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvFeed.setAdapter(adapter);

        loadPosts();
    }

    private void loadPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    adapter.clear();
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post [" + i + "] = "
                                + objects.get(i).getDescription()
                                + "username = " + objects.get(i).getUser().getUsername());
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