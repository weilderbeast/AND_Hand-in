package github.bob.andhand_in.ui.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.User;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText re_password;
    TextInputLayout email_layout;
    TextInputLayout password_layout;
    TextInputLayout re_password_layout;
    Button sign_up;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        init();
    }

    private void init() {
        email = findViewById(R.id.email_edit_text);
        email_layout = findViewById(R.id.email);
        password = findViewById(R.id.password_edit_text);
        password_layout = findViewById(R.id.password);
        re_password = findViewById(R.id.repeat_password_edit_text);
        re_password_layout = findViewById(R.id.repeat_password);
        sign_up = findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_up();
            }
        });
    }

    private void sign_up() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String re_password = this.re_password.getText().toString();

        if (email.isEmpty()) {
            email_layout.setError("Cannot be empty");
            email_layout.requestFocus();
            return;
        }

        if (email.length() < 6) {
            email_layout.setError("Has to be longer than 5 characters");
            email_layout.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("Needs to be of pattern email@email.com");
            email_layout.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            password_layout.setError("Cannot be empty");
            password_layout.requestFocus();
            return;
        }

        if (password.length() < 6) {
            password_layout.setError("Has to be longer than 5 characters");
            password_layout.requestFocus();
            return;
        }

        if (!password.equals(re_password)) {
            re_password_layout.setError("Passwords don't match");
            re_password_layout.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            User user = setUserData(mAuth.getCurrentUser());
                            db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                            //get to main menu activity
                            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e){
                                email_layout.setError("Email already in use.");
                                email_layout.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                email_layout.setError("Email is invalid.");
                                email_layout.requestFocus();
                            } catch (Exception e) {
                                Log.w(TAG, "createUserWithEmail:failure", e);
                            }
                        }
                    }
                });
    }

    private User setUserData(FirebaseUser user){
        User actualUser = new User(user.getEmail(), user.getUid());
        actualUser.setDisplayName(user.getDisplayName());
        actualUser.setEmailVerified(user.isEmailVerified());
        actualUser.setPhoneNumber(user.getPhoneNumber());
        actualUser.setPhotoUrl(user.getPhotoUrl());
        return actualUser;
    }

}