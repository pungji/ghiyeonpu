package com.example.class_room_professor_identify_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.class_room_professor_identify_app.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
