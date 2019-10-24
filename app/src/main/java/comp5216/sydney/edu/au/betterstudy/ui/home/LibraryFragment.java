package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import comp5216.sydney.edu.au.betterstudy.R;

public class LibraryFragment extends Fragment {

    public static boolean flag;
    private static FirebaseFirestore mFirestore;
    private static String userIdFromLogin;
    private FragmentManager manager;
    private FragmentTransaction ft;
    private ImageView img1;
    private ImageView img2;

    // identify whether the date is before today
    public static boolean isDateBeforeToday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy ");
        Date today = new Date();
        String todayStr = sdf.format(today);
        try {
            Date todayZero = sdf.parse(todayStr);
            Log.i("Library", todayZero.toString());
            if (date.before(todayZero)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    //identify whether the date is after today by Hill
    public static boolean isDateAfterToday(Date date) {
        Date today = new Date();
        if (date.after(today)) {
            Log.i("Library", "11111");
            return true;
        } else {
            Log.i("Library", "22222");
            return false;
        }
    }

    //if the date is the same as today identify whether the time is before current time by Hill
    public static boolean isTimeBeforeCurrentTime(String finishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String currentTime = sdf.format(new Date());
        int current = Integer.parseInt(currentTime);
        int finish = Integer.parseInt(finishTime);
        Log.i("Library", String.valueOf(finish));
        Log.i("Library", String.valueOf(current));
        if (finish < current) {
            return true;
        } else {
            return false;
        }
    }

    //judge whether the user has incomplete order
    public static void isUserHasIncompleteOrder() {


        flag = false;

        mFirestore.collection("Seat")
                .whereEqualTo("id", userIdFromLogin)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("task", task.getResult().toString());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                comp5216.sydney.edu.au.betterstudy.model.Seat seat = document.toObject(comp5216.sydney.edu.au.betterstudy.model.Seat.class);
                                String dateStr = document.getString("da");
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date captureDate = format.parse(dateStr);
                                    Log.i("firestore", captureDate.toString() + "  " + dateStr);
                                    if (isDateBeforeToday(captureDate)) {
                                        continue;
                                    } else if (isDateAfterToday(captureDate)) {
                                        flag = true;
                                        Log.i("2", String.valueOf(flag));
                                        break;
                                    } else {
                                        if (isTimeBeforeCurrentTime(seat.getFt())) {
                                            continue;
                                        } else {
                                            flag = true;
                                            Log.i("4", String.valueOf(flag));
                                            break;
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            Log.d("setting", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        userIdFromLogin = getActivity().getIntent().getStringExtra("ID");


        img1 = root.findViewById(R.id.lib1);
        img2 = root.findViewById(R.id.lib2);
        Seat.loadlist();
        manager = getFragmentManager();
        setupImageListener();
        return root;
    }

    private void setupImageListener() {


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
}