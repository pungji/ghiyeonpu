package com.example.class_room_professor_identify_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import Notification.NotificationHelper;
import Notification.NotificationItem;
import Notification.NotificationRepository;
import Notification.ResultAdapter;

public class HistoryActivity extends AppCompatActivity {

    // Volley 관련 변수는 클래스 멤버로 선언
    private EditText nameEditText;
    private Button searchButton;
    private TextView resultTextView;
    private RequestQueue requestQueue;
    private String serverUrl = "아이피주소 지워±";
    // 전역 변수 선언
    private ResultAdapter adapter;
    private RecyclerView alertRecyclerView;

    NotificationRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_history);






        // onCreate()에서 반드시 초기화
        alertRecyclerView = findViewById(R.id.alertRecyclerView);
        adapter = new ResultAdapter(new ArrayList<>());
        alertRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alertRecyclerView.setAdapter(adapter);



        // 홈 버튼 눌렀을 때 메인화면으로 이동
        Button btnHome = findViewById(R.id.homeButton);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 뷰 초기화
        nameEditText = findViewById(R.id.nameEditText);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);
        requestQueue = Volley.newRequestQueue(this);

        // 검색 버튼 클릭 시
        searchButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            if (!name.isEmpty()) {
                sendNameToServer(name);
            } else {
                resultTextView.setText("이름을 입력하세요.");
            }
        });
    }

    // 서버에 이름 검색 요청을 보내는 메서드
    private void sendNameToServer(String name) {
        String url = serverUrl + "?name=" + name;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // 응답을 JSONObject로 파싱
                        JSONObject obj = new JSONObject(response);

                        // "prohe" 키가 있는 경우 해당 값을 가져옴
                        if (obj.has("prowhe")) {
                            int prowheValue = obj.getInt("prowhe");  // 또는 getString()도 가능
                            adapter.addMessage("웹에서 받은 값은 " + prowheValue + "입니다");

                        } else {
                            resultTextView.setText("prohe 값이 존재하지 않습니다.");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultTextView.setText("JSON 파싱 오류: " + e.getMessage());
                    }
                },
                error -> {
                    String msg = "서버 요청 실패: ";
                    if (error.getMessage() != null) {
                        msg += error.getMessage();
                    } else {
                        msg += "알 수 없는 오류";
                    }
                    resultTextView.setText(msg);
                });

        requestQueue.add(stringRequest);
    }

}
