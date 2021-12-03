package github.bob.andhand_in.dao;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import github.bob.andhand_in.res.app.NotificationManager;
import github.bob.andhand_in.res.user.User;

public class NotificationDAO {
    private static NotificationDAO notificationDAO;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<String> friendNotification;

    private NotificationDAO() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        friendNotification = new MutableLiveData<>();
    }

    public static NotificationDAO getInstance() {
        if (notificationDAO == null) {
            notificationDAO = new NotificationDAO();
        }

        return notificationDAO;
    }

    public MutableLiveData<String> getFriendNotification() {
        return friendNotification;
    }

    public void listenForFriendNotifications() {
        firebaseFirestore
                .collection("friendRequests")
                .document(getCurrentUser())
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

                            for(String requestId: ((ArrayList<String>) snapshot.get("requestId"))){
                                firebaseFirestore
                                        .collection("users")
                                        .whereEqualTo("uid", requestId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                User user = task.getResult().getDocuments().get(0).toObject(User.class);
                                                friendNotification.postValue(user.getDisplayName());
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }

    private String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
