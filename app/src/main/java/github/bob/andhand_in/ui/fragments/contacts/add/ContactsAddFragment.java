package github.bob.andhand_in.ui.fragments.contacts.add;

import static android.content.ContentValues.TAG;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.ContactsAddFragmentBinding;
import github.bob.andhand_in.res.user.User;

public class ContactsAddFragment extends Fragment {

    private ContactsAddViewModel mViewModel;
    private ContactsAddFragmentBinding binding;
    private SearchView searchView;
    private ContactsAddFragmentAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ContactsAddViewModel.class);
        binding = ContactsAddFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }

    private void init(View root) {
        searchView = root.findViewById(R.id.search_user_view);
        recyclerView = root.findViewById(R.id.contacts_add_recycler_view);
        adapter = new ContactsAddFragmentAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mViewModel.getSearchedUser().observe(getViewLifecycleOwner(), adapter::Update);
    }
}