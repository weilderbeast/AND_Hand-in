package github.bob.andhand_in.ui.fragments.chats;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Chat;
import github.bob.andhand_in.res.chat.ChatHelper;
import github.bob.andhand_in.res.user.User;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<Chat> chats;
    private Context context;
    private SharedChatViewModel viewModel;
    private Fragment fragment;
    private List<User> users;

    public ChatListAdapter(Context context, SharedChatViewModel viewModel, Fragment fragment) {
        this.context = context;
        this.viewModel = viewModel;
        this.fragment = fragment;
        users = new ArrayList<>();
    }

    public void update(List<Chat> chatList) {
        chats = chatList;
        notifyDataSetChanged();
    }

    public void updateWithHelpers(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_display_layout_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chat_user.setText(users.get(position).getDisplayName());
//        holder.chat_last_sent.setText(chats.get(position).getLast_sent_text());
//        holder.chat_timestamp.setText(chats.get(position).getTimestamp());
//        if(chats.get(position).getUser().getPhotoUrl() == null){
//            holder.chat_user_icon.setImageResource(R.drawable.ic_baseline_person_24);
//        } else {
//            Glide.with(fragment).load(chats.get(position).getUser().getPhotoUrl()).into(holder.chat_user_icon);
//        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", users.get(holder.getAdapterPosition()).getUID());
                bundle.putString("username", users.get(holder.getAdapterPosition()).getDisplayName());
                Navigation.findNavController(view).navigate(R.id.chatFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chat_user;
//        TextView chat_last_sent;
//        TextView chat_timestamp;
//        ShapeableImageView chat_user_icon;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chat_user = itemView.findViewById(R.id.username_layout_simple);
//            chat_last_sent = itemView.findViewById(R.id.chat_last_text);
//            chat_timestamp = itemView.findViewById(R.id.chat_last_text_timestamp);
//            chat_user_icon = itemView.findViewById(R.id.chat_user_icon);
            layout = itemView.findViewById(R.id.chat_item_simple);


        }
    }
}
