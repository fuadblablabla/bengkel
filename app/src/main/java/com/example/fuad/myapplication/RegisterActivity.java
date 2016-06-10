package com.example.fuad.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btRegister;
    EditText etUsername, etPassword, etNama,etAlamat, etTelpon, etJsonResponse;
    RadioGroup rgKelamin;
    RadioButton rbTerpilih;
    Map<String, String> jsonParams = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btRegister = (Button) findViewById(R.id.btRegisterReg);
        etUsername = (EditText) findViewById(R.id.etUsernameReg);
        etPassword = (EditText) findViewById(R.id.etPasswordReg);
        etNama = (EditText) findViewById(R.id.etNamaReg);
        etAlamat = (EditText) findViewById(R.id.etAlamatReg);
        etTelpon = (EditText) findViewById(R.id.etTelponReg);
        etJsonResponse = (EditText) findViewById(R.id.etJsonResponseReg);
        rgKelamin = (RadioGroup) findViewById(R.id.rgKelamin);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgKelamin.getCheckedRadioButtonId();
                rbTerpilih = (RadioButton) findViewById(selectedId);
                jsonParams.put("username", etUsername.getText().toString());
                jsonParams.put("password", etPassword.getText().toString());
                jsonParams.put("nama", etNama.getText().toString());
                jsonParams.put("jenis_kelamin", rbTerpilih.getText().toString());
                jsonParams.put("alamat", etAlamat.getText().toString());
                jsonParams.put("telepon", etTelpon.getText().toString());
                prosesRegister();
            }
        });

    }

    private void prosesRegister() {
        String url = "http://bengkel.median.web.id/auth/register";
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    etJsonResponse.setText(response.getString("status_message") + ", " + data.getString("jenis_kelamin"));

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
}
