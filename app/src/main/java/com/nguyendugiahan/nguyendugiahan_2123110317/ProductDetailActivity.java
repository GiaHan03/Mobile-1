package com.nguyendugiahan.nguyendugiahan_2123110317;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtName, txtPrice, txtDescription;
    Button btnAddToCart, btnViewCart, btnBackHome;

    String name, price, description;
    int image;

    // API lưu đơn hàng
    String API_URL = "https://6895acb2039a1a2b288fe535.mockapi.io/cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProductDetail);
        txtName = findViewById(R.id.txtProductNameDetail);
        txtPrice = findViewById(R.id.txtProductPriceDetail);
        txtDescription = findViewById(R.id.txtProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnBackHome = findViewById(R.id.btnBackHome);

        // Nhận dữ liệu từ Intent
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        description = getIntent().getStringExtra("description");
        image = getIntent().getIntExtra("image", 0);

        // Gán dữ liệu
        imgProduct.setImageResource(image);
        txtName.setText(name);
        txtPrice.setText(price);
        txtDescription.setText(description);

        // Thêm vào giỏ
        btnAddToCart.setOnClickListener(v -> {
            addToCartLocal();   // lưu local
            addToCartAPI();     // lưu server
        });

        // Chuyển sang giỏ hàng
        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Quay lại Home
        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Lưu giỏ hàng trong SharedPreferences
    private void addToCartLocal() {
        SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
        String oldCart = prefs.getString("cart_items", "[]");

        try {
            JSONArray cartArray = new JSONArray(oldCart);

            JSONObject product = new JSONObject();
            product.put("name", name);
            product.put("price", price);
            product.put("description", description);
            product.put("image", image);

            cartArray.put(product);

            prefs.edit().putString("cart_items", cartArray.toString()).apply();

            Toast.makeText(this, name + " đã được thêm vào giỏ hàng (Local)", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi thêm vào giỏ (Local)", Toast.LENGTH_SHORT).show();
        }
    }

    // Gửi giỏ hàng lên API (server)
    private void addToCartAPI() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject product = new JSONObject();
        try {
            product.put("name", name);
            product.put("price", price);
            product.put("description", description);
            product.put("image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                product,
                response -> {
                    Toast.makeText(ProductDetailActivity.this, "Đã lưu sản phẩm lên server!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(ProductDetailActivity.this, "Lỗi khi lưu lên server", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }
}
