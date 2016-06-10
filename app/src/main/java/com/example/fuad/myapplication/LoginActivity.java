package com.example.fuad.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etJsonResponse;
    Button btLogin, btRegister;
    Map<String, String> jsonParams = new HashMap<String, String>();
    SharedPreferences sharedPreferences;

    String PREFS_NAME = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etJsonResponse = (EditText) findViewById(R.id.etJsonResponse);
        btLogin = (Button) findViewById(R.id.btLogin);
        btRegister = (Button) findViewById(R.id.btRegister);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParams.put("username", etUsername.getText().toString());
                jsonParams.put("password", etPassword.getText().toString());
                prosesLogin();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void prosesLogin() {
        String url = "http://bengkel.median.web.id/auth/login";
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    setSharedPrefs(data.getString("api_key"), data.getString("username"), data.getString("id"), data.getString("id_bengkel"), data.getString("role_id"));
                    etJsonResponse.setText("api_key: " + sharedPreferences.getString("api_key", "api_key Tidak Tersimpan di Shared Preferences") + "\n" +
                            "id: " + sharedPreferences.getString("id", "id Tidak Tersimpan di Shared Preferences") + "\n" +
                            "id_bengkel: " + sharedPreferences.getString("id_bengkel", "id_bengkel Tidak Tersimpan di Shared Preferences") + "\n" +
                            "role_id: " + sharedPreferences.getString("role_id", "role_id Tidak Tersimpan di Shared Preferences") + "\n" +
                            "username: " + sharedPreferences.getString("username", "username Tidak Tersimpan di Shared Preferences") + "\n");

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void setSharedPrefs(String api_key, String username, String id, String id_bengkel, String role_id) {
        SharedPreferences.Editor edSharedPreferences = sharedPreferences.edit();
        edSharedPreferences.putString("api_key", api_key);
        edSharedPreferences.putString("username", username);
        edSharedPreferences.putString("id", id);
        edSharedPreferences.putString("id_bengkel", id_bengkel);
        edSharedPreferences.putString("role_id", role_id);
        edSharedPreferences.commit();
    }


}
