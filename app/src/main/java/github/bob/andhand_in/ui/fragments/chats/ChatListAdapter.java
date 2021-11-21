package github.bob.andhand_in.ui.fragments.chats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Chat;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    List<Chat> chats;

    public ChatListAdapter() {}

    public void Update(List<Chat> chatList){
        chats = chatList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chat_user.setText(chats.get(position).getUsername());
        holder.chat_last_sent.setText(chats.get(position).getLast_sent_text());
        holder.chat_timestamp.setText(chats.get(position).getLast_sent_text_timestamp());
        holder.chat_user_icon.setImageResource(chats.get(position).getUser_icon());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView chat_user;
        TextView chat_last_sent;
        TextView chat_timestamp;
        ShapeableImageView chat_user_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chat_user = itemView.findViewById(R.id.chat_user);
            chat_last_sent = itemView.findViewById(R.id.chat_last_text);
            chat_timestamp = itemView.findViewById(R.id.chat_last_text_timestamp);
            chat_user_icon = itemView.findViewById(R.id.chat_user_icon);
        }
    }
}
