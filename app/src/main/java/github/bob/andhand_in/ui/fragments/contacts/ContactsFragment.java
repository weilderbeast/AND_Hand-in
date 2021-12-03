package github.bob.andhand_in.ui.fragments.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.FragmentContactsBinding;
import github.bob.andhand_in.res.user.User;


public class ContactsFragment extends Fragment {

    private ContactsFragmentViewModel contactsFragmentViewModel;
    private ContactsListAdapter adapter;
    private FragmentContactsBinding binding;
    private RecyclerView recyclerView;

    private FloatingActionButton button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactsFragmentViewModel =
                new ViewModelProvider(this).get(ContactsFragmentViewModel.class);

        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init(root);
        return root;
    }

    private void init(View root) {
        button = binding.addContactFab;
        recyclerView = binding.contactsRecycleView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new ContactsListAdapter(contactsFragmentViewModel);
        recyclerView.setAdapter(adapter);
        contactsFragmentViewModel.queryContacts();
        contactsFragmentViewModel
                .listenForContacts()
                .observe(getViewLifecycleOwner(),adapter::update);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.contactsAddFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //TODO do this everywhere needed to avoid performance issues
        contactsFragmentViewModel.listenForContacts().removeObservers(this);
        binding = null;
    }
}