package com.example.tugas9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tugas9.api.ApiClient;
import com.example.tugas9.api.ApiInterface;
import com.example.tugas9.model.login.Login;
import com.example.tugas9.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etUserName, etPassword;
    Button btnLogin;
    String Username, Password;
    TextView tvRegister;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUserName = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        tvRegister = findViewById(R.id.tvCreateAccount);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            Username = etUserName.getText().toString();
            Password = etPassword.getText().toString();
            login(Username, Password);
        } else if (view.getId() == R.id.tvCreateAccount) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }


    private void login(String username, String password) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginData> loginCall = apiInterface.loginResponse(username, password);
        loginCall.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    sessionManager = new SessionManager(LoginActivity.this);
                    LoginData loginData = response.body().getData();
                    sessionManager.createLoginSession(loginData);

                    Toast.makeText(LoginActivity.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}