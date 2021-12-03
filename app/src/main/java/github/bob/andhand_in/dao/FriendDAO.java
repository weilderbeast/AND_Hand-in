package github.bob.andhand_in.dao;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import github.bob.andhand_in.res.user.User;

public class FriendDAO {
    private static FriendDAO instance;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<User>> friendRequests;
    private List<User> users_friend_requests;
    private ContactDAO contactDAO;

    private FriendDAO() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        friendRequests = new MutableLiveData<>();
        users_friend_requests = new ArrayList<>();
        contactDAO = ContactDAO.getInstance();
    }

    public static FriendDAO getInstance() {
        if (instance == null) {
            instance = new FriendDAO();
        }
        return instance;
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

    public void acceptFriendRequest(User user) {
        firebaseFirestore
                .collection("friendRequests")
                .document(getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        List<String> requestId = (List<String>) task.getResult().get("requestId");
                        for (String id : requestId) {
                            if (id.equals(user.getUID())) {
                                contactDAO.addUserAsContact(id);
                                removeFriendRequest(id);
                            }
                        }
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
        users_friend_requests.clear();
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

    public MutableLiveData<List<User>> getFriendRequestsLiveData() {
        return friendRequests;
    }

    private String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
