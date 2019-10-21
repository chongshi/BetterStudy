package comp5216.sydney.edu.au.betterstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InitialActivity extends AppCompatActivity {

    private Button btn_getStart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        btn_getStart = findViewById(R.id.ivButton);
        btn_getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitialActivity.this.finish();
                startActivity(new Intent(InitialActivity.this, LoginActivity.class));
            }
        });
    }
}
