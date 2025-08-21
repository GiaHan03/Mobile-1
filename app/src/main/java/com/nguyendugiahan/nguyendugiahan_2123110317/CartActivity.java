package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ListView listCart;
    TextView txtTotal;
    Button btnCheckout;
    ImageButton btnBack;
    ArrayList<String> cartItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listCart = findViewById(R.id.listCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        // Dữ liệu mẫu - sau này bạn sẽ lấy từ giỏ hàng thật
        cartItems = new ArrayList<>();
        cartItems.add("Sản phẩm A - 100.000đ");
        cartItems.add("Sản phẩm B - 200.000đ");
        cartItems.add("Sản phẩm C - 150.000đ");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        listCart.setAdapter(adapter);

        // Tính tổng tiền
        int total = 100000 + 200000 + 150000;
        txtTotal.setText("Tổng: " + total + "đ");

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(CartActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        });
    }
}
