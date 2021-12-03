package github.bob.andhand_in.dao;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.bob.andhand_in.res.chat.Chat;
import github.bob.andhand_in.res.chat.ChatHelper;
import github.bob.andhand_in.res.chat.Message;
import github.bob.andhand_in.res.chat.MessageHelper;
import github.bob.andhand_in.res.user.User;

public class ChatDAO {
    private static ChatDAO instance;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<Message>> messages;
    private MutableLiveData<List<User>> contacts;

    private ChatDAO() {
        messages = new MutableLiveData<>();
        messages.postValue(new ArrayList<>());
        contacts = new MutableLiveData<>();
        contacts.postValue(new ArrayList<>());
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static ChatDAO getInstance() {
        if (instance == null) {
            instance = new ChatDAO();
        }

        return instance;
    }

    //man i hate using firestore in java :(
    public void sendText(String message, String uid) {
        Message temp = new Message();
        temp.setSenderID(getCurrentUser());
        temp.setContent(message);
        //add to message collection
        firebaseFirestore
                .collection("messages")
                .add(temp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        List<String> query = new ArrayList<>();
                        query.add(uid);
                        query.add(getCurrentUser());
                        //update the chat
                        firebaseFirestore
                                .collection("chats")
                                .whereEqualTo("participants", query)
                                .limit(1)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        //only gets 1 document
                                        if (!task.getResult().getDocuments().isEmpty()) {
                                            ChatHelper helper = task.getResult().getDocuments().get(0).toObject(ChatHelper.class);
                                            List<String> conversationRefs = helper.getConversationRefs();
                                            conversationRefs.add(documentReference.getId());
                                            helper.setConversationRefs(conversationRefs);
                                            firebaseFirestore
                                                    .collection("chats")
                                                    .document(task.getResult().getDocuments().get(0).getId())
                                                    .set(helper)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            System.out.println("Everything went well???");
                                                        }
                                                    });
                                        } else {
                                            //firestore bug
                                            //if the list of participants that i query with is "id1, id2", and in firestore the list is "id2, id1"
                                            //the query "whereEqualTo" returns false
                                            //thus, we need to do both versions of the query, a.k.a "id1, id2" AND "id2, id1"
                                            //if both return false, then we create a new chat
                                            //awful solution, but i am tired
                                            Collections.reverse(query);
                                            firebaseFirestore
                                                    .collection("chats")
                                                    .whereEqualTo("participants", query)
                                                    .limit(1)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            //only gets 1 document
                                                            if (!task.getResult().getDocuments().isEmpty()) {
                                                                ChatHelper helper = task.getResult().getDocuments().get(0).toObject(ChatHelper.class);
                                                                List<String> conversationRefs = helper.getConversationRefs();
                                                                conversationRefs.add(documentReference.getId());
                                                                helper.setConversationRefs(conversationRefs);
                                                                firebaseFirestore
                                                                        .collection("chats")
                                                                        .document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(helper)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                System.out.println("Everything went well.");
                                                                            }
                                                                        });
                                                            } else {
                                                                ChatHelper helper = new ChatHelper();
                                                                helper.setParticipants(query);
                                                                List<String> convRefs = new ArrayList<>();
                                                                convRefs.add(documentReference.getId());
                                                                helper.setConversationRefs(convRefs);
                                                                firebaseFirestore
                                                                        .collection("chats")
                                                                        .add(helper)
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                System.out.println("Everything went well.");
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    public MutableLiveData<List<User>> getMessagesAndChats() {
        return contacts;
    }

    public void fetchChats() {
        contacts.postValue(new ArrayList<>());
        firebaseFirestore
                .collection("chats")
                .whereArrayContains("participants", getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.getResult().getDocuments().isEmpty()) {
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ChatHelper helper = doc.toObject(ChatHelper.class);
                                String uid = "";
                                //awful code
                                //get the id of the other user to query for it
                                //i am tired
                                if (!helper.getParticipants().get(0).equals(getCurrentUser())) {
                                    uid = helper.getParticipants().get(0);
                                } else {
                                    uid = helper.getParticipants().get(1);
                                }
                                firebaseFirestore
                                        .collection("users")
                                        .whereEqualTo("uid", uid)
                                        .limit(1)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                List<User> current = new ArrayList<>(contacts.getValue());
                                                if (!task.getResult().getDocuments().isEmpty()) {
                                                    User temp = task
                                                            .getResult()
                                                            .getDocuments()
                                                            .get(0)
                                                            .toObject(User.class);
                                                    current.add(temp);
                                                    contacts.setValue(current);
                                                } else {
                                                    Log.d(TAG, "Conversations are empty");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    //good lord what the f*ck is this
    public void fetchMessages(String uid) {
        System.out.println("Called fetch********************");
        messages.setValue(new ArrayList<>());
        List<String> query = new ArrayList<>();
        query.add(uid);
        query.add(getCurrentUser());
        firebaseFirestore
                .collection("chats")
                .whereEqualTo("participants", query)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //only one chat per user
                        if (!task.getResult().getDocuments().isEmpty()) {
                            //even if i set limit(1), it still returns a list
                            MessageHelper helper = task.getResult().getDocuments().get(0).toObject(MessageHelper.class);
                            if (!helper.getConversationRefs().isEmpty()) {
                                for (String message : helper.getConversationRefs()) {
                                    firebaseFirestore
                                            .collection("messages")
                                            .document(message)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.getResult().exists()) {
                                                        Message temp = task.getResult().toObject(Message.class);
                                                        List<Message> temp_list = new ArrayList<>();
                                                        temp_list.addAll(messages.getValue());
                                                        temp_list.add(temp);
                                                        messages.setValue(temp_list);
                                                    }
                                                }
                                            });
                                }
                            } else {
                                System.out.println("no messages found for this chat");
                            }
                        } else {
                            Collections.reverse(query);
                            firebaseFirestore
                                    .collection("chats")
                                    .whereEqualTo("participants", query)
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (!task.getResult().getDocuments().isEmpty()) {
                                                MessageHelper helper = task.getResult().getDocuments().get(0).toObject(MessageHelper.class);
                                                if (!helper.getConversationRefs().isEmpty()) {
                                                    for (String message : helper.getConversationRefs()) {
                                                        firebaseFirestore
                                                                .collection("messages")
                                                                .document(message)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.getResult().exists()) {
                                                                            Message temp = task.getResult().toObject(Message.class);
                                                                            List<Message> temp_list = new ArrayList<>();
                                                                            temp_list.addAll(messages.getValue());
                                                                            temp_list.add(temp);
                                                                            messages.setValue(temp_list);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            } else {
                                                System.out.println("no messages found for this chat");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void subscribeToMessages(String uid) {
        List<String> query = new ArrayList<>();
        query.add(uid);
        query.add(getCurrentUser());
        firebaseFirestore
                .collection("chats")
                .whereEqualTo("participants", query)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.getResult().getDocuments().isEmpty()) {
                            firebaseFirestore
                                    .collection("chats")
                                    .whereEqualTo("participants", query)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value,
                                                            @Nullable FirebaseFirestoreException error) {
                                            if (!value.isEmpty()) {
                                                for (DocumentChange dc : value.getDocumentChanges()) {
                                                    if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                                        fetchMessages(uid);
                                                    }
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Collections.reverse(query);
                            firebaseFirestore
                                    .collection("chats")
                                    .whereEqualTo("participants", query)
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            firebaseFirestore
                                                    .collection("chats")
                                                    .whereEqualTo("participants", query)
                                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onEvent(@Nullable QuerySnapshot value,
                                                                            @Nullable FirebaseFirestoreException error) {
                                                            if (!value.isEmpty()) {
                                                                for (DocumentChange dc : value.getDocumentChanges()) {
                                                                    if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                                                        fetchMessages(uid);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                });
    }
}





















