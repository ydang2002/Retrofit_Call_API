package com.nhuy.callapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhuy.callapi.API.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;

    private List<User> mListUser;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);

        mListUser = new ArrayList<>();
        getListUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        String strUserName = edtUserName.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        // kiểm tra nếu rỗng hoặc không có dữ liệu thì return
        if (mListUser == null || mListUser.isEmpty()) {
            return;
        }

        boolean isHasUser = false;
        for (User user: mListUser){
            if(strUserName.equals(user.getUserLoginId()) && strPassword.equals(user.getUserPassword())) {
                isHasUser = true;
                mUser = user;
                break;
            }
        }
        
        if(isHasUser) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_user", mUser);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "UserName or Password invalid", Toast.LENGTH_SHORT).show();
        }
    }

    //Call API
    private void getListUser() {
        ApiService.apiService.getListUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                mListUser = response.body();
                Log.e("List users", mListUser.size() + "");
                Toast.makeText(LoginActivity.this, "Call api success" + mListUser.size() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("getListUser", t.getMessage());
                Toast.makeText(LoginActivity.this, "Call api error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}