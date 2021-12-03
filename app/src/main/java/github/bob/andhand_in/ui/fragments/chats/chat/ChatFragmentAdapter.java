package github.bob.andhand_in.ui.fragments.chats.chat;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Message;

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder> {
    private List<Message> messages;

    public ChatFragmentAdapter() {
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_message_layout, parent, false);
        return new ChatFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(messages.get(position).getContent());
        holder.timestamp.setText("");
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void update(List<Message> texts) {
        Collections.sort(texts);
        this.messages = texts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.chat_message_content);
            timestamp = itemView.findViewById(R.id.chat_message_timestamp);
        }
    }
}
