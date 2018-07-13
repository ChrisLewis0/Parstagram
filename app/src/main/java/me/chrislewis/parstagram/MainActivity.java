package me.chrislewis.parstagram;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;

    private EditText etUsername;
    private EditText etPassword;
    private Button bLogin;
    private Button bSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animationDrawable = (AnimationDrawable) findViewById(R.id.relativeLayout).getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            final Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            bLogin = findViewById(R.id.bLogin);
            bSignUp = findViewById(R.id.bSignUp);

            bLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    login(username, password);
                }
            });

            bSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    signUp(username, password);
                }
            });

        }

    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("Login Activity", "Login Successful");

                    final Intent intent =
                            new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("Login Activity", "Login Failure");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    private void signUp(String username, String password) {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignUp", "Sign Up Success");

                    final Intent intent =
                            new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("SignUp", "Sign Up Fail");
                    e.printStackTrace();

                    // TODO Tell user if username taken
                }
            }
        });
    }
}
