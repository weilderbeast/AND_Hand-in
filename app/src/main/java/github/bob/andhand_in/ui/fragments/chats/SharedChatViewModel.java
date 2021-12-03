package github.bob.andhand_in.ui.fragments.chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import github.bob.andhand_in.dao.ChatDAO;
import github.bob.andhand_in.res.chat.Chat;
import github.bob.andhand_in.res.chat.Message;
import github.bob.andhand_in.res.user.User;

public class SharedChatViewModel extends ViewModel {
    private ChatDAO chatDAO;

    public SharedChatViewModel() {
        chatDAO = ChatDAO.getInstance();
    }

    public void sendText(String text, String uid){
        chatDAO.sendText(text, uid);
    }

    public void fetchMessages(String uid) {
        chatDAO.fetchMessages(uid);
    }

    public MutableLiveData<List<User>> getConvos(){
        return chatDAO.getMessagesAndChats();
    }

    public void fetchChats(){
        chatDAO.fetchChats();

    }

    public LiveData<List<Message>> getMessages() {
        return chatDAO.getMessages();
    }

    public void subscribeToMessages(String uid) {
        chatDAO.subscribeToMessages(uid);
    }
}
