package com.nhuy.uploadimage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nhuy.uploadimage.api.ApiService;
import com.nhuy.uploadimage.api.Const;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private static final int MY_REQUEST_CODE = 10;
    private EditText edtUserName, edtPassword;
    private ImageView imgFromGallery, imgFromApi;
    private Button btnSelectImage, btnUploadImage;
    private TextView tvUserName, tvPassWord;
    private Uri mUri;
    private ProgressDialog mProgressDialog;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if( result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgFromGallery.setImageBitmap(bitmap);
                        } catch (IOException e) {

                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        // Init progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait ...");

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (mUri != null) {
                   Log.e("click", "click up load");
                   callApiRegisterAccount();
               }
            }
        });
    }

    private void initUi() {
        edtUserName = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        imgFromGallery = findViewById(R.id.img_from_gallery);
        imgFromApi = findViewById(R.id.img_from_api);
        btnSelectImage = findViewById(R.id.btn_image);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        tvUserName = findViewById(R.id.tv_username);
        tvPassWord = findViewById(R.id.tv_password);
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    // Lắng nghe người dùng cho phép hay từ chối permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void callApiRegisterAccount() {
        mProgressDialog.show();

        String strUsername = edtUserName.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        RequestBody requestBodyUsername = RequestBody.create(MediaType.parse("multipart/from-data"), strUsername);
        RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("multipart/from-data"), strPassword);

        String strRealPath = RealPathUtil.getRealPath(this, mUri);
        Log.e("Duong dan anh", strRealPath);
        File file = new File(strRealPath);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/from-data"), file);
        MultipartBody.Part multipartAvt = MultipartBody.Part.createFormData(Const.KEY_AVT, file.getName(), requestBodyAvatar);

//        ApiService.apiService.registerAccount(requestBodyUsername, requestBodyPassword, multipartAvt).enqueue(new Callback<Accounts>() {
//            @Override
//            public void onResponse(Call<Accounts> call, Response<Accounts> response) {
//                mProgressDialog.dismiss();
//                Accounts accounts = response.body();
//                if (accounts != null) {
//                    tvUserName.setText(accounts.getUsername());
//                    tvPassWord.setText(accounts.getPassword());
//                    Glide.with(MainActivity.this).load(accounts.getAvt()).into(imgFromApi);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Accounts> call, Throwable t) {
//                mProgressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "Call api fail" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("error", t.getMessage());
//            }
//        });

        ApiService.apiService.registerAccount(requestBodyUsername, requestBodyPassword, multipartAvt).enqueue(new Callback<Accounts>() {
            @Override
            public void onResponse(Call<Accounts> call, Response<Accounts> response) {
                mProgressDialog.dismiss();
                Accounts accounts = response.body();
                if (accounts != null) {
                    tvUserName.setText(accounts.getUsername());
                    tvPassWord.setText(accounts.getPassword());
                    Glide.with(MainActivity.this).load(accounts.getAvt()).into(imgFromApi);
                }
            }

            @Override
            public void onFailure(Call<Accounts> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Call api fail" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}