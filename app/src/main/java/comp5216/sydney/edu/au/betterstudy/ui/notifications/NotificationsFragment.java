package comp5216.sydney.edu.au.betterstudy.ui.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import comp5216.sydney.edu.au.betterstudy.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    ListView listView;

    private ListAdapter listAdapter;
    private static final String[] settingElements = {"", "Account", "Upgrade Account", "SETTINGS", "User name",
            "Your email address", "Your phone number", "SUPPORT",
            "Help & FAQ", "Send Feedback", "ABOUT Better Study",
            "What's New?", "Privacy Policy"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
       /* final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        listView = (ListView)root.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getContext(),settingElements);
        listView.setAdapter(listAdapter);
        setupListViewListener();

        return root;
    }



    public void setupListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                switch (position){
                    case 4:
                        builder.setTitle("Your user name")
                                .setMessage("user name")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 5:
                        builder.setTitle("Your email address")
                                .setMessage("email address")
                                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                        break;
                    case 6:
                        builder.setTitle("Your phone number")
                                .setMessage("phone number")
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
}