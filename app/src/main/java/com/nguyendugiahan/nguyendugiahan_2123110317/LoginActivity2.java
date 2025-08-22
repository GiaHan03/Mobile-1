package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity2 extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin, btnRegister;

    String url = "https://6895acb2039a1a2b288fe535.mockapi.io/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // 🔹 Xử lý nút Login
        btnLogin.setOnClickListener(v -> checkLogin());

        // 🔹 Chuyển sang màn hình Đăng ký
        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity2.this, RegisterActivity.class);
            startActivity(i);
        });
    }

    private void checkLogin() {
        String txtEmail = edtEmail.getText().toString().trim();
        String txtPass = edtPassword.getText().toString().trim();

        if (txtEmail.isEmpty() || txtPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    boolean success = false;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject user = response.getJSONObject(i);
                            Log.d("API_USER", user.toString()); // Debug API

                            // Kiểm tra key JSON
                            String email = user.optString("email");
                            String pass = user.optString("password");

                            if (email.equals(txtEmail) && pass.equals(txtPass)) {
                                success = true;
                                Toast.makeText(LoginActivity2.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                // ✅ Lưu thông tin user vào SharedPreferences
                                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("email", email);
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                // ✅ Chuyển sang HomeActivity
                                Intent it = new Intent(LoginActivity2.this, HomeActivity.class);
                                startActivity(it);
                                finish();
                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!success) {
                        Toast.makeText(LoginActivity2.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("ERROR", "Lỗi khi gọi API: " + error.getMessage());
                    Toast.makeText(LoginActivity2.this, "Không thể kết nối API", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
