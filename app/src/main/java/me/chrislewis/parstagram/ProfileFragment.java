package me.chrislewis.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    ParseUser user;

    private static final String KEY_IMAGE = "profilePic";


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

        user = ParseUser.getCurrentUser();

        tvUsername = view.findViewById(R.id.tvUsername);
        tvUsername.setText(user.getUsername());

        ivProfilePic = view.findViewById(R.id.ivProfileImage);
        Glide.with(view)
                .load(user.getParseFile("profilePic").getUrl())
                .apply(new RequestOptions().circleCrop())
                .into(ivProfilePic);

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
    }
}
