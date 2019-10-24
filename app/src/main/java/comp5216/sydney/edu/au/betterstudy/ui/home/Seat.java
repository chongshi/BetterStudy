package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.betterstudy.R;

public class Seat extends Fragment {


    private static List<comp5216.sydney.edu.au.betterstudy.model.Seat> seats = new ArrayList<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] S;
    String[] F;
    private FragmentManager manager;

    public static void loadlist() {

        db.collection("Seat").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        seats = queryDocumentSnapshots.toObjects(comp5216.sydney.edu.au.betterstudy.model.Seat.class);


                    }
                });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final String library = getArguments().getString("library");
        S = getArguments().getString("S").split(":");
        F = getArguments().getString("F").split(":");
        Toast.makeText(getActivity(), "Library: " + library, Toast.LENGTH_SHORT).show();
        System.out.println(S + "********************" + F);
        View root = inflater.inflate(R.layout.activity_main2, container, false);

        final SeatTable seatTableView = root.findViewById(R.id.mSearchView);

        Button button = root.findViewById(R.id.buttonsave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIdFromLogin = getActivity().getIntent().getStringExtra("ID");
                seatTableView.saveseat(userIdFromLogin, S[0], S[1], F[1], library);
                if (seatTableView.check == true) {

                    Toast.makeText(getActivity(), "Save Success", Toast.LENGTH_SHORT).show();

                    manager.popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Not select", Toast.LENGTH_SHORT).show();
                }


            }
        });
        manager = getFragmentManager();
        Button button1 = root.findViewById(R.id.buttonback);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manager.popBackStack();

            }
        });


        final int ft = Integer.parseInt(S[1]);
        final int tt = Integer.parseInt(F[1]);

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {


                return true;
            }

            @Override
            public boolean isSold(int row, int column) {


                if (seats != null) {

                    for (int i = 0; i < seats.size(); i++) {
                        if (seats.get(i).getLibrary().equals(library) && seats.get(i).getDa().equals(S[0]) && row == seats.get(i).getI() && column == seats.get(i).getJ()) {

                            if ((Integer.parseInt(seats.get(i).getFt()) > ft && Integer.parseInt(seats.get(i).getFt()) <= tt) || (Integer.parseInt(seats.get(i).getSt()) >= ft && Integer.parseInt(seats.get(i).getSt()) < tt))


                                return true;

                        }


                    }
                }


                return false;


            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });

        if (library.equals("UNSW")) {

            seatTableView.setData(10, 15);

        } else if (library.equals("Fisher")) {

            seatTableView.setData(30, 35);

        }

        seatTableView.setScreenName(library);
        seatTableView.setMaxSelected(1);
        return root;
    }


}
