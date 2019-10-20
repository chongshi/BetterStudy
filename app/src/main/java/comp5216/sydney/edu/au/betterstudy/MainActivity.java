package comp5216.sydney.edu.au.betterstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5216.sydney.edu.au.betterstudy.ui.notifications.NotificationsFragment;

public class MainActivity extends AppCompatActivity {

    private NotificationsFragment notificationsFragment;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        Toast.makeText(MainActivity.this, "id: " + userId, Toast.LENGTH_SHORT).show();
        //passMessage();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    //to pass message this method doesn't work
    public void passMessage(){
        notificationsFragment = new NotificationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", userId);
        Log.i("mainactivity", userId);
        notificationsFragment.setArguments(bundle);
    }
}
