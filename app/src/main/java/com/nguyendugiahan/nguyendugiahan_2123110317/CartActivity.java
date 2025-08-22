package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ListView listCart;
    TextView txtTotal;
    Button btnCheckout;
    ImageButton btnBack;
    ArrayList<CartItem> cartItems;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listCart = findViewById(R.id.listCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        // Load giỏ hàng từ SharedPreferences
        cartItems = loadCartFromPrefs();

        adapter = new CartAdapter(this, cartItems, txtTotal);
        listCart.setAdapter(adapter);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút Checkout
        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(CartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 👉 Tính tổng tiền & danh sách sản phẩm
            int total = 0;
            ArrayList<String> orderList = new ArrayList<>();

            for (CartItem item : cartItems) {
                // Tính tổng giá an toàn
                try {
                    String priceStr = (item.getPrice() != null)
                            ? item.getPrice().replaceAll("[^0-9]", "")
                            : "0";

                    if (!priceStr.isEmpty()) {
                        total += Integer.parseInt(priceStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Thêm vào danh sách hiển thị
                String name = (item.getName() != null) ? item.getName() : "Sản phẩm";
                String price = (item.getPrice() != null) ? item.getPrice() : "0";
                orderList.add(name + " - " + price);
            }

            // 👉 Gửi dữ liệu sang CheckoutActivity
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putStringArrayListExtra("order_list", orderList);
            intent.putExtra("total_price", total);
            startActivity(intent);
        });
    }

    private ArrayList<CartItem> loadCartFromPrefs() {
        ArrayList<CartItem> items = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String json = prefs.getString("cart_items", "[]");

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                items.add(new CartItem(
                        obj.optString("name", "Không tên"),       // tránh null
                        obj.optString("price", "0"),              // tránh null
                        obj.optString("description", ""),         // tránh null
                        obj.optInt("image", R.drawable.ic_launcher_foreground) // fallback ảnh
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
}
