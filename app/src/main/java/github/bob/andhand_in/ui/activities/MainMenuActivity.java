package github.bob.andhand_in.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import github.bob.andhand_in.MainActivity;
import github.bob.andhand_in.R;
import github.bob.andhand_in.dao.NotificationDAO;

import github.bob.andhand_in.databinding.ActivityMainMenuBinding;
import github.bob.andhand_in.res.app.NotificationManager;


public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;
    private ShapeableImageView user_icon;
    private TextView user_email;
    private TextView user_username;
    private Button edit_profile_button;
    private NotificationDAO notificationDAO;
    private NotificationManager notificationManager;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainMenu.toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.friendRequests, R.id.contactsFragment, R.id.contactsAddFragment, R.xml.root_preferences)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void init() {
        initUserData();
        notifications();
    }

    //TODO move this within it's own class and figure out context
    private void notifications() {
        notificationDAO = NotificationDAO.getInstance();
        notificationDAO.listenForFriendNotifications();
        notificationManager = NotificationManager.getInstance();
        notificationDAO.getFriendNotification().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                notificationManager.createFriendRequestNotification(getApplicationContext(), "Friend request", s + " wants to be your friend!", R.drawable.ic_baseline_person_add_24);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user_icon = findViewById(R.id.header_icon);
        user_email = findViewById(R.id.nav_header_email);
        user_username = findViewById(R.id.nav_header_username);
        edit_profile_button = findViewById(R.id.edit_profile_button);
        //TODO any items here are null, why?
//        edit_profile_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.updateProfile);
//            }
//        });
//        user_email.setText(user.getEmail());
//        user_username.setText(user.getDisplayName());

    }

    private void initUserData() {

    }


    public void logout(MenuItem item) {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}