// 🔐 LoginRegisterActivity.java (회원가입 및 로그인 통합 액티비티)
package com.example.class_room_professor_identify_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity {

    // 입력 필드 및 버튼 선언
    private EditText editName, editUserId, editPassword;
    private Button btnRegister, btnLogin;

    // Volley 요청 큐 및 서버 주소
    private RequestQueue requestQueue;
    private final String registerUrl = "\"http://211.33.127.150/get_person_info.php\"";//서버 url 추가
    private final String loginUrl = "\"http://211.33.127.150/get_person_info.php\"";//서버 url 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        // 뷰 연결
        editName = findViewById(R.id.editName);
        editUserId = findViewById(R.id.editUserId);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        // Volley 요청 큐 초기화
        requestQueue = Volley.newRequestQueue(this);

        // 버튼 클릭 이벤트 설정
        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> loginUser());
    }

    // 회원가입 요청 처리 함수
    private void registerUser() {
        String name = editName.getText().toString();
        String userId = editUserId.getText().toString();
        String password = editPassword.getText().toString();

        // POST 요청 생성
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl,
                response -> {
                    if (response.equals("success")) {
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                // 요청 파라미터 설정
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("userId", userId);
                params.put("password", password);
                return params;
            }
        };

        // 요청 큐에 추가
        requestQueue.add(request);
    }

    // 🔹 로그인 요청 처리 함수
    private void loginUser() {
        String userId = editUserId.getText().toString();
        String password = editPassword.getText().toString();

        // POST 요청 생성
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl,
                response -> {
                    if (response.equals("success")) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // 로그인 화면 종료

                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                // 요청 파라미터 설정
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("password", password);
                return params;
            }
        };

        // 요청 큐에 추가
        requestQueue.add(request);
    }
}
