package comp5216.sydney.edu.au.betterstudy.ui.dashboard;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comp5216.sydney.edu.au.betterstudy.R;
import comp5216.sydney.edu.au.betterstudy.model.Seat;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String userIdFromLogin;
    private FirebaseFirestore mFirestore;
    private String documentId;
    private ArrayList<Seat> historySeatOrder;

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
        mFirestore = FirebaseFirestore.getInstance();
        items = new ArrayList<String>();
        items.add("Time");
        items.add("Library");
        items.add("Seat Row");
        items.add("Seat Column");
        incompleteItems = new ArrayList<String>();
        historyDates = new ArrayList<String>();
        historyItems = new ArrayList<String>();
        historySeatOrder = new ArrayList<Seat>();
        userIdFromLogin = getActivity().getIntent().getStringExtra("ID");

        //there should be a method to read database
        getListOrderDataFromFirestore();



        itemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, items);
        incompleteItemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, incompleteItems);
        historyDatesAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, historyDates);
        historyItemsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, historyItems);

       /* if (incompleteItems.isEmpty()){
            noIncomplete.setVisibility(View.VISIBLE);
            Log.i("incompleteItems", "没有元素在incomplete");
        }else {
            Log.i("incompleteItems", incompleteItems.toString());
            noIncomplete.setVisibility(View.INVISIBLE);
            incomplete1.setAdapter(itemsAdapter);
            incomplete2.setAdapter(incompleteItemsAdapter);
        }

        if (historyDates.isEmpty()){
            noHistory.setVisibility(View.VISIBLE);
            Log.i("history", "没有元素在history");
        }else {
            Log.i("history", historyDates.toString());
            noHistory.setVisibility(View.INVISIBLE);
            historyDate.setAdapter(historyDatesAdapter);
            history1.setAdapter(itemsAdapter);
            history2.setAdapter( historyItemsAdapter);
        }*/
        setUpListViewListener();
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
                                mFirestore.collection("Seat").document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                noIncomplete.setVisibility(View.VISIBLE);
                                                incomplete1.setVisibility(View.INVISIBLE);
                                                incomplete2.setVisibility(View.INVISIBLE);
                                                Log.i("Dashboard", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Dashboard", "Error deleting document", e);
                                            }
                                        });


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
        Log.i("判断当天时间finish", finishTime);
        Log.i("判断当天时间current", Integer.toString(current));
        if (finish <= current) {
            return true;
        }else {
            return false;
        }
    }

    public void getListOrderDataFromFirestore(){
        mFirestore.collection("Seat")
                .whereEqualTo("id",userIdFromLogin)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("task",task.getResult().toString());
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.i("Dashboard", document.getId() + " => " + document.getData().get("userId"));
                                Seat seat = document.toObject(Seat.class);
                                String dateStr = document.getString("da");
                                SimpleDateFormat format =   new SimpleDateFormat( "dd/MM/yyyy" );
                                try {
                                    Date captureDate = format.parse(dateStr);
                                    Log.i("数据库的时间",captureDate.toString());
                                    if (isDateBeforeToday(captureDate)){
                                        historyDates.add(dateStr);
                                        historySeatOrder.add(seat);
                                        if (historyDates.isEmpty()){
                                            Log.i("Dashboard", "空的");
                                        }else {
                                            Log.i("Dashboard", "有元素");
                                        }
                                        Log.i("Dashboard", "在判断日期时加入");
                                        Log.i("Dashboard", dateStr);
                                        Log.i("Dashboard", Integer.toString(seat.getI() + 1) + "  "+ Integer.toString(seat.getJ() + 1));
                                    }else if (isDateAfterToday(captureDate)){
                                        String inCompleteDate = seat.getDa() + " " + seat.getSt() + ":00 - " + seat.getFt() + ":00";
                                        String library = seat.getLibrary() + " library";
                                        String row = Integer.toString(seat.getI() + 1);
                                        String column = Integer.toString(seat.getJ() + 1);
                                        incompleteItems.add(inCompleteDate);
                                        incompleteItems.add(library);
                                        incompleteItems.add(row);
                                        incompleteItems.add(column);
                                        documentId = document.getId();
                                    }else {
                                        if (isTimeBeforeCurrentTime(seat.getFt())){
                                            historyDates.add(dateStr);
                                            historySeatOrder.add(seat);
                                            Log.i("Dashboard", "在判断当天时间时加入");
                                            Log.i("Dashboard", dateStr);
                                            Log.i("Dashboard", Integer.toString(seat.getI() + 1) + "  "+ Integer.toString(seat.getJ() + 1));
                                        }else {
                                            String inCompleteDate = seat.getDa() + " " + seat.getSt() + ":00 - " + seat.getFt() + ":00";
                                            String library = seat.getLibrary() + " library";
                                            String row = Integer.toString(seat.getI() + 1);
                                            String column = Integer.toString(seat.getJ() + 1);
                                            incompleteItems.add(inCompleteDate);
                                            incompleteItems.add(library);
                                            incompleteItems.add(row);
                                            incompleteItems.add(column);
                                            documentId = document.getId();
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (incompleteItems.isEmpty()){
                                noIncomplete.setVisibility(View.VISIBLE);
                                incomplete1.setVisibility(View.INVISIBLE);
                                incomplete2.setVisibility(View.INVISIBLE);
                                Log.i("incompleteItems", "没有元素在incomplete");
                            }else {
                                Log.i("incompleteItems", incompleteItems.toString());
                                noIncomplete.setVisibility(View.INVISIBLE);
                                incomplete1.setVisibility(View.VISIBLE);
                                incomplete2.setVisibility(View.VISIBLE);
                                incomplete1.setAdapter(itemsAdapter);
                                incomplete2.setAdapter(incompleteItemsAdapter);
                            }
                            if (historyDates.isEmpty()){
                                noHistory.setVisibility(View.VISIBLE);
                                historyDate.setVisibility(View.INVISIBLE);
                                history1.setVisibility(View.INVISIBLE);
                                history2.setVisibility(View.INVISIBLE);
                                Log.i("history", "没有元素在history");
                            }else {
                                Log.i("history", historyDates.toString());
                                historyItems.add(historySeatOrder.get(0).getDa() + " " + historySeatOrder.get(0).getSt() + ":00 - " + historySeatOrder.get(0).getFt() + ":00");
                                historyItems.add(historySeatOrder.get(0).getLibrary() + " library");
                                historyItems.add(Integer.toString(historySeatOrder.get(0).getI() + 1));
                                historyItems.add(Integer.toString(historySeatOrder.get(0).getJ() + 1));
                                noHistory.setVisibility(View.INVISIBLE);
                                historyDate.setVisibility(View.VISIBLE);
                                history1.setVisibility(View.VISIBLE);
                                history2.setVisibility(View.VISIBLE);
                                historyDate.setAdapter(historyDatesAdapter);
                                history1.setAdapter(itemsAdapter);
                                history2.setAdapter( historyItemsAdapter);
                            }
                        }else {
                            Log.d("setting","Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void setUpListViewListener(){
        historyDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                historyItems.clear();
                Log.i("清空了",historyItems.toString());
                historyItems.add(historySeatOrder.get(position).getDa() + " " + historySeatOrder.get(position).getSt() + ":00 - " + historySeatOrder.get(position).getFt() + ":00");
                historyItems.add(historySeatOrder.get(position).getLibrary() + " library");
                historyItems.add(Integer.toString(historySeatOrder.get(position).getI() + 1));
                historyItems.add(Integer.toString(historySeatOrder.get(position).getJ() + 1));
                history2.setAdapter( historyItemsAdapter);
                Log.i("加入了",historyItems.toString());
            }
        });
    }
}