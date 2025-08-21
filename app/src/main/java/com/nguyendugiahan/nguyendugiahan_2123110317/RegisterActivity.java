package com.nguyendugiahan.nguyendugiahan_2123110317;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtpassword,  edtEmail;
    Button btnRegister;
    String API_URL = "https://6895acb2039a1a2b288fe535.mockapi.io/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.txtName);
        edtpassword = findViewById(R.id.txtPass);

        edtEmail = findViewById(R.id.txtEmail);
        btnRegister = findViewById(R.id.btnNext);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUserToAPI();
            }
        });
    }

    private void postUserToAPI() {
        String name = edtName.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                        // Quay về MainActivity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API", "Error: " + error.getMessage());
                        Toast.makeText(RegisterActivity.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }
}
