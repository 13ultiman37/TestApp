package com.example.testapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;
import com.example.testapp.activities.ChatActivity;
import com.example.testapp.activities.ProfileActivity;
import com.example.testapp.activities.UsersActivity;
import com.example.testapp.adapters.UsersAdapter;
import com.example.testapp.databinding.ActivityUsersBinding;
import com.example.testapp.databinding.FragmentChatBinding;
import com.example.testapp.databinding.FragmentFriendsBinding;
import com.example.testapp.listeners.UserListener;
import com.example.testapp.models.User;
import com.example.testapp.utilities.Constants;
import com.example.testapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Fragment implements UserListener{

    private FragmentFriendsBinding friendsBinding;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        friendsBinding = FragmentFriendsBinding.inflate(getLayoutInflater());
        View view = friendsBinding.getRoot();
        friendsBinding.imageSettings.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        preferenceManager = new PreferenceManager(getActivity());
        getUsers();

        return view;
    }


    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.nickname = queryDocumentSnapshot.getString(Constants.KEY_NICKNAME);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, (UserListener) this);
                            friendsBinding.usersRecyclerView.setAdapter(usersAdapter);
                            friendsBinding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        friendsBinding.textErrorMessage.setText(String.format("%s", "Нет доступных пользователей"));
        friendsBinding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            friendsBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            friendsBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}