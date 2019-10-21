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

//import android.support.v7.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private TextView tv_register, tv_find_psw;//返回键,显示的注册，找回密码
    private Button btn_login, btn_getStart;//登录按钮
    private String userName, psw, spPsw, userID, getUserID;//获取的用户名，密码，加密密码
    private EditText et_user_name, et_psw;//编辑框
    private static final String TAG = "LoginActivity";
    private FirebaseFirestore mFirestore;
    String userPwd;
    Map<String, Object> list;
    ArrayList<String> listString, listPwd, listEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


      /*  btn_getStart = findViewById(R.id.ivButton);
        btn_getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
            }
        });*/
        if (isNetworkConnected()) {
            if (getLoginStatus()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ID", getLoginData());
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
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

    //获取界面控件
    private void init() {
        //从main_title_bar中获取的id
       /* tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("Login");*/
        // tv_back = findViewById(R.id.tv_back);
        //从activity_login.xml中获取的
        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        //返回键的点击事件
        /*  tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录界面销毁
                LoginActivity.this.finish();
            }
        });*/
        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为了跳转到注册界面，并实现注册功能
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //Bundle bundle = new Bundle();
                intent.putStringArrayListExtra("userId", listString);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });
        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLoginStatus(false, getLoginData());
                Toast.makeText(LoginActivity.this, "id:" + getLoginData(), Toast.LENGTH_SHORT).show();
                //跳转到找回密码界面（此页面暂未创建）
            }
        });
        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码 getText().toString().trim();
                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                //对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ); psw 进行加密判断是否一致
                String md5Psw = MD5Utils.md5(psw);
                // md5Psw ; spPsw 为 根据从SharedPreferences中用户名读取密码
                // 定义方法 readPsw为了读取用户名，得到密码
                spPsw = readPsw(userName);
                getUserID = readUserId(userName);
                // TextUtils.isEmpty
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "please input username", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "please input password", Toast.LENGTH_SHORT).show();

                    // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                } else if (md5Psw.equals(spPsw)) {
                    //一致登录成功
                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                    setLoginStatus(true, getUserID);
                    //登录成功后关闭此页面进入主页
                    //Intent data=new Intent();
                    //datad.putExtra( ); name , value ;
                    //data.putExtra("isLogin",true);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    //setResult(RESULT_OK,data);
                    //销毁登录界面
                    LoginActivity.this.finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("ID", getUserID);
                    if (getUserID == null){
                        Log.i("loginactivity","ID是空的");
                    }
                    Log.i("loginactivity",getUserID);
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
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
     * 从firestone中根据用户名读取密码
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
     * 从firestone中根据用户名读取userId
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
     * 保存登录状态和登录用户名到SharedPreferences中
     */
    private void setLoginStatus(boolean status, String userID) {

        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserId", userID);
        //提交修改
        editor.apply();
    }

    /**
     * 判断登录状态和登录用户名是否存在SharedPreferences中
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
     * 移除登录状态和登录用户名
     *
     * @return
     */
    private void removeLoginStatus(boolean status, String userID) {
        //setLoginStatus(false,userID);
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.remove(userID);
        editor.apply();
    }

}
