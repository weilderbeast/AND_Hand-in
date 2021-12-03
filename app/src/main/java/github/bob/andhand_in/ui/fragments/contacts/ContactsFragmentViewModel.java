package github.bob.andhand_in.ui.fragments.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.dao.ContactDAO;
import github.bob.andhand_in.dao.UserDAO;
import github.bob.andhand_in.res.user.User;

public class ContactsFragmentViewModel extends ViewModel {

    private ContactDAO contactDAO;

    public ContactsFragmentViewModel() {
        contactDAO = ContactDAO.getInstance();
    }

    public void queryContacts(){
        contactDAO.getContacts();
    }

    public LiveData<List<User>> listenForContacts(){
       return contactDAO.getContactsLiveData();
    }

    public void removeContact(String displayName) {
        contactDAO.removeContact(displayName);
    }
}