package github.bob.andhand_in.ui.fragments.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import github.bob.andhand_in.databinding.FragmentContactsBinding;


public class ContactsFragment extends Fragment {

    private ContactsFragmentViewModel contactsFragmentViewModel;
    private FragmentContactsBinding binding;

    //TODO make this a list with add and remove functionality
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactsFragmentViewModel =
                new ViewModelProvider(this).get(ContactsFragmentViewModel.class);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        contactsFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}