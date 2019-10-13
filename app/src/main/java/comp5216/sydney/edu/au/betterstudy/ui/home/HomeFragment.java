package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import comp5216.sydney.edu.au.betterstudy.R;
import comp5216.sydney.edu.au.betterstudy.ui.notifications.NotificationsFragment;

public class HomeFragment extends Fragment {

    private FragmentManager manager;
    private FragmentTransaction ft;
    private ImageView img1;
    private ImageView img2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_library, container, false);
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
                TimeFragment df = new TimeFragment();
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
                TimeFragment df = new TimeFragment();
                df.setArguments(bundle);
                ft = manager.beginTransaction();
                ft.replace(R.id.nav_host_fragment, df);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}