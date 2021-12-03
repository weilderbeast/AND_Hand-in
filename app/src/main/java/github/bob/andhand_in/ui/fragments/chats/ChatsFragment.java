package github.bob.andhand_in.ui.fragments.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import github.bob.andhand_in.res.chat.Chat;
import github.bob.andhand_in.res.chat.ChatHelper;
import github.bob.andhand_in.res.user.User;


public class ChatsFragment extends Fragment {

    RecyclerView chat_list;
    ChatListAdapter adapter;
    SharedChatViewModel viewModel;
    private FloatingActionButton main_fab;
    private FloatingActionButton text_fab;
    private FloatingActionButton group_fab;
    private boolean isFabOpen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        //TODO create context menus for deleting and stuff
        chat_list = rootView.findViewById(R.id.chats_recycle_view);
        chat_list.hasFixedSize();
        chat_list.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new ChatListAdapter(getContext(), viewModel, this);
        chat_list.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(SharedChatViewModel.class);
        viewModel.getConvos().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.updateWithHelpers(users);
            }
        });
        viewModel.fetchChats();
        main_fab = rootView.findViewById(R.id.main_fab);
        text_fab = rootView.findViewById(R.id.fab_text);
        text_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.contactsFragment);
            }
        });


        group_fab = rootView.findViewById(R.id.fab_group);
        isFabOpen = false;
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFabOpen){
                    showFab();
                } else {
                    closeFab();
                }
            }
        });
    }

    private void showFab(){
        isFabOpen = true;
        text_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        group_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFab(){
        isFabOpen = false;
        text_fab.animate().translationY(0);
        group_fab.animate().translationY(0);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}