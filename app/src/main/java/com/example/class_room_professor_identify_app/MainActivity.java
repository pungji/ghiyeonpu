package com.example.class_room_professor_identify_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.class_room_professor_identify_app.databinding.ActivityMainBinding;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<View> professorViews = new ArrayList<>();
    private List<View> originalOrder = new ArrayList<>(); // 초기 순서 저장용
    private Map<View, Boolean> favoriteStatus = new HashMap<>();
    private LinearLayout professorListLayout;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        professorListLayout = findViewById(R.id.professor_list_layout);

        int[] professorIds = {
                R.id.professor1, R.id.professor2, R.id.professor3, R.id.professor4,
                R.id.professor5, R.id.professor6, R.id.professor7, R.id.professor8
        };

        int[] starButtonIds = {
                R.id.starButton1, R.id.starButton2, R.id.starButton3, R.id.starButton4,
                R.id.starButton5, R.id.starButton6, R.id.starButton7, R.id.starButton8
        };

        for (int i = 0; i < professorIds.length; i++) {
            View profView = findViewById(professorIds[i]);
            professorViews.add(profView);
            originalOrder.add(profView); // 초기 순서 저장!
            favoriteStatus.put(profView, false);
            setupStarToggle(profView, starButtonIds[i]);
        }
        sortProfessorViews();
        Button btnNotification = findViewById(R.id.alertButton);
        btnNotification.setOnClickListener(v -> {
            ;
            Intent intent = new Intent(MainActivity.this, AlertActivity.class);
            startActivity(intent);
        });
        Button btnTest = findViewById(R.id.testConnectionButton);

        btnTest.setOnClickListener(v -> {
            testRaspberryConnection();
        });
    }

    private void setupStarToggle(View profView, int starButtonId) {
        ImageButton starBtn = profView.findViewById(starButtonId);

        starBtn.setOnClickListener(v -> {
            boolean isFav = favoriteStatus.get(profView);
            isFav = !isFav;
            favoriteStatus.put(profView, isFav);
            starBtn.setImageResource(isFav ? R.drawable.ilike : R.drawable.idonlike);
            sortProfessorViews(); // 즐겨찾기 눌릴 때마다 정렬
        });
    }

    private void sortProfessorViews() {
        professorListLayout.removeAllViews();

        // 초기 순서를 기준으로 즐겨찾기 정렬
        List<View> sorted = new ArrayList<>(originalOrder);
        sorted.sort((v1, v2) -> {
            boolean f1 = favoriteStatus.get(v1);
            boolean f2 = favoriteStatus.get(v2);
            return Boolean.compare(!f1, !f2); // 즐겨찾기 true가 앞으로 오게
        });

        for (View profView : sorted) {
            professorListLayout.addView(profView);
        }

        professorViews = sorted; // 현재 순서 업데이트
    }
    private void testRaspberryConnection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("211.33.127.150:18650")  // 여기에 라즈베리파이 IP 넣기
                .addConverterFactory(GsonConverterFactory.create())

                .build();

        RaspberryApi api = retrofit.create(RaspberryApi.class);

        Call<JsonObject> call = api.getHello(); // RaspberryApi 인터페이스에서 정의한 메서드
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();
                    if (body.has("message")) {
                        String msg = body.get("message").getAsString();
                        Toast.makeText(MainActivity.this, "서버 응답: " + msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "메시지 필드 없음", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "응답 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
