package me.chrislewis.parstagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostFragment extends Fragment {

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    ImageView ivPost;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ivPost = view.findViewById(R.id.ivPost);
        context = getContext();
        try {
            onLaunchCamera(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLaunchCamera(View view) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Fragment frag = this;
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            frag.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

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
                ivPost.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
