package github.bob.andhand_in.ui.fragments.friend_requests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.User;
import github.bob.andhand_in.ui.fragments.contacts.ContactsListAdapter;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {
    private FriendRequestsViewModel viewModel;
    private List<User> requests;
    private ViewGroup fragment;

    public FriendRequestsAdapter(FriendRequestsViewModel mViewModel) {
        viewModel = mViewModel;
        requests = new ArrayList<>();
    }

    @NonNull
    @Override
    public FriendRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_request_layout, parent, false);
        fragment = parent;
        return new FriendRequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestsAdapter.ViewHolder holder, int position) {
        System.out.println(requests.get(position));
        if (requests.get(position).getPhotoUrl() == null) {
            holder.icon.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Glide.with(fragment).load(requests.get(position).getPhotoUrl()).into(holder.icon);
        }
        if (requests.get(position).getDisplayName() == null) {
            holder.name.setText("Default");
        } else {
            holder.name.setText(requests.get(position).getDisplayName());
        }

        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.acceptFriendRequest(requests.get(holder.getAdapterPosition()));
                requests.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.declineFriendRequest(requests.get(holder.getAdapterPosition()));
                requests.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void update(List<User> users) {
        requests = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView icon;
        TextView name;
        Button accept_button;
        Button reject_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.friend_request_icon);
            name = itemView.findViewById(R.id.friend_request_name);
            accept_button = itemView.findViewById(R.id.friend_request_accept);
            reject_button = itemView.findViewById(R.id.friend_request_reject);
        }
    }
}
