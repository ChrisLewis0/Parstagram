package me.chrislewis.parstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import me.chrislewis.parstagram.models.Post;

public class HomeActivity extends AppCompatActivity {

    private EditText etBody;
    private Button bSubmit;
    private Button bRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etBody = findViewById(R.id.etBody);
        bSubmit = findViewById(R.id.bSubmit);
        bRefresh = findViewById(R.id.bRefresh);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = etBody.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

//                final File file = new File();
//                final ParseFile parseFile = new ParseFile(file);

                createPost(description, user);

            }
        });

        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPosts();
            }
        });
    }

    private void createPost(String description, /* ParseFile imageFile,*/ ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        //newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create Post Success");
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post [" + i + "] = "
                                + objects.get(i).getDescription()
                                + "username = " + objects.get(i).getUser().getUsername());
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
