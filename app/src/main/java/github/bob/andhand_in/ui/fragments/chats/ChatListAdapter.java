package github.bob.andhand_in.ui.fragments.chats;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Chat;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    List<Chat> chats;
    Context context;

    public ChatListAdapter(Context context) {
        this.context = context;
    }

    public void Update(List<Chat> chatList) {
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
        holder.chat_user.setText(chats.get(position).getUser());
        holder.chat_last_sent.setText(chats.get(position).getLast_text());
        holder.chat_timestamp.setText(chats.get(position).getTimestamp());
        holder.chat_user_icon.setImageResource(chats.get(position).getProfile_picture());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.chatFragment);
            }
        });
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
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chat_user = itemView.findViewById(R.id.chat_user);
            chat_last_sent = itemView.findViewById(R.id.chat_last_text);
            chat_timestamp = itemView.findViewById(R.id.chat_last_text_timestamp);
            chat_user_icon = itemView.findViewById(R.id.chat_user_icon);
            layout = itemView.findViewById(R.id.chat_item);


        }
    }
}
