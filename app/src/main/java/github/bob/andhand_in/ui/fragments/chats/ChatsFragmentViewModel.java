package github.bob.andhand_in.ui.fragments.chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.chat.Chat;

public class ChatsFragmentViewModel extends ViewModel {

    private MutableLiveData<List<Chat>> chats;

    public ChatsFragmentViewModel() {
        chats = new MutableLiveData<>();

        ArrayList<Chat> chatArrayList = new ArrayList<>();
        chatArrayList.add(new Chat("Bogdan Cirstoiu","So what about that party?","12:45", R.drawable.profile));
        chatArrayList.add(new Chat("Ionut Grosu","Drugs?","13:42", R.drawable.profile));
        chatArrayList.add(new Chat("Gosia Drygala","How fucking dare you?!","9:21", R.drawable.wallpaper));
        chatArrayList.add(new Chat("Karlo Plepelic","Got any more laptop stickers?","21:45", R.drawable.profile));
        chatArrayList.add(new Chat("Claudiu Hornet","Gym tiiiime","17:23", R.drawable.profile));
        chats.setValue(chatArrayList);
    }

    public LiveData<List<Chat>> getChats(){
        return chats;
    }

    public void addChat(Chat chat){
        List<Chat> currentChats = chats.getValue();
        currentChats.add(chat);
        chats.setValue(currentChats);
    }

    public void deleteChat(Chat chat){
        List<Chat> currentChats = chats.getValue();
        currentChats.remove(chat);
        chats.setValue(currentChats);
    }
}