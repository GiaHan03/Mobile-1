package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity2 extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;

    // URL API của bạn (MockAPI hoặc server thật)
    String url = "https://6895acb2039a1a2b288fe535.mockapi.io/user";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
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
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean success = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject user = response.getJSONObject(i);

                                // ⚠️ Kiểm tra key trả về từ API (có thể là "name", "username", "email")
                                String email = user.getString("name");   // nếu API là "email" thì sửa lại
                                String pass = user.getString("password");

                                if (email.equals(txtEmail) && pass.equals(txtPass)) {
                                    success = true;

                                    Log.d("DEBUG", "Đăng nhập thành công -> mở HomeActivity");
                                    Toast.makeText(LoginActivity2.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    Intent it = new Intent(LoginActivity2.this, HomeActivity.class);
                                    it.putExtra("email", email);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Lỗi khi gọi API: " + error.getMessage());
                        Toast.makeText(LoginActivity2.this, "Không thể kết nối API", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
}
