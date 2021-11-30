package github.bob.andhand_in.ui.fragments.chats.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.ChatFragmentBinding;
import github.bob.andhand_in.databinding.FragmentContactsBinding;
import github.bob.andhand_in.ui.activities.MainMenuActivity;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private ChatFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        binding = ChatFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }

    private void init(View root) {

    }
}