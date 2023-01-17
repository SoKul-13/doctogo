package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doctogo.model.Notification;
import com.doctogo.model.NotificationType;
import com.doctogo.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HelperFinderActivity extends BaseActivity {
    private static final String TAG = HelperFinderActivity.class.getName();
    private Gson gson;
    private User mLoggedInUser;
    private Button mButton;
    private int nNotificationId ;
    private int mHelperId;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String SERVICE_URL = Constants.DISPATCHER_SERVICE_URL
            +"findHelpers?notificationId=";
    private static final String ASSIGN_HELPER_SERVICE_URL = Constants.DISPATCHER_SERVICE_URL
            +"assignHelper";

    private List<User> mHelpers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_finder);

        ConstraintLayout thisLayout =findViewById(R.id.hContainer);
        gson = new Gson();

        Intent intent = getIntent();
        Log.i(TAG, "got intent" + intent );

        mLoggedInUser  = (User) intent.getSerializableExtra(Constants.USER_INFO);
        Log.i(TAG, " intent user email =" + mLoggedInUser.getEmail() );
        nNotificationId = intent.getIntExtra(Constants.NOTIF_INFO, -1);
        Log.i(TAG, "got notification id " + nNotificationId );
        mHelpers = (List<User>) intent.getSerializableExtra(Constants.USER_LIST);


        //mRadioGrp = findViewById(R.id.dRradiogroup);
        RadioGroup rg = createRadioButtonsForHelpers();
        thisLayout.addView(rg,1);

        mButton = findViewById(R.id.btnAssignHelpers);
        mButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v){
                   assignHelperToNotification();
               }
           }
        );
    }

    private RadioGroup createRadioButtonsForHelpers(){
        RadioGroup rdGrp = new RadioGroup(this);
        rdGrp.setOrientation(RadioGroup.VERTICAL);
        //Get first 10 new notifications and create radio buttons for them
        //Add radio buttons in the group for notifications
        RadioGroup.LayoutParams lp;
        boolean firstSelected = false;
        for(User helper : mHelpers){
            RadioButton radioBtn = new RadioButton(this);
            radioBtn.setText(helper.getFirstName() + ","+ helper.getLastName());
            radioBtn.setTag(helper.getId());
            Log.i(TAG , "********** button created  "+ helper.getId());
            lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.MATCH_PARENT);
            rdGrp.addView(radioBtn, lp);
            Log.i(TAG , "********** button added to the group ");
        }

        rdGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rdGrp.findViewById(checkedId);
                mHelperId = (int)radioButton.getTag();
                Log.i(TAG , "********** selected mHelperId "+ mHelperId);
            }
        });
        return rdGrp;
    }

    private void startDispatcherDashboardActivity(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(getBaseContext(), DashboardActivity.class);
            i.putExtra(Constants.USER_INFO, mLoggedInUser);
            startActivity(i);
            finish();
        }, 2000);
    }

    private void getHelpersFromServer(){
        List<User> users = new ArrayList<>();
        Log.i(TAG , "*************************getting all helpers ");
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        Log.i(TAG, " Sending request to url :"+ SERVICE_URL);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET,
                SERVICE_URL+nNotificationId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response="+ response);
                Type listType = new TypeToken<List<User>>() {}.getType();
                mHelpers  = gson.fromJson(response, listType);
                Log.i(TAG, " found helpers  ="+ mHelpers);
            }
        },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG," Error getting helpers :" + error.toString());
                    showErrorDialog();
                }
            }
        ) ;
        mRequestQueue.add(mStringRequest);
    }
    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Request to server failed")
                .setMessage(" Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void showAssignFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Failed to assign the helper to notificaiton")
                .setMessage(" Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void assignHelperToNotification() {
        Log.i(TAG , "********** Assigning helper to notification ");
        Log.i(TAG , "******************* nNotificationId "+ nNotificationId);
        Log.i(TAG , "******************* helper id "+ mHelperId);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        Log.i(TAG, " Sending request to url :"+ ASSIGN_HELPER_SERVICE_URL);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, ASSIGN_HELPER_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, " response from server  :"+ response);
                //Check response, send to dashboard on success
                if(response!=null && response.equals("true")){
                    Log.i(TAG, " sending user to dashboard ");
                    startDispatcherDashboardActivity();
                }else{
                    Log.i(TAG, " error from server while assigning the helper ");
                    showAssignFailedDialog();
                }
            }
        },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG," Error assigning helper :" + error.toString());
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.N_ID_PARAM, String.valueOf(nNotificationId));
                params.put(Constants.HELPER_ID_PARAM, String.valueOf(mHelperId));
                Log.i(TAG, " params passed "+ params);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}