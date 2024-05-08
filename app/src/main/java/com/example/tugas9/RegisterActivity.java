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
import com.example.tugas9.model.login.LoginData;
import com.example.tugas9.model.register.Register;
import com.example.tugas9.model.register.RegisterData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etPassword, etName;
    Button btnRegister;
    TextView tvLogin;
    String Username, Password, Name;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etName = findViewById(R.id.etRegisterName);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        tvLogin = findViewById(R.id.tvLoginHere);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnRegister) {
            Username = etUsername.getText().toString();
            Password = etPassword.getText().toString();
            Name = etName.getText().toString();
            register(Username, Password, Name);
        } else if (view.getId() == R.id.tvLoginHere) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void register(String username, String password, String name){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterData> call = apiInterface.RegisterResponse(username, password, name);
        call.enqueue(new Callback<RegisterData>() { // Ubah dari Register ke RegisterData
            @Override
            public void onResponse(Call<RegisterData> call, Response<RegisterData> response) {
                if(response.isSuccessful() && response.body() != null){
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    Toast.makeText(RegisterActivity.this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                    finish(); // Menutup aktivitas saat ini setelah berhasil mendaftar
                } else {
                    // Menampilkan pesan kesalahan dari respons
                    String errorMessage = "Gagal membuat akun";
                    if (response.errorBody() != null) {
                        errorMessage = response.errorBody().toString();
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterData> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}