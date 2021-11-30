package github.bob.andhand_in.ui.fragments.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.dao.UserDAO;
import github.bob.andhand_in.res.user.User;

public class ContactsFragmentViewModel extends ViewModel {

    private UserDAO userDAO;

    public ContactsFragmentViewModel() {
        userDAO = UserDAO.getInstance();
    }

    public void queryContacts(){
        userDAO.getContacts();
    }

    public LiveData<List<User>> listenForContacts(){
       return userDAO.getContactsLiveData();
    }

    public void removeContact(String displayName) {
        userDAO.removeContact(displayName);
    }
}