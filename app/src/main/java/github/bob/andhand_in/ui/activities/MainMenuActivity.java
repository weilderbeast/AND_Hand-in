package github.bob.andhand_in.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import github.bob.andhand_in.databinding.ActivityMainMenuBinding;


public class MainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainMenuBinding binding;
    private FloatingActionButton main_fab;
    private FloatingActionButton text_fab;
    private FloatingActionButton group_fab;
    private boolean isFabOpen;
    private ShapeableImageView user_icon;
    private TextView user_email;
    private TextView user_username;

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

        init();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    private void init(){
        initFabs();
        initUserData();
    }

    private void initUserData(){
        user_icon = findViewById(R.id.header_icon);
        user_email = findViewById(R.id.header_email);
        user_username = findViewById(R.id.header_username);

        //TODO this does not work, why?
        //user_email.setText(user.getEmail());

    }

    private void initFabs(){
        main_fab = findViewById(R.id.main_fab);
        text_fab = findViewById(R.id.fab_text);
        group_fab = findViewById(R.id.fab_group);
        isFabOpen = false;
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFabOpen){
                    showFab();
                } else {
                    closeFab();
                }
            }
        });
    }

    private void showFab(){
        isFabOpen = true;
        text_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        group_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFab(){
        isFabOpen = false;
        text_fab.animate().translationY(0);
        group_fab.animate().translationY(0);
    }

    public void logout(MenuItem item) {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}