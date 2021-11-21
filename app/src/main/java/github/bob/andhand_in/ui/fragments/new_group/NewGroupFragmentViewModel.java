package github.bob.andhand_in.ui.fragments.new_group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewGroupFragmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewGroupFragmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}