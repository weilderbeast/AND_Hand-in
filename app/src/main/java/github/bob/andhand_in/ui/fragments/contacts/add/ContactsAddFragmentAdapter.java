package github.bob.andhand_in.ui.fragments.contacts.add;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.User;

public class ContactsAddFragmentAdapter extends RecyclerView.Adapter<ContactsAddFragmentAdapter.ViewHolder> {

    List<User> searched_users;
    Context context;
    ContactsAddViewModel viewModel;

    public ContactsAddFragmentAdapter(Context context, ContactsAddViewModel viewModel) {
        searched_users = new ArrayList<>();
        this.context = context;
        this.viewModel = viewModel;
    }

    public void Update(List<User> users) {
        searched_users.clear();
        searched_users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_add_display_layout, parent, false);
        return new ContactsAddFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(searched_users.get(position).getDisplayName());
        if(searched_users.get(position).getPhotoUrl() != null){
            Glide.with(context).load(searched_users.get(position).getPhotoUrl()).into(holder.icon);
        }

        holder.add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addUserAsContact(searched_users.get(holder.getAdapterPosition()).getUID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searched_users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView icon;
        TextView username;
        Button add_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.contacts_search_user_icon);
            username = itemView.findViewById(R.id.contacts_search_username);
            add_button = itemView.findViewById(R.id.contact_add_button);
        }
    }
}
