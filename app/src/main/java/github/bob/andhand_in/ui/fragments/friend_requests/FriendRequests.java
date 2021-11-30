package github.bob.andhand_in.ui.fragments.friend_requests;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.FriendRequestsFragmentBinding;
import github.bob.andhand_in.res.user.User;


public class FriendRequests extends Fragment {

    private FriendRequestsViewModel mViewModel;
    private FriendRequestsFragmentBinding binding;
    private RecyclerView recyclerView;
    private FriendRequestsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FriendRequestsViewModel.class);
        binding = FriendRequestsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init(root);
        return root;
    }

    private void init(View root) {
        recyclerView = binding.friendRequestRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new FriendRequestsAdapter(mViewModel);
        recyclerView.setAdapter(adapter);
        mViewModel.retrieveFriendRequests();
        mViewModel.listenForFriendRequests().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.update(users);
            }
        });
    }


}