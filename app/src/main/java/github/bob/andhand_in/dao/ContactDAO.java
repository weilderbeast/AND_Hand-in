package github.bob.andhand_in.dao;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import github.bob.andhand_in.res.user.User;

public class ContactDAO {
    private static ContactDAO instance;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<User>> contacts;

    private ContactDAO() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        contacts = new MutableLiveData<>();
    }

    public static ContactDAO getInstance() {
        if (instance == null) {
            instance = new ContactDAO();
        }

        return instance;
    }

    public MutableLiveData<List<User>> getContactsLiveData() {
        return contacts;
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

    public void addUserAsContact(String id) {
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
                    }
                });
    }

    private String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
