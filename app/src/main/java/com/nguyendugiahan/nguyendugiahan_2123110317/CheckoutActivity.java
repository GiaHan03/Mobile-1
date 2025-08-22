package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    EditText edtName, edtPhone, edtAddress;
    Button btnConfirmOrder;
    TextView txtOrderSummary, txtTotalOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        txtOrderSummary = findViewById(R.id.txtOrderSummary);
        txtTotalOrder = findViewById(R.id.txtTotalOrder);

        // Nhận dữ liệu từ CartActivity
        ArrayList<String> orderList = getIntent().getStringArrayListExtra("order_list");
        int totalPrice = getIntent().getIntExtra("total_price", 0);

        // Hiển thị đơn hàng
        if (orderList != null && !orderList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String item : orderList) {
                sb.append("- ").append(item).append("\n");
            }
            txtOrderSummary.setText("Đơn hàng:\n" + sb.toString());
        } else {
            txtOrderSummary.setText("Không có sản phẩm nào trong đơn hàng");
        }

        txtTotalOrder.setText("Tổng: " + totalPrice + " đ");

        btnConfirmOrder.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 👉 Xóa giỏ hàng trong SharedPreferences sau khi đặt hàng thành công
            SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Xử lý lưu đơn hàng hoặc gửi lên server (nếu có)
            Toast.makeText(this,
                    "Đặt hàng thành công!\nCảm ơn " + name,
                    Toast.LENGTH_LONG).show();

            // Quay về Home
            Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
