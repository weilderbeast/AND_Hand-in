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
    private MutableLiveData<User> user;
    private MutableLiveData<List<User>> user_live_data;
    private List<User> users_for_search;

    private UserDAO() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        users_for_search = new ArrayList<>();
        user_live_data = new MutableLiveData<>();
        user = new MutableLiveData<>();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public MutableLiveData<List<User>> getUser_live_data() {
        return user_live_data;
    }

    public void getUserByName(String name) {
        List<User> temp = new ArrayList<>();
        user_live_data.setValue(new ArrayList<>());
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


}



















