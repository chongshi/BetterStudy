package comp5216.sydney.edu.au.betterstudy.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import comp5216.sydney.edu.au.betterstudy.MainActivity;
import comp5216.sydney.edu.au.betterstudy.R;

public class TimeFragment extends Fragment {


    private FragmentManager manager;
    private TimePicker timePicker1;
    private TimePicker timePicker2;
    private TextView timeText;
    private RadioButton todayBtn;
    private RadioButton tomorrowBtn;
    private Button setBtn;
    private Button cancelBtn;
    private String hour1;
    private String hour2;
    private String date;
    private String nextDate;
    private String S, F;
    private Date today, sdate, fdate;
    private boolean isTomorrow;
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    final SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    final SimpleDateFormat DHFormat = new SimpleDateFormat("dd:");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String library = getArguments().getString("library");
        Toast.makeText(getActivity(), "Library: " + library, Toast.LENGTH_SHORT).show();
        View root = inflater.inflate(R.layout.fragment_time, container, false);
        manager = getFragmentManager();
        today = new Date();
        timeText = root.findViewById(R.id.textTime);
        timePicker1 = root.findViewById(R.id.timePicker1);
        timePicker2 = root.findViewById(R.id.timePicker2);
        todayBtn = root.findViewById(R.id.todayBtn);
        tomorrowBtn = root.findViewById(R.id.tomorrowBtn);
        setBtn = root.findViewById(R.id.setBtn);
        cancelBtn = root.findViewById(R.id.cancelBtn);
        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);
        setRadioButtonListener();
        setTimePickerListener();
        setButtonListener();
        return root;
    }


    public void setTimePickerListener() {
        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            Calendar a = Calendar.getInstance();
            final int hour = a.get(Calendar.HOUR_OF_DAY);

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                if (judgeHour(hour, String.valueOf(hour), hour2)) {
                    hour1 = String.valueOf(hour);
                    timeDisplay();
                } else {
                    hour1 = null;
                    timeDisplay();
                }
            }
        });
        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            Calendar a = Calendar.getInstance();
            final int hour = a.get(Calendar.HOUR_OF_DAY);

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                if (judgeHour(hour, hour1, String.valueOf(hour))) {
                    if (hour == 0) {
                        if (date == null || date.equals("dd/MM/yyyy"))
                            nextDate = date;
                        else
                            nextDate = dateAddOne(date);
                    } else {
                        nextDate = date;
                    }
                    hour2 = String.valueOf(hour);
                    timeDisplay();
                } else {
                    hour2 = null;
                    timeDisplay();
                }
            }
        });
    }

    public boolean judgeHour(int hour, String hour1, String hour2) {
        if (hour1 == null || hour2 == null || hour1.equals("HH") || hour2.equals("HH")) {
            if (!isTomorrow) {
                if (hour <= Integer.parseInt(hourFormat.format(today)) && hour != 0) {
                    Toast.makeText(getActivity(), "Please choose future time", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            if (!isTomorrow) {
                if ((hour <= Integer.parseInt(hourFormat.format(today))) && !hour2.equals("0")) {
                    Toast.makeText(getActivity(), "Please choose future time.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if ((Integer.parseInt(hour2) - Integer.parseInt(hour1) > 2 || Integer.parseInt(hour2) - Integer.parseInt(hour1) < 1) && !((hour1.equals("22") || hour1.equals("23")) && hour2.equals("0"))) {
                    Toast.makeText(getActivity(), "You can only choose 1-2 hour(s) in one time.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            } else {
                if ((Integer.parseInt(hour2) - Integer.parseInt(hour1) > 2 || Integer.parseInt(hour2) - Integer.parseInt(hour1) < 1) && !((hour1.equals("22") || hour1.equals("23")) && hour2.equals("0"))) {
                    Toast.makeText(getActivity(), "You can only choose 1-2 hour(s) in one time.", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        }
    }


    public void setRadioButtonListener() {
        todayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isTomorrow = false;
                date = dateFormat.format(today);
                sdate = today;
                fdate = today;
                if (hour2 != null) {
                    if (hour2.equals("0")) {
                        nextDate = dateAddOne(date);
                    }else{
                        nextDate = date;
                    }
                }else {
                    nextDate = date;
                }
                timeDisplay();
            }
        });
        tomorrowBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isTomorrow = true;
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(today);
                calendar.add(calendar.DATE, 1);
                date = dateAddOne(dateFormat.format(today));
                if (hour2 != null) {
                    if (hour2.equals("0")) {
                        nextDate = dateAddOne(date);
                    }else{
                        nextDate = date;
                    }
                }else {
                    nextDate = date;
                }
                date = dateFormat.format(calendar.getTime());
                sdate = calendar.getTime();
                fdate = calendar.getTime();
                timeDisplay();
            }
        });
    }

    public String dateAddOne(String date) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = new GregorianCalendar();
            try {
                calendar.setTime(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(calendar.DATE, 1);
            return dateFormat.format(calendar.getTime());
        }
    }

    public void timeDisplay() {
        if (hour1 == null) {
            hour1 = "HH";
        }
        if (hour2 == null) {
            hour2 = "HH";
        }
        if (date == null) {
            date = "dd/MM/yyyy";
        }
        timeText.setText(date + " " + hour1 + ":00" + " - " + date + " " + hour2 + ":00");
        if (sdate != null && fdate != null) {
            S = DHFormat.format(sdate) + hour1;
            F = DHFormat.format(fdate) + hour2;
        }
        if (nextDate == null) {
            nextDate = "dd/MM/yyyy";
        }
        timeText.setText(date + " " + hour1 + ":00" + " - " + nextDate + " " + hour2 + ":00");
    }

    public void setButtonListener() {
        setBtn.setOnClickListener(new View.OnClickListener() {
            String date1;
            String date2;

            @Override
            public void onClick(View v) {
                if (!(hour1 == null || hour2 == null || hour1.equals("HH") || hour2.equals("HH")) && date != null) {
                    String[] date = timeText.getText().toString().split(" - ");
                    try {
                        date1 = dataFormat.format(dataFormat.parse(date[0]));
                        date2 = dataFormat.format(dataFormat.parse(date[1]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirmation")
                            .setMessage("Are you sure to set this time?\n" + date1 + " to " + date2 + "")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle bundle = new Bundle();
                                    String library = getArguments().getString("library");
                                    bundle.putString("library", library);
                                    bundle.putString("date1", date1);
                                    bundle.putString("date2", date2);
                                    bundle.putString("S", S);
                                    bundle.putString("F", F);
                                    Seat seat = new Seat();
                                    seat.setArguments(bundle);

                                    manager.beginTransaction().replace(R.id.nav_host_fragment, seat).addToBackStack(null).commit();
                                }
                            }).show();
                } else {
                    Toast.makeText(getActivity(), "Set failed: please check the correctness of your current selection time.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
    }
}