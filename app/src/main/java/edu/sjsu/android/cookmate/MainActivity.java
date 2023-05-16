package edu.sjsu.android.cookmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity = MainActivity.this;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = this.findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.information:
                NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
                navController.navigate(R.id.informationScreen);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        switch (item.getItemId()) {
            case R.id.nav_home:
                navController.navigate(R.id.mainFragment);
                System.out.println("Home clicked");
                drawerLayout.close();
                return true;
            case R.id.nav_saved:
                System.out.println("Saved Recipes clicked");
                navController.navigate(R.id.savedRecipes);
                drawerLayout.close();
                return true;
            case R.id.nav_logout:
                // Get the shared preferences object
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                // Get the editor object
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Remove the key-value pair
                editor.remove("user_id");
                // Commit the changes
                editor.commit();
                Intent accountsIntent = new Intent(activity, LoginActivity.class);
                startActivity(accountsIntent);
            default:
                return true;
        }
    }
}