package comp5216.sydney.edu.au.betterstudy;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class SettingActivity extends AppCompatActivity {

    ListView listView;

    private ListAdapter listAdapter;
    private static final String[] settingElements = {"", "Account", "Upgrade Account", "SETTINGS", "User name",
                                            "Your email address", "Your phone number", "SUPPORT",
                                            "Help & FAQ", "Send Feedback", "ABOUT Better Study",
                                            "What's New?", "Privacy Policy"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new ListAdapter(SettingActivity.this,settingElements);
        listView.setAdapter(listAdapter);

    }

    private class ListAdapter extends BaseAdapter{

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
