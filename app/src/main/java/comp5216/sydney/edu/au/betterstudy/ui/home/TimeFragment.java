package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import comp5216.sydney.edu.au.betterstudy.R;

public class TimeFragment extends Fragment {
    private FragmentManager manager;
    private FragmentTransaction ft;
    private TimePicker timePicker1;
    private TimePicker timePicker2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_time, container, false);
        manager = getFragmentManager();
        timePicker1 = root.findViewById(R.id.timePicker1);
        timePicker2 =root.findViewById(R.id.timePicker2);
        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);
        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            Calendar a = Calendar.getInstance();
            final int hour = a.get(Calendar.HOUR_OF_DAY);

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Toast.makeText(getActivity(), i + "时" + i + "分",Toast.LENGTH_SHORT).show();
            }
        });
        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            Calendar a = Calendar.getInstance();
            final int hour = a.get(Calendar.HOUR_OF_DAY);

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Toast.makeText(getActivity(), i + "时" + i + "分",Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}