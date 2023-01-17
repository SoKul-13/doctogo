package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doctogo.model.User;
import com.google.gson.Gson;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private Button btnRequest;
    private Button btnRegister;
    private EditText mTextEmailAddress;
    private EditText mPassword;
    private TextView mTxView;
    private String mResponse;
    private Gson gson;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://10.0.2.2:8080/user/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gson = new Gson();
        mTextEmailAddress = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mTxView = findViewById(R.id.textView);

        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v){
                  sendAndRequestResponse();
              }
          }

        );
        btnRegister = findViewById(R.id.btnSignup);
        btnRegister.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v){
                      Handler handler = new Handler();
                      handler.postDelayed(() -> {
                          Intent i = new Intent(getBaseContext(), SignUpActivity.class);
                          startActivity(i);
                          finish();
                      }, 2000);
                  }
              }

        );
    }

    private void sendAndRequestResponse() {
        Log.i(TAG , "************************* sending request ");
        Log.i(TAG , "******************* email entered "+ mTextEmailAddress.getText());
        Log.i(TAG , "******************* pass entered "+ mPassword.getText());

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        Log.i(TAG, " Sending request to url :"+ url);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i(TAG, "response="+ response);
                        mResponse = response.toString();
                        User u  = gson.fromJson(response, User.class);
                        Log.i(TAG, " user ="+ u);
                        if(u!=null) {
                            Log.i(TAG, " is helper  ="+ u.isHelper());
                            Log.i(TAG, " is distatcher   ="+ u.isDispatcherFlag());
                            if(u.isDispatcherFlag()) {
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    Intent i = new Intent(getBaseContext(), DashboardActivity.class);
                                    i.putExtra(Constants.USER_INFO, u);
                                    startActivity(i);
                                    finish();
                                }, 2000);
                            }else if(u.isHelper() && !u.getAssignedNotifications().isEmpty()){ //Helper home
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    Intent i = new Intent(getBaseContext(), HelperDashboardActivity.class);
                                    i.putExtra(Constants.USER_INFO, u);
                                    startActivity(i);
                                    finish();
                                }, 2000);
                            }else{//Semd to notification home for user
                                Log.i(TAG, " user notifications ="+ u.getNotifications());
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {
                                    Intent i = new Intent(getBaseContext(), NotificationsHomeActivity.class);
                                    i.putExtra(Constants.USER_INFO, u);
                                    startActivity(i);
                                    finish();
                                }, 2000);
                            }
                        }else{
                            Log.i(TAG," Login failed. User does not exist . Retry ");
                            showErrorDialog();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG," Login Error :" + error.toString());
                        showErrorDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.EMAIL_PARAM, String.valueOf(mTextEmailAddress.getText()));
                params.put(Constants.PASS_PARAM, String.valueOf(mPassword.getText()));
                Log.i(TAG, " params passed "+ params);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}