package com.nhuy.callapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvUserInfor = findViewById(R.id.tv_user_infor);
        Bundle bundleReceive = getIntent().getExtras();
        if(bundleReceive != null) {
            User user = (User) bundleReceive.get("object_user");
            if(user != null) {
                tvUserInfor.setText(user.toString());
            }
        }
    }
}