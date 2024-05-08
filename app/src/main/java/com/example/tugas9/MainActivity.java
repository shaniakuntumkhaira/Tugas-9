package com.example.tugas9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;


public class MainActivity extends AppCompatActivity {

    TextView etUsername, etName;
    SessionManager sessionManager;
    String username, name;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(MainActivity.this);
        if(!sessionManager.isLoggedIn()){
            moveToLogin();
        }

        etUsername = findViewById(R.id.etMainUsername);
        etName = findViewById(R.id.etMainName);

        username = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        name = sessionManager.getUserDetail().get(SessionManager.NAME);

        etUsername.setText(username);
        etName.setText(name);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kepada pengguna
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_CODE);
        }
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Memeriksa apakah izin diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan
            } else {
                // Izin ditolak
            }
        }
    }
}