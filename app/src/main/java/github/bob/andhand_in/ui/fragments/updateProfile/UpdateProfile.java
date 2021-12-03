package github.bob.andhand_in.ui.fragments.updateProfile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.FriendRequestsFragmentBinding;
import github.bob.andhand_in.databinding.UpdateProfileFragmentBinding;

public class UpdateProfile extends Fragment {

    private UpdateProfileViewModel mViewModel;
    private UpdateProfileFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);
        binding = UpdateProfileFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init(root);
        return root;
    }

    private void init(View root) {

    }

}