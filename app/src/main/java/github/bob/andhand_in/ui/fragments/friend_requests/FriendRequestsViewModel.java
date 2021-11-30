package github.bob.andhand_in.ui.fragments.friend_requests;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import github.bob.andhand_in.dao.UserDAO;
import github.bob.andhand_in.res.user.User;

public class FriendRequestsViewModel extends ViewModel {
    private UserDAO userDAO;

    public FriendRequestsViewModel() {
        userDAO = UserDAO.getInstance();
    }

    public void acceptFriendRequest(User user) {
        userDAO.acceptFriendRequest(user);
    }

    public void declineFriendRequest(User user) {
        userDAO.declineFriendRequest(user);
    }

    public void retrieveFriendRequests() {
        userDAO.getFriendRequests();
    }

    public MutableLiveData<List<User>> listenForFriendRequests(){
        return userDAO.getFriendRequestsLiveData();
    }
}