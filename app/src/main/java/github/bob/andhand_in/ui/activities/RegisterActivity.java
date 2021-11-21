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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import github.bob.andhand_in.R;
import github.bob.andhand_in.res.user.CurrentUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get to main menu activity
                            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //TODO this would be turned into a request to Firebase to retrieve the user data with specified inputs
        CurrentUser.getInstance().setUser(new User(email, password));
    }

}