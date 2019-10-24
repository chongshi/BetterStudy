package comp5216.sydney.edu.au.betterstudy.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.betterstudy.LoginActivity;
import comp5216.sydney.edu.au.betterstudy.R;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private String userIdFromLogin;
    private String userNmae;
    private String phone;
    private String email;
    private String documentId;
    ListView listView;
    EditText dialogUserName;
    EditText dialogUserEmail;
    EditText dialogUserPhone;
    Button logOut;


    private ListAdapter listAdapter;
    private static final String[] settingElements = {"", "Account", "Update Account", "SETTINGS", "User name",
            "Your email address", "Your phone number", "SUPPORT",
            "Help & FAQ", "Send Feedback", "ABOUT Better Study",
            "What's New?", "Privacy Policy"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        mFirestore = FirebaseFirestore.getInstance();
        userIdFromLogin = getActivity().getIntent().getStringExtra("ID");
        Log.i("userID",userIdFromLogin);
        getDataFromFirestore();
        listView = (ListView)root.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getContext(),settingElements);
        listView.setAdapter(listAdapter);
        logOut = (Button)root.findViewById(R.id.logout);

        setupLogOutListener();
        setupListViewListener();

        return root;
    }
    //get the user's information from firestore
    public void getDataFromFirestore(){
        mFirestore.collection("User")
                .whereEqualTo("userId",userIdFromLogin)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.i("task",task.getResult().toString());
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.i("setting!!!", document.getId() + " => " + document.getData().get("userId"));
                                documentId = document.getId();
                                userNmae = document.getString("userName");
                                phone = document.getString("phone");
                                email = document.getString("email");
                                if(userNmae == null){
                                    userNmae = "null";
                                }
                                if(phone == null){
                                    phone = "null";
                                }
                                if(email == null){
                                    email = "null";
                                }

                            }
                        }else {
                            Log.d("setting","Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //set up the ListView listener
    public void setupListViewListener(){
        Log.i("setting","listener: "+ userNmae + " " + phone + " " + email);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                switch (position){
                    case 2:
                        View dialogView = View.inflate(getContext(), R.layout.setting_dialog, null);
                        dialogUserName = (EditText)dialogView.findViewById(R.id.dialog_userName);
                        dialogUserEmail = (EditText)dialogView.findViewById(R.id.dialog_userEmail);
                        dialogUserPhone = (EditText)dialogView.findViewById(R.id.dialog_userPhone);
                        dialogUserName.setText(userNmae);
                        dialogUserEmail.setText(email);
                        dialogUserPhone.setText(phone);
                        builder.setTitle("Update your account")
                                .setView(dialogView)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DocumentReference documentReference = mFirestore.collection("User").document(documentId);
                                        Map<String,Object> detailInformation = new HashMap<>();
                                        detailInformation.put("userName", dialogUserName.getText().toString());
                                        detailInformation.put("email", dialogUserEmail.getText().toString());
                                        detailInformation.put("phone", dialogUserPhone.getText().toString());
                                        documentReference.update(detailInformation)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "update successfully", Toast.LENGTH_SHORT).show();
                                                        getDataFromFirestore();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "update failure", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 4:
                        Log.i("!!!!!!","listener: "+ userNmae);
                        builder.setTitle("Your user name")
                                .setMessage(userNmae)
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 5:
                        builder.setTitle("Your email address")
                                .setMessage(email)
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 6:
                        builder.setTitle("Your phone number")
                                .setMessage(phone)
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 8:
                        builder.setTitle("Help & FAQ")
                                .setMessage("If you need help, please go to the website.")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 9:
                        EditText editText = new EditText(getContext());
                        editText.setTextColor(Color.BLACK);
                        builder.setTitle("Send Feedback")
                                .setView(editText)
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 11:
                        builder.setTitle("What's New?")
                                .setMessage("This new version provides the function of ordering library's seat for you.")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 12:
                        builder.setTitle("Privacy Policy")
                                .setMessage("Copyright is owned by Group 16.")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    default:
                        break;
                    //throw new IllegalStateException("Unexpected value: " + position);
                }
            }
        });
    }

    //set up the listAdapter
    private class ListAdapter extends BaseAdapter {

        private Context mContext;
        private String[] list;
        private static final int TITLE = 0;
        private static final int SUB_TITLE = 1;
        private static final int UPGRADE = 2;

        public ListAdapter(Context context, String[] list) {
            this.list = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int i) {
            return list[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 3 || position == 7 || position == 10){
                return TITLE;
            }else if (position == 2){
                return UPGRADE;
            }else {
                return SUB_TITLE;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            int type = getItemViewType(position);
            ViewHolder viewHolder = null;
            if (convertView == null){
                switch (type){
                    case TITLE:
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_text1, null);
                        viewHolder.textView = (TextView)convertView.findViewById(R.id.maintitle);

                        break;
                    case SUB_TITLE:
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_text2, null);
                        viewHolder.textView = (TextView)convertView.findViewById(R.id.subtitle);

                        break;
                    case UPGRADE:
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.setting_text3, null);
                        viewHolder.textView = (TextView)convertView.findViewById(R.id.upgrade);

                        break;
                }
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.textView.setText(list[position]);
            return convertView;
        }
    }

    static class ViewHolder{
        TextView textView;
    }

    // click the button and invoke the log out method
    public void setupLogOutListener(){
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Log Out")
                        .setMessage("Do you want to Log Out")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeLoginStatu(false, userIdFromLogin);
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
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

    // Log out the user
    private void removeLoginStatu(boolean status, String userID) {
        //setLoginStatus(false,userID);
        SharedPreferences sp = this.getActivity().getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.remove(userID);
        editor.apply();
    }
}