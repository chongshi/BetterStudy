package comp5216.sydney.edu.au.betterstudy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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



public class RegisterActivity extends AppCompatActivity {

    private Button btn_register;
    private EditText et_user_name, et_psw, et_psw_again;
    private String userName, psw, pswAgain, uId;
    private FirebaseFirestore mFirestore;
    ArrayList<String> userlist, emailList;
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
        emailList = getIntent().getStringArrayListExtra("userEmail");
        rand = new Random();
        init();
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

    public void addData(String userEmail, String password) {
        // Create userID
        Map<String, Object> user = new HashMap<>();
        do {
            Id = rand.nextInt(100000);
            uId = String.valueOf(Id);
        }
        while (isExistUserId(uId));
        user.put("email", userEmail);
        user.put("userId", "540" + uId);
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
        btn_register = findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
        //register button
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                //verify what context in
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

                    /**
                     * save username and password into firestone
                     */
                    if (isNetworkConnected()) {
                        String md5Psw = MD5Utils.md5(psw);
                        addData(userName, md5Psw);
                        Toast.makeText(RegisterActivity.this, "register successfully", Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "network error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    /**
     * gain string
     */
    private void getEditString() {
        userName = et_user_name.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        pswAgain = et_psw_again.getText().toString().trim();
    }

    /**
     * read username from firestone，verify whether exist in firestone
     */
    private boolean isExistUserName(String userName) {
        boolean has_userName = false;
        for (int i = 0; i < emailList.size(); i++) {
            if (userName.equalsIgnoreCase(emailList.get(i))) {
                has_userName = true;
                break;
            }
        }
        return has_userName;
    }

    /**
     * read userID from firestone，verify whether exist in firestone
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
     * read username from editing text，verify it is email
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
