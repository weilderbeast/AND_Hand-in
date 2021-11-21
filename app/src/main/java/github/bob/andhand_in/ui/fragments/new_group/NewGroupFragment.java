package github.bob.andhand_in.ui.fragments.new_group;

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

import github.bob.andhand_in.databinding.FragmentNewGroupBinding;


public class NewGroupFragment extends Fragment {

    private NewGroupFragmentViewModel newGroupFragmentViewModel;
    private FragmentNewGroupBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newGroupFragmentViewModel =
                new ViewModelProvider(this).get(NewGroupFragmentViewModel.class);

        binding = FragmentNewGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        newGroupFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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