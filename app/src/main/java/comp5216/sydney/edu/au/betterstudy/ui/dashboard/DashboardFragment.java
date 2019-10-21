package comp5216.sydney.edu.au.betterstudy.ui.dashboard;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comp5216.sydney.edu.au.betterstudy.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String userIdFromLogin;

    Button button;
    TextView noIncomplete;
    TextView noHistory;
    ListView incomplete1;
    ListView incomplete2;
    ListView historyDate;
    ListView history1;
    ListView history2;
    ArrayList<String> items;
    ArrayList<String> incompleteItems;
    ArrayList<String> historyDates;
    ArrayList<String> historyItems;
    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> incompleteItemsAdapter;
    ArrayAdapter<String> historyDatesAdapter;
    ArrayAdapter<String> historyItemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        button = root.findViewById(R.id.cancelorder);
        noIncomplete = root.findViewById(R.id.noIncomplete);
        noHistory = root.findViewById(R.id.noHistory);
        incomplete1 = root.findViewById(R.id.incomplete1);
        incomplete2 = root.findViewById(R.id.incomplete2);
        historyDate = root.findViewById(R.id.historyDate);
        history1 = root.findViewById(R.id.history1);
        history2 = root.findViewById(R.id.history2);

        items = new ArrayList<String>();
        incompleteItems = new ArrayList<String>();
        historyDates = new ArrayList<String>();
        historyItems = new ArrayList<String>();

        userIdFromLogin = getActivity().getIntent().getStringExtra("ID");

        //there should be a method to read database


        itemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, items);
        incompleteItemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, incompleteItems);
        historyDatesAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, historyDates);
        historyItemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, historyItems);

        if (incompleteItems.isEmpty()){
            noIncomplete.setVisibility(View.VISIBLE);
        }else {
            noIncomplete.setVisibility(View.INVISIBLE);
            incomplete1.setAdapter(itemsAdapter);
            incomplete2.setAdapter(incompleteItemsAdapter);
        }

        if (historyDates.isEmpty()){
            noHistory.setVisibility(View.VISIBLE);
        }else {
            noHistory.setVisibility(View.INVISIBLE);
            historyDate.setAdapter(historyDatesAdapter);
            history1.setAdapter(itemsAdapter);
            history2.setAdapter( historyItemsAdapter);
        }

        setupButtonListener();
        return root;
    }

    public void  setupButtonListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cancel this incomplete order")
                        .setMessage("Do you want to cancel this incomplete order ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        });
    }
    // identify whether the date is before today
    public boolean isDateBeforeToday(Date date){
        Date today = new Date();
        if (date.before(today)){
            return true;
        }else {
            return false;
        }
    }

    //identify whether the date is after today
    public boolean isDateAfterToday(Date date){
        Date today = new Date();
        if(date.after(today)){
            return true;
        }else {
            return false;
        }
    }

    //identify whether the time is before current time
    public boolean isTimeBeforeCurrentTime(String finishTime){
        SimpleDateFormat sdf =   new SimpleDateFormat( "HH" );
        String currentTime = sdf.format(new Date());
        int current = Integer.parseInt(currentTime);
        int finish = Integer.parseInt(finishTime);
        if (finish < current){
            return true;
        }else {
            return false;
        }
    }

}