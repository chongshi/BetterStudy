package comp5216.sydney.edu.au.betterstudy.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import comp5216.sydney.edu.au.betterstudy.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final SeatTable seatTableView = (SeatTable) findViewById(R.id.mSearchView);

        Intent intent = new Intent();
        intent.getExtras();

        ;

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
        seatTableView.setData(10, 15);

        seatTableView.setMaxSelected(1);
    }
}
