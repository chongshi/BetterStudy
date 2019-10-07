package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import comp5216.sydney.edu.au.betterstudy.R;
import comp5216.sydney.edu.au.betterstudy.ui.dashboard.DashboardFragment;
import comp5216.sydney.edu.au.betterstudy.ui.notifications.NotificationsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentManager manager;
    private FragmentTransaction ft;
    private ImageView img1;
    private ImageView img2;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_library, container, false);
/*        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        img1 = root.findViewById(R.id.lib1);
        img2 = root.findViewById(R.id.lib2);
        manager = getFragmentManager();
        setupImageListener();
        return root;
    }

    private void setupImageListener(){
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("library", "UNSW");
                NotificationsFragment df = new NotificationsFragment();
                df.setArguments(bundle);
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, df);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("library", "Fisher");
                NotificationsFragment df = new NotificationsFragment();
                df.setArguments(bundle);
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, df);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}