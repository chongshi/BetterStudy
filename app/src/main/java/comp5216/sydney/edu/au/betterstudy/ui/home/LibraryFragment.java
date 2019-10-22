package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comp5216.sydney.edu.au.betterstudy.LoginActivity;
import comp5216.sydney.edu.au.betterstudy.R;

public class LibraryFragment extends Fragment {

    private FragmentManager manager;
    private FragmentTransaction ft;
    private ImageView img1;
    private ImageView img2;

    //new add fields by Hill
    private FirebaseFirestore mFirestore;
    private String userIdFromLogin;
    private boolean flag = false;


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

        //get the userId and init the mFirestore by Hill
        mFirestore = FirebaseFirestore.getInstance();
        userIdFromLogin = getActivity().getIntent().getStringExtra("ID");


        img1 = root.findViewById(R.id.lib1);
        img2 = root.findViewById(R.id.lib2);
        Seat.loadlist();
        manager = getFragmentManager();
        setupImageListener();
        return root;
    }

    private void setupImageListener(){
        //judge if the user has incomplete order by Hill
        if (isUserHasIncompleteOrder()){
            Toast.makeText(getActivity(), "user has incomplete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "user has not incomplete", Toast.LENGTH_SHORT).show();
        }


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("library", "UNSW");
                TimeFragment timeFragment = new TimeFragment();
                timeFragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.nav_host_fragment, timeFragment).addToBackStack(null).commit();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("library", "Fisher");
                TimeFragment timeFragment = new TimeFragment();
                timeFragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.nav_host_fragment, timeFragment).addToBackStack(null).commit();
            }
        });
    }



    // identify whether the date is before today by Hill
    public boolean isDateBeforeToday(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( " dd/MM/yyyy " );
        Date today = new Date();
        String todayStr = sdf.format(today);
        try {
            Date todayZero = sdf.parse(todayStr);
            Log.i("当前时间零点",todayZero.toString());
            if (date.before(todayZero)){
                return true;
            }else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    //identify whether the date is after today by Hill
    public boolean isDateAfterToday(Date date){
        Date today = new Date();
        if(date.after(today)){
            return true;
        }else {
            return false;
        }
    }

    //if the date is the same as today identify whether the time is before current time by Hill
    public boolean isTimeBeforeCurrentTime(int finishTime){
        SimpleDateFormat sdf =   new SimpleDateFormat( "HH" );
        String currentTime = sdf.format(new Date());
        int current = Integer.parseInt(currentTime);
        if (finishTime < current){
            return true;
        }else {
            return false;
        }
    }
    //judge
    public boolean isUserHasIncompleteOrder(){
        mFirestore.collection("Seat")
                .whereEqualTo("id",userIdFromLogin)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("task",task.getResult().toString());
                            for (QueryDocumentSnapshot document : task.getResult()){
                                comp5216.sydney.edu.au.betterstudy.model.Seat seat = document.toObject(comp5216.sydney.edu.au.betterstudy.model.Seat.class);
                                String dateStr = document.getString("da");
                                SimpleDateFormat format =   new SimpleDateFormat( "dd/MM/yyyy" );
                                try {
                                    Date captureDate = format.parse(dateStr);
                                    Log.i("数据库中的时间",captureDate.toString());
                                    if (isDateBeforeToday(captureDate)){
                                        continue;
                                    }else if (isDateAfterToday(captureDate)){
                                        flag = true;
                                        break;
                                    }else {
                                        if (isTimeBeforeCurrentTime(seat.getJ())){
                                            continue;
                                        }else {
                                            flag = true;
                                            break;
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            Log.d("setting","Error getting documents: ", task.getException());
                        }
                    }
                });
        return flag;
    }
}