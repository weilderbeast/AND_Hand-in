package github.bob.andhand_in.ui.fragments.contacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.User;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder>{
    private List<User> users;
    private ViewGroup fragment;
    private ContactsFragmentViewModel viewModel;

    public ContactsListAdapter(ContactsFragmentViewModel viewModel){
        this.users = new ArrayList<>();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_display_layout, parent, false);
        fragment = parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(users.get(position).getPhotoUrl() == null){
            holder.imageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Glide.with(fragment).load(users.get(position).getPhotoUrl()).into(holder.imageView);
        }

        holder.text.setText(users.get(position).getDisplayName());

        holder.remove_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.removeContact(users.get(holder.getAdapterPosition()).getUID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void update(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView text;
        Button remove_contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.contacts_user_icon);
            text = itemView.findViewById(R.id.contacts_username);
            remove_contact = itemView.findViewById(R.id.remove_contact);
        }
    }
}
