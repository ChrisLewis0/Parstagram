package me.chrislewis.parstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.chrislewis.parstagram.models.Post;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    private EditText etBody;
    private Button bSubmit;
    private Button bLogOut;
    private Button bCamera;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FeedFragment feedFragment;
    PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        feedFragment = new FeedFragment();
        fragmentTransaction.add(R.id.fragment_container, feedFragment);
//        fragmentTransaction.commit();

        postFragment = new PostFragment();
        fragmentTransaction.add(R.id.fragment_container, postFragment);

//        etBody = findViewById(R.id.etBody);
//        bSubmit = findViewById(R.id.bSubmit);
        bLogOut = findViewById(R.id.bLogOut);
//        bCamera = findViewById(R.id.bCamera);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

//        bSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String description = etBody.getText().toString();
//                final ParseUser user = ParseUser.getCurrentUser();
//
//                final ParseFile parseFile = new ParseFile(photoFile);
//
//                createPost(description, parseFile, user);
//            }
//        });
//
//        bLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParseUser.logOut();
//                Intent intent = new Intent(HomeActivity.this, MainActivity.class) ;
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        bCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    onLaunchCamera(view);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, feedFragment).commit();
                        return true;
                    case R.id.action_camera:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, postFragment).commit();

                        return true;
                    case R.id.action_profile:
                        // do something here
                        return true;
                    default:
                        return true;
                }
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
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


    public void onLaunchCamera(View view) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                takenImage = BitmapScaler.scaleToFitWidth(takenImage, 300);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(photoFile.getAbsolutePath());
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                ivPreview.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
