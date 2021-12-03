package github.bob.andhand_in.ui.fragments.friend_requests;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import github.bob.andhand_in.dao.FriendDAO;
import github.bob.andhand_in.dao.UserDAO;
import github.bob.andhand_in.res.user.User;

public class FriendRequestsViewModel extends ViewModel {
    private FriendDAO friendDAO;

    public FriendRequestsViewModel() {
        friendDAO = FriendDAO.getInstance();
    }

    public void acceptFriendRequest(User user) {
        friendDAO.acceptFriendRequest(user);
    }

    public void declineFriendRequest(User user) {
        friendDAO.declineFriendRequest(user.getUID());
    }

    public void retrieveFriendRequests() {
        friendDAO.getFriendRequests();
    }

    public MutableLiveData<List<User>> listenForFriendRequests(){
        return friendDAO.getFriendRequestsLiveData();
    }
}