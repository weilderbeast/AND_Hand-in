package github.bob.andhand_in.dao;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.User;
import github.bob.andhand_in.ui.fragments.contacts.add.ContactsAddFragment;

public class UserDAO {

    private static UserDAO instance;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<User>> contacts;
    private MutableLiveData<User> user;
    private MutableLiveData<List<User>> user_live_data;
    private List<User> users_for_search;
    private MutableLiveData<List<User>> friendRequests;
    private List<User> users_friend_requests = new ArrayList<>();


    //TODO to separate all of these into different classes
    private UserDAO() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        contacts = new MutableLiveData<>();
        contacts.postValue(new ArrayList<>());
        users_for_search = new ArrayList<>();
        user_live_data = new MutableLiveData<>();
        user = new MutableLiveData<>();
        friendRequests = new MutableLiveData<>();
        subscribeToUserNotifications();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private void subscribeToUserNotifications() {
        subscribeToContactsNotifications();
    }

    private void subscribeToContactsNotifications() {
        firebaseFirestore
                .collection("users")
                .whereEqualTo("uid", getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        firebaseFirestore
                                .collection("users")
                                .document(task.getResult().getDocuments().get(0).getId() + ".contacts")
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                        @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }

                                        if (snapshot != null && snapshot.exists()) {
                                            Log.d(TAG, "Current data: " + snapshot.getData());
//                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Context)
//                                                    .setSmallIcon(R.drawable.ic_baseline_person_add_24)
//                                                    .setContentTitle("Friend request")
//                                                    .setContentText("Someone sent a friend request")
//                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                        } else {
                                            Log.d(TAG, "Current data: null");
                                        }
                                    }
                                });
                    }
                });

    }

    public MutableLiveData<List<User>> getUser_live_data() {
        return user_live_data;
    }

    public MutableLiveData<List<User>> getContactsLiveData() {
        return contacts;
    }

    public MutableLiveData<List<User>> getFriendRequestsLiveData() {
        return friendRequests;
    }

    public void getContacts() {
        List<String> list = new ArrayList<>();
        contacts.postValue(new ArrayList<>());
        firebaseFirestore
                .collection("users")
                .whereEqualTo("uid", getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot documentSnapshot = task.getResult();
                            for (DocumentSnapshot doc : documentSnapshot.getDocuments()) {
                                if (doc.exists()) {
                                    list.clear();
                                    for (String contact : (List<String>) doc.get("contacts")) {
                                        list.add(contact);
                                    }
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            }
                            for (String id : list) {
                                firebaseFirestore
                                        .collection("users")
                                        .whereEqualTo("uid", id)
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
                                                    Log.d(TAG, "Contacts are empty");
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void getUserByName(String name) {
        List<User> temp = new ArrayList<>();
        firebaseFirestore
                .collection("users")
                .whereEqualTo("displayName", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp.add(document.toObject(User.class));
                            }
                            for (User user_t : temp) {
                                if (user_t.getDisplayName().equals(name)) {
                                    users_for_search.add(user_t);
                                }
                            }
                            user_live_data.setValue(users_for_search);
                        } else {
                            Log.e(TAG, task.getException().toString());
                        }
                    }
                });
    }

    private String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void sendFriendRequest(String uid) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(getCurrentUser());
        map.put("requestId", list);
        firebaseFirestore
                .collection("friendRequests")
                .document(uid)
                .set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "everything went well");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    public void removeContact(String id) {
        firebaseFirestore
                .collection("users")
                .whereEqualTo("uid", getCurrentUser())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //this will return only one item every time
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        User user = documentSnapshot.toObject(User.class);
                        String ref = documentSnapshot.getReference().getPath();
                        firebaseFirestore
                                .collection("users")
                                .document(ref.substring(5))
                                .update("contacts", FieldValue.arrayRemove(id));
//                        for (String contactId : contacts) {
//                            firebaseFirestore
//                                    .collection("users")
//                                    .whereEqualTo("uid", contactId)
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if (task.getResult().getDocuments().get(0).toObject(User.class).getUID().equals(id)) {
//                                                contacts.remove(contactId);
//                                                documentSnapshot
//                                                        .getReference()
//                                                        .update("contacts", contacts)
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                Log.e(TAG, "Everything went well?*******************");
//                                                            }
//                                                        });
//                                            }
//                                        }
//                                    });
//                        }

                    }
                });
    }

    //TODO fix this shit, figure out what you need to send and receive
    public void acceptFriendRequest(User user) {
        firebaseFirestore
                .collection("friendRequests")
                .document(getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        List<String> requestId = (List<String>) task.getResult().get("requestId");
                        for(String id:requestId){
                            if(id.equals(user.getUID())){
                                addUserAsContact(id);
                                removeFriendRequest(id);
                            }
                        }
                    }
                });
    }

    private void addUserAsContact(String id) {
        firebaseFirestore
                .collection("users")
                .whereEqualTo("uid", getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        User user = task.getResult().getDocuments().get(0).toObject(User.class);
                        //user.getContacts().add(id);
                        DocumentReference reference = task.getResult().getDocuments().get(0).getReference();
                        String ref = reference.getPath();
                        //adding for this user
                        firebaseFirestore
                                .collection("users")
                                .document(ref.substring(5))
                                .update("contacts", FieldValue.arrayUnion(id));
                        //adding for the other user
                        firebaseFirestore
                                .collection("users")
                                .whereEqualTo("uid", id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        User user1 = task.getResult().getDocuments().get(0).toObject(User.class);
                                        //user1.getContacts().add(getCurrentUser());
                                        DocumentReference reference = task.getResult().getDocuments().get(0).getReference();
                                        String ref1 = reference.getPath();
                                        firebaseFirestore
                                                .collection("users")
                                                .document(ref1.substring(5))
                                                .update("contacts", FieldValue.arrayUnion(getCurrentUser()));
                                    }
                                });
                    }
                });
    }


    private void removeFriendRequest(String id) {
        firebaseFirestore
                .collection("friendRequests")
                .document(getCurrentUser())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Everyhing went well");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                });
    }

    public void declineFriendRequest(User user) {
        firebaseFirestore
                .collection("friendRequests")
                .whereEqualTo("requestId", user.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentReference reference = task.getResult().getDocuments().get(0).getReference();

                        firebaseFirestore
                                .collection("friendRequests")
                                .document(reference.getPath())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Everything went well");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Failed with " + e.getMessage());
                                    }
                                });
                    }
                });
    }

    public void getFriendRequests() {

        firebaseFirestore
                .collection("friendRequests")
                .document(getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot result = task.getResult();
                        List<String> arrayList;
                        arrayList = (List<String>) result.get("requestId");
                        if (arrayList != null) {
                            for (String id : arrayList) {
                                firebaseFirestore
                                        .collection("users")
                                        .whereEqualTo("uid", id)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                users_friend_requests.add(task.getResult().getDocuments().get(0).toObject(User.class));
                                                friendRequests.setValue(users_friend_requests);
                                            }
                                        });
                            }


                        } else {
                            Log.d(TAG, "no friend requests");
                        }
                    }
                });
    }
}



















