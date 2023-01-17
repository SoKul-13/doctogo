package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doctogo.model.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getName();
    private Button btnRequest;
    private EditText mTextEmailAddress;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPhone;
    private EditText mLongitude;
    private EditText mLatitude;
    private EditText mFirstName;
    private EditText mLastName;
    private boolean mDispatcherFlag;
    private boolean mHelperFlag;
    private boolean mNormalUser;
    private TextView mTxView;
    private String mResponse;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = Constants.USER_SERVICE_URL+"create";
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mTextEmailAddress = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mUsername = findViewById(R.id.username);
        mPhone = findViewById(R.id.phone);

        mLongitude = findViewById(R.id.longitude);
        mLatitude = findViewById(R.id.latitude);
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastname);
        mTxView = findViewById(R.id.textView);
        gson = new Gson();
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v){
                                              sendAndRequestResponse();
                                          }
                                      }

        );
        final RadioGroup radio = (RadioGroup) findViewById(R.id.radiogroup);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG , "********** on checked change ");
                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);
                Log.i(TAG , "********** index ="+index);
                switch (index) {
                    case 0: // first button
                        mDispatcherFlag = false;
                        mHelperFlag = true;
                        mNormalUser = false;
                        Log.i(TAG , "**********HELPER LETS GO ");
                        break;
                    case 1: // secondbutton
                        mDispatcherFlag = true;
                        mHelperFlag = false;
                        mNormalUser = false;
                        Log.i(TAG , "**********DISPATCHER LETS GO ");
                        break;
                    case 2: // secondbutton
                        mDispatcherFlag = false;
                        mHelperFlag = false;
                        mNormalUser = true;
                        Log.i(TAG , "********** Normal user LETS GO ");
                        break;
                }
            }
        });

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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.EMAIL_PARAM, String.valueOf(mTextEmailAddress.getText()));
                params.put("password", String.valueOf(mPassword.getText()));
                params.put("userName", String.valueOf(mUsername.getText()));
                params.put("firstName", String.valueOf(mFirstName.getText()));
                params.put("lastName", String.valueOf(mLastName.getText()));
                params.put("phone", String.valueOf(mPhone.getText()));
                params.put("longitude", String.valueOf(mLongitude.getText()));
                params.put("latitude", String.valueOf(mLatitude.getText()));
                params.put("dispatcherFlag", String.valueOf(mDispatcherFlag));
                params.put("helperFlag", String.valueOf(mHelperFlag));
                Log.i(TAG, " params passed "+ params);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("User Registration Failed")
                .setMessage("Check the data and please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
