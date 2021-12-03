package github.bob.andhand_in.ui.fragments.contacts.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import github.bob.andhand_in.dao.FriendDAO;
import github.bob.andhand_in.dao.UserDAO;
import github.bob.andhand_in.res.user.User;

public class ContactsAddViewModel extends ViewModel {
    private FriendDAO friendDAO;
    private UserDAO userDAO;
    private MutableLiveData<List<User>> user_search_complete;

    public ContactsAddViewModel(){
        friendDAO = FriendDAO.getInstance();
        userDAO = UserDAO.getInstance();
        user_search_complete = new MutableLiveData<>();
    }

    public void searchUser(String id){
        userDAO.getUserByName(id);
        userDAO.getUser_live_data().observeForever(new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                user_search_complete.postValue(users);
            }
        });
    }

    public LiveData<List<User>> getSearchedUser(){
        return user_search_complete;
    }

    public void addUserAsContact(String uid) {
        friendDAO.sendFriendRequest(uid);
    }
}