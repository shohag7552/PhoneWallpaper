package com.example.phonewallpaper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.phonewallpaper.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SettingFragment extends Fragment {

    private static final int GOOGLE_SIGN_IN_CODE = 212;
    private GoogleSignInClient googleSignInClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return inflater.inflate(R.layout.fragment_setting, container, false);
        }
        return inflater.inflate(R.layout.fragment_setting_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            ImageView profilePic = view.findViewById(R.id.img);
            TextView textViewName = view.findViewById(R.id.user_name);
            TextView textViewEmail = view.findViewById(R.id.email);

            FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

            Glide.with(getActivity())
                    .load(user.getPhotoUrl().toString())
                    .into(profilePic);

            textViewName.setText(user.getDisplayName());
            textViewEmail.setText(user.getEmail());

            view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_area, new SettingFragment()).commit();
                        }

                    });

                }
            });
        }
        else {
            view.findViewById(R.id.google_sign_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent,GOOGLE_SIGN_IN_CODE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_area,new SettingFragment()).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
