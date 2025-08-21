package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView txtName, txtPrice, txtDescription;
    Button btnAddToCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProductDetail);
        txtName = findViewById(R.id.txtProductNameDetail);
        txtPrice = findViewById(R.id.txtProductPriceDetail);
        txtDescription = findViewById(R.id.txtProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        int image = getIntent().getIntExtra("image", 0);
        String description = getIntent().getStringExtra("description");

        // Gán dữ liệu
        imgProduct.setImageResource(image);
        txtName.setText(name);
        txtPrice.setText(price);
        txtDescription.setText(description);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu sản phẩm
                String name = txtName.getText().toString();
                String price = txtPrice.getText().toString();
                String description = txtDescription.getText().toString();
                int imageRes = getIntent().getIntExtra("image", 0);

                // Lưu vào giỏ hàng
                SharedPreferences prefs = getSharedPreferences("cart", MODE_PRIVATE);
                String oldCart = prefs.getString("cart_items", "[]");

                try {
                    JSONArray cartArray = new JSONArray(oldCart);

                    JSONObject product = new JSONObject();
                    product.put("name", name);
                    product.put("price", price);
                    product.put("description", description);
                    product.put("image", imageRes);

                    cartArray.put(product);

                    prefs.edit().putString("cart_items", cartArray.toString()).apply();

                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProductDetailActivity.this, "Lỗi khi thêm vào giỏ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}



