package comp5216.sydney.edu.au.betterstudy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private TextView tv_register, tv_find_psw;
    private Button btn_login;
    private String userName, psw, spPsw, userID, getUserID;
    private EditText et_user_name, et_psw;
    private static final String TAG = "LoginActivity";
    private FirebaseFirestore mFirestore;
    String userPwd;
    Map<String, Object> list;
    ArrayList<String> listString, listPwd, listEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isNetworkConnected()) {
            if (getLoginStatus()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ID", getLoginData());
                //transmit status of login into MainActivity
                startActivity(intent);
            } else {
                mFirestore = FirebaseFirestore.getInstance();
                list = new HashMap<>();
                listString = new ArrayList<>();
                listPwd = new ArrayList<>();
                listEmail = new ArrayList<>();
                getAllData();
                init();
            }
        } else {
            Toast.makeText(LoginActivity.this, "network error", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isNetworkConnected() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

    public void getAllData() {
        // [START get_all_users]
        mFirestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("userId"));
                                String userEmail = String.valueOf(document.getData().get("email"));
                                String userID = String.valueOf(document.getData().get("userId"));
                                String userPwd = String.valueOf(document.getData().get("userPassWord"));
                                listEmail.add(userEmail);
                                listString.add(userID);
                                listPwd.add(userPwd);
                                // Toast.makeText(LoginActivity.this, listString.get(0) + "22222", Toast.LENGTH_SHORT).show();
                                // Map<String, Object> list = new HashMap<>();
                                //list.put("userId",document.getData().get("userId"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    //gain layout
    private void init() {

        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        //get into page of register
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putStringArrayListExtra("userId", listString);
                intent.putStringArrayListExtra("userEmail", listEmail);
                startActivity(intent);
            }
        });
        //find password which forgot(not create this function)
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //event of login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gain username and password from getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                //encrypt password by MD5
                String md5Psw = MD5Utils.md5(psw);
                spPsw = readPsw(userName);
                getUserID = readUserId(userName);
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "please input username", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "please input password", Toast.LENGTH_SHORT).show();
                } else if (md5Psw.equals(spPsw)) {
                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                    //keep status of login，save userID
                    setLoginStatus(true, getUserID);
                    LoginActivity.this.finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("ID", getUserID);
                    startActivity(intent);
                } else if ((spPsw != null && !TextUtils.isEmpty(spPsw) && !psw.equals(spPsw))) {
                    Toast.makeText(LoginActivity.this, "The user name and password entered are inconsistent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "This user name does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * read password from firestone
     */
    private String readPsw(String userName) {
        for (int i = 0; i < listEmail.size(); i++) {
            if (userName.equalsIgnoreCase(listEmail.get(i))) {
                userPwd = listPwd.get(i);
                break;
            }
        }
        return userPwd;
    }

    /**
     * read userID from firestone
     */
    private String readUserId(String userName) {
        for (int i = 0; i < listEmail.size(); i++) {
            if (userName.equalsIgnoreCase(listEmail.get(i))) {
                userID = listString.get(i);
                break;
            }
        }
        return userID;
    }

    /**
     * keep status of login and userID into SharedPreferences
     */
    private void setLoginStatus(boolean status, String userID) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserId", userID);
        editor.apply();
    }

    /**
     * gain what status and data in SharedPreference
     *
     * @return
     */
    private boolean getLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    private String getLoginData() {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString("loginUserId", null);
    }

    /**
     * remove status of login and userID
     *
     * @return
     */
    private void removeLoginStatus(boolean status, String userID) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.remove(userID);
        editor.apply();
    }


}
