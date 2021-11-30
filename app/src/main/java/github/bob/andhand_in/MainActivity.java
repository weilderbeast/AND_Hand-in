package github.bob.andhand_in;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import github.bob.andhand_in.ui.activities.MainMenuActivity;
import github.bob.andhand_in.ui.activities.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email;
    TextInputEditText password;
    TextInputLayout email_layout;
    TextInputLayout password_layout;
    Button sign_in_button;
    TextView register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser() != null){
            finish();
        }
    }

    private void init() {
        //TODO create a settings view
        email = findViewById(R.id.email_edit_text);
        email_layout = findViewById(R.id.email_layout);
        password = findViewById(R.id.password_edit_text);
        password_layout = findViewById(R.id.password_layout);
        createInputListeners();
        password.setTransformationMethod(new PasswordTransformationMethod());
        sign_in_button = findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignIn();
            }
        });
        register = findViewById(R.id.register_here);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void createInputListeners() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });
    }

    private void onClickSignIn() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty()) {
            email_layout.setError("Cannot be empty");
            email_layout.requestFocus();
            return;
        }

        if (email.length() < 6) {
            email_layout.setError("Cannot have less than 6 characters");
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
            password_layout.setError("Cannot have less than 6 characters");
            password_layout.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}