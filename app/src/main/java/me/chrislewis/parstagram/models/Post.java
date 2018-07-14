package me.chrislewis.parstagram.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;

@ParseClassName("Post")
public class Post extends ParseObject{
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_LIKES = "likes";

    boolean liked;

    public void Post() {
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public boolean isLiked() {
        boolean liked = false;
        if (ParseUser.getCurrentUser().getJSONArray("likes") != null) {
            JSONArray likeArray = ParseUser.getCurrentUser().getJSONArray("likes");
            for (int i = 0; i < likeArray.length(); i++) {
                try {
                    if ((getObjectId()).equals(likeArray.getString(i))) {
                        liked = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Likes", String.valueOf(liked));
        }
        return liked;
    }

    public void onLike() {
        int likes = getInt(KEY_LIKES);
        if (!liked) {
            put(KEY_LIKES, likes + 1);
            ParseUser.getCurrentUser().addUnique("likes", getObjectId());
            liked = true;
        }
        else {
            put(KEY_LIKES, likes - 1);
            ParseUser.getCurrentUser().removeAll("likes", Collections.singleton(getObjectId()));
            liked = false;
        }
        Log.d("Likes", String.valueOf(liked));
        saveInBackground();
        ParseUser.getCurrentUser().saveInBackground();
    }

    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public String getRelativeTimeAgo() {
        String relativeDate;
        long dateMillis = getCreatedAt().getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        return relativeDate;
    }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            orderByDescending("createdAt");
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }

}
