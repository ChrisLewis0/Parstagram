package me.chrislewis.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import me.chrislewis.parstagram.models.Post;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("BittyMo")
                .clientKey("KingOfAtlanta")
                .server("http://chrislewis-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);

    }
}
