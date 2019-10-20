package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;

import comp5216.sydney.edu.au.betterstudy.R;

public class Seat extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String library = getArguments().getString("library");
        String date1 = getArguments().getString("date1");
        String date2 = getArguments().getString("date2");
        Toast.makeText(getActivity(), "Library: " + library, Toast.LENGTH_SHORT).show();
        System.out.println(date1+"********************"+date2);
        View root = inflater.inflate(R.layout.activity_main2, container, false);

        SeatTable seatTableView = (SeatTable) root.findViewById(R.id.mSearchView);

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {

                return true;
            }

            @Override
            public boolean isSold(int row, int column) {

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

        seatTableView.setData(40, 30);

        seatTableView.setScreenName(library);
        seatTableView.setMaxSelected(1);
        return root;
    }
}
