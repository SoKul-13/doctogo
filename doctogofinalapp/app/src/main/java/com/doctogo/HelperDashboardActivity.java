package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.doctogo.model.NotificationStatus;
import com.doctogo.model.NotificationType;
import com.doctogo.model.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HelperDashboardActivity extends BaseActivity {
    private static final String TAG = HelperDashboardActivity.class.getName();

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String ACCEPT_NOTIF_SERVICE_URL = Constants.HELPER_SERVICE_URL
            +"acceptNotification";
    private Gson gson;

    private User mLoggedInUser;
    private Button mButton;
    private int nNotificationId ;
    private Set<Notification> mNotifications ;
    private ConstraintLayout mLayout;
    private int mIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_dashboard);
        mLayout =findViewById(R.id.hdContainer);
        gson = new Gson();

        Intent intent = getIntent();
        Log.i(TAG, "got intent" + intent );

        mLoggedInUser  = (User) intent.getSerializableExtra(Constants.USER_INFO);
        Log.i(TAG, " intent user email =" + mLoggedInUser.getEmail() );
        mButton = findViewById(R.id.btnAccpet);
        mButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v){
                                           acceptNotificationToWorkOn();
                                       }
                                   }
        );

        if(mLoggedInUser.getAssignedNotifications() != null && !mLoggedInUser.getAssignedNotifications().isEmpty()){
            mNotifications = mLoggedInUser.getAssignedNotifications();

            Log.i(TAG, " helper assigned  notifications   ="+ mNotifications);
            RadioGroup rg = createRadioButtonsForNotifications();
            mLayout.addView(rg,1);
        }else{
            TextView tv = new TextView(this);
            tv.setText(" No notifications assigned to you");
            mLayout.addView(tv);
            mButton.setEnabled(false);
            mButton.setVisibility(View.GONE);
        }
    }
    private RadioGroup createRadioButtonsForNotifications(){
        RadioGroup notificationRadioGrp = new RadioGroup(this);
        notificationRadioGrp.setOrientation(RadioGroup.VERTICAL);

        RadioGroup.LayoutParams lp;
        boolean firstSelected = false;
        for(Notification n : mNotifications){
            if(n.getStatus() == NotificationStatus.HELPER_ASSIGNED) {
                RadioButton radioBtn = new RadioButton(this);
                radioBtn.setText(NotificationType.LOW_BLOOD_PRESSURE.toString());
                switch (n.getNotificationType()) {
                    case 0:
                    case 1:
                        radioBtn.setText(radioBtn.getText() + " : " + n.getBloodPressure());
                        break;
                    case 2:
                    case 3:
                        radioBtn.setText(radioBtn.getText() + " : " + n.getOxygenSaturation());
                        break;
                    case 4:
                    case 5:
                        radioBtn.setText(radioBtn.getText() + " : " + n.getHeartBeat());
                        break;
                }
                radioBtn.setText(radioBtn.getText() + " : "+ n.getStatus()) ;
                radioBtn.setTag(n.getId());
                Log.i(TAG, "********** button created  " + n.getId());
                if (!firstSelected) {
                    nNotificationId = n.getId();
                    firstSelected = true;
                    radioBtn.setSelected(true);
                    Log.i(TAG, "**** first button selected  " + radioBtn.getText());
                }
                lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.MATCH_PARENT);
                notificationRadioGrp.addView(radioBtn, lp);
                Log.i(TAG, "********** button added to the group ");
            }
        }
        Log.i(TAG , "********** done adding buttons to group ");
        notificationRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG , "********** selected notification "+ checkedId);
                View radioButton = notificationRadioGrp.findViewById(checkedId);
                nNotificationId = (int)radioButton.getTag();
                mIndex = checkedId;
                Log.i(TAG , "********** selected notificationId "+ nNotificationId);
            }
        });
        return notificationRadioGrp;
    }

    private void acceptNotificationToWorkOn() {
        Log.i(TAG , "********** accepted notification for helper ");
        Log.i(TAG , "******************* nNotificationId "+ nNotificationId);
        Log.i(TAG , "******************* helper id ");

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        Log.i(TAG, " Sending request to url :"+ ACCEPT_NOTIF_SERVICE_URL);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, ACCEPT_NOTIF_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, " response from server  :"+ response);
                //Check response, send to dashboard on success
                if(response!=null && response.equals("true")){
                    Log.i(TAG, " Successfully accpted the notification for helper ");
                    //Remove it from the working list
                    mNotifications.remove(mIndex);
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
                        Log.i(TAG," Error accepting the notification for helper :" + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.N_ID_PARAM, String.valueOf(nNotificationId));
                params.put(Constants.HELPER_ID_PARAM, String.valueOf(mLoggedInUser.getId()));
                Log.i(TAG, " params passed "+ params);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void showAssignFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Failed to assign helper to notificaiton")
                .setMessage(" Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}