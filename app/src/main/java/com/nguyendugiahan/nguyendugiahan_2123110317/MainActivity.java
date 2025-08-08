package com.nguyendugiahan.nguyendugiahan_2123110317;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt, jobEdt;
    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;

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

        // Ánh xạ các thành phần giao diện
        nameEdt = findViewById(R.id.idEdtName);
        jobEdt = findViewById(R.id.idEdtJob);
        postDataBtn = findViewById(R.id.idBtnPost);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.idLoadingPB);

        postDataBtn.setOnClickListener(v -> {
            if (nameEdt.getText().toString().isEmpty() || jobEdt.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            postData(nameEdt.getText().toString(), jobEdt.getText().toString());
        });
    }

    private void postData(String name, String job) {
        loadingPB.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6895acb2039a1a2b288fe535.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        DataModal modal = new DataModal(name, job);

        Call<DataModal> call = retrofitAPI.createPost(modal);

        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();

                nameEdt.setText("");
                jobEdt.setText("");

                DataModal responseFromAPI = response.body();

                if (responseFromAPI != null) {
                    String responseString = "Response Code: " + response.code() + "\n"
                            + "Name: " + responseFromAPI.getName() + "\n"
                            + "Job: " + responseFromAPI.getJob();
                    responseTV.setText(responseString);
                } else {
                    responseTV.setText("Không có dữ liệu trả về!");
                }
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                loadingPB.setVisibility(View.GONE);
                responseTV.setText("Lỗi: " + t.getMessage());
            }
        });
    }
}
