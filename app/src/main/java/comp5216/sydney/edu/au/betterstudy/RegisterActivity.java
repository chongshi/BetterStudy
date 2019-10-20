package comp5216.sydney.edu.au.betterstudy;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import cn.edu.gdmec.android.androidstudiodemo.utils.MD5Utils;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;
    private EditText et_user_name, et_psw, et_psw_again;
    private String userName, psw, pswAgain, uId;
    ;
    private RelativeLayout rl_title_bar;
    private FirebaseFirestore mFirestore;
    ArrayList<String> userlist;
    private Random rand;
    int Id;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirestore = FirebaseFirestore.getInstance();
        userlist = getIntent().getStringArrayListExtra(
                "userId");
        rand = new Random();
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    public void addData(String userEmail, String password) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        do {
            Id = rand.nextInt(10000);
            uId = String.valueOf(Id);
        }
        while (isExistUserId(uId));

        user.put("email", userEmail);
        user.put("userId", uId);
        user.put("userPassWord", password);

        // Add a new document with a generated ID
        mFirestore.collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        /*tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back=findViewById(R.id.tv_back);*/
        //布局根元素
       /* rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);*/
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
        /*tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegisterActivity.this.finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });*/
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "please input email", Toast.LENGTH_SHORT).show();
                } else if (!isEmail(userName)) {
                    Toast.makeText(RegisterActivity.this, "please input correct email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(RegisterActivity.this, "please input password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "please input password again", Toast.LENGTH_SHORT).show();
                } else if (!psw.equals(pswAgain)) {
                    Toast.makeText(RegisterActivity.this, "password doesn't match", Toast.LENGTH_SHORT).show();
                } else if (isExistUserName(userName)) {
                    Toast.makeText(RegisterActivity.this, "username is existing", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "register successful", Toast.LENGTH_SHORT).show();
                    //把账号、密码和账号标识保存到sp里面
                    /**
                     * 保存账号和密码到firestone中
                     */
                    String md5Psw = MD5Utils.md5(psw);
                    addData(userName, md5Psw);
                    // saveRegisterInfo(userName,psw);
                    //注册成功后把账号传递到LoginActivity.java中
                    // 返回值到loginActivity显示
                    //Intent data = new Intent();
                    //startActivity(data);
                    //data.putExtra("userId", uId);
                    //setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    RegisterActivity.this.finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
    }

    /**
     * 获取控件中的字符串
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
    }

    /**
     * 从firestone中读取输入的用户名，判断firestone中是否有此用户名
     */
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        for (int i = 0; i < userlist.size(); i++) {
            if (userName.equalsIgnoreCase(userlist.get(i))) {
                has_userName = true;
                break;
            }
        }
        return has_userName;
    }

    /**
     * 从firestone中读取输入的用户名，判断firestone中是否有此用户名
     */
    private boolean isExistUserId(String userId) {
        boolean has_userId = false;
        for (int i = 0; i < userlist.size(); i++) {
            if (userId.equalsIgnoreCase(userlist.get(i))) {
                has_userId = true;
                break;
            }
        }
        return has_userId;
    }

    /**
     * 从编辑文本中读取输入的用户名，判断用户名是否为email
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
