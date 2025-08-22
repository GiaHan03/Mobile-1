package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CartItem> cartItems;
    private TextView txtTotal;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, TextView txtTotal) {
        this.context = context;
        this.cartItems = cartItems;
        this.txtTotal = txtTotal;
        updateTotal();
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgCartProduct);
            holder.txtName = convertView.findViewById(R.id.txtCartName);
            holder.txtPrice = convertView.findViewById(R.id.txtCartPrice);
            holder.txtDescription = convertView.findViewById(R.id.txtCartDescription);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem item = cartItems.get(position);
        holder.imgProduct.setImageResource(item.getImage());
        holder.txtName.setText(item.getName());
        holder.txtPrice.setText(item.getPrice());
        holder.txtDescription.setText(item.getDescription());

        // Sự kiện xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            cartItems.remove(position);
            saveCartToPrefs();
            notifyDataSetChanged();
            updateTotal();
        });

        return convertView;
    }

    private void updateTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            try {
                String p = item.getPrice().replaceAll("[^0-9]", "");
                total += Integer.parseInt(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        txtTotal.setText("Tổng: " + total + "đ");
    }

    // Cập nhật SharedPreferences khi xóa
    private void saveCartToPrefs() {
        SharedPreferences prefs = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        try {
            for (CartItem item : cartItems) {
                JSONObject obj = new JSONObject();
                obj.put("name", item.getName());
                obj.put("price", item.getPrice());
                obj.put("description", item.getDescription());
                obj.put("image", item.getImage());
                jsonArray.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        prefs.edit().putString("cart_items", jsonArray.toString()).apply();
    }

    static class ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtDescription;
        ImageButton btnDelete;
    }
}
