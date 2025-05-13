package com.example.class_room_professor_identify_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {

    // Volley 관련 변수는 클래스 멤버로 선언
    private EditText nameEditText;
    private Button searchButton;
    private TextView resultTextView;
    private RequestQueue requestQueue;
    private String serverUrl = "http://211.33.127.150/get_person_info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_history);

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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Volley Response", response.toString());
                    try {
                        if (response.length() > 0) {
                            StringBuilder result = new StringBuilder();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = response.getJSONObject(i);
                                //int id = person.getInt("id");// 이건 아직 안씀
                                String personName = person.getString("name");
                                result.append(personName).append("\n");
                            }
                            resultTextView.setText(result.toString());
                        } else {
                            resultTextView.setText("해당하는 이름을 찾을 수 없음.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultTextView.setText("JSON 파싱 오류");
                    }
                },
                error -> {
                    Log.e("Volley Error", error.toString());
                    resultTextView.setText("서버 요청 실패앙기모띠: " + error.getMessage());
                });

        requestQueue.add(jsonArrayRequest);
    }
}
