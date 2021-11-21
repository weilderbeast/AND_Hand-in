package github.bob.andhand_in.ui.fragments.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Chat;


public class ChatsFragment extends Fragment {

    RecyclerView chat_list;
    ChatListAdapter adapter;
    ChatsFragmentViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        //TODO figure this out, set up everything for adding, removing data
        //TODO create context menus for deleting and stuff
        chat_list = rootView.findViewById(R.id.chats_recycle_view);
        chat_list.hasFixedSize();
        chat_list.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new ChatListAdapter();
        chat_list.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(ChatsFragmentViewModel.class);
        viewModel.getChats().observe(getViewLifecycleOwner(), new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                adapter.Update(chats);
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}