package github.bob.andhand_in.ui.fragments.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactsFragmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ContactsFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}