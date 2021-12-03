package github.bob.andhand_in.ui.fragments.chats.chat;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.databinding.ChatFragmentBinding;
import github.bob.andhand_in.res.chat.Message;
import github.bob.andhand_in.ui.fragments.chats.SharedChatViewModel;

public class ChatFragment extends Fragment {
    private ChatFragmentBinding binding;
    private SharedChatViewModel viewModel;
    private RecyclerView recyclerView;
    private ChatFragmentAdapter adapter;
    private EditText chat_text;
    private ImageView send_button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SharedChatViewModel.class);
        binding = ChatFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init(root);
        return root;
    }

    private void init(View root) {
        recyclerView = root.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new ChatFragmentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        viewModel = new ViewModelProvider(this).get(SharedChatViewModel.class);
        viewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> texts) {
                adapter.update(texts);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
        viewModel.fetchMessages(getArguments().getString("uid"));

        chat_text = root.findViewById(R.id.chat_text);
        send_button = root.findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.sendText(chat_text.getText().toString(), getArguments().getString("uid"));
                chat_text.getText().clear();
            }
        });
        String user = "";
        if(getArguments().getString("username") != null){
            user = getArguments().getString("username");
        }

        viewModel.subscribeToMessages(getArguments().getString("uid"));

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getMessages().removeObservers(this);
    }
}