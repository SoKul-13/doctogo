package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;
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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DashboardActivity extends BaseActivity {
    private static final String TAG = DashboardActivity.class.getName();

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Gson gson;

    private User mLoggedInUser;
    private Button mButton,mButtonLoadData;
    private int nNotificationId ;
    private List<Notification> mNotifications;
    private ConstraintLayout mLayout;

    private RadioGroup mRadioGrp ;
    private TextView mNoNotificationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.menu.main_menu){
            Log.i(TAG, " logging out user ");
            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mLayout =findViewById(R.id.dContainer);
        gson = new Gson();

        Intent intent = getIntent();
        Log.i(TAG, "got intent" + intent );

        mLoggedInUser  = (User) intent.getSerializableExtra(Constants.USER_INFO);
        Log.i(TAG, " intent user email =" + mLoggedInUser.getEmail() );
        mButton = findViewById(R.id.btnFindHelpers);
        mButton.setVisibility(View.INVISIBLE);
        mButton.setEnabled(false);

        mButtonLoadData = findViewById(R.id.btnLoadData);
        mButtonLoadData.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v){
                       getNotificationsFromServer();
                   }
               }
        );

        mButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v){
                   acceptNotificationToWorkOn();
               }
           }
        );
    }

    private RadioGroup createRadioButtonsForNotifications(){
        RadioGroup notificationRadioGrp = new RadioGroup(this);
        notificationRadioGrp.setOrientation(RadioGroup.VERTICAL);
        //Get first 10 new notifications and create radio buttons for them
        //Add radio buttons in the group for notifications
        RadioGroup.LayoutParams lp;
        boolean firstSelected = false;
        for(Notification n : mNotifications){
            RadioButton radioBtn = new RadioButton(this);
            switch(n.getNotificationType()){
                case 0 :
                    radioBtn.setText(NotificationType.HIGH_BLOOD_PRESSURE.toString()+  " : "+ n.getBloodPressure());
                    break;
                case 1 :
                    radioBtn.setText(NotificationType.LOW_BLOOD_PRESSURE.toString()+  " : "+ n.getBloodPressure());
                    break;
                case 2 :
                    radioBtn.setText(NotificationType.HIGH_BLOOD_OXYGEN.toString()+  " : "+ n.getOxygenSaturation());
                    break;
                case 3 :
                    radioBtn.setText(NotificationType.LOW_BLOOD_OXYGEN.toString()+  " : "+ n.getOxygenSaturation());
                    break;
                case 4 :
                    radioBtn.setText(NotificationType.HIGH_HEART_RATE.toString()+  " : "+ n.getHeartBeat());
                    break;
                case 5 :
                    radioBtn.setText(NotificationType.LOW_HEART_RATE.toString()+  " : "+ n.getHeartBeat());
                    break;
            }
            radioBtn.setText(radioBtn.getText() + " : "+ n.getStatus()) ;

            radioBtn.setTag(n.getId());
            Log.i(TAG , "********** button created  "+ n.getId());
            if(!firstSelected){
                nNotificationId = n.getId();
                firstSelected = true;
                radioBtn.setSelected(true);
                Log.i(TAG , "**** first button selected  "+ radioBtn.getText());
            }
            lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.MATCH_PARENT);
            notificationRadioGrp.addView(radioBtn, lp);
            Log.i(TAG , "********** button added to the group ");
        }
        Log.i(TAG , "********** done adding buttons to group ");
        notificationRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG , "********** selected notification "+ checkedId);
                View radioButton = notificationRadioGrp.findViewById(checkedId);
                nNotificationId = (int)radioButton.getTag();
                Log.i(TAG , "********** selected notificationId "+ nNotificationId);
            }
        });
        return notificationRadioGrp;
    }

    private void startHelperFinderActivity(List<User> helpers){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(getBaseContext(), HelperFinderActivity.class);
            i.putExtra(Constants.USER_INFO, mLoggedInUser);
            i.putExtra(Constants.NOTIF_INFO, nNotificationId);
            i.putExtra(Constants.USER_LIST, (Serializable) helpers);

            startActivity(i);
            finish();
        }, 2000);
    }

    private void getNotificationsFromServer(){
        Log.i(TAG , "*************************getting all new notifications  ");
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        Log.i(TAG, " Sending request to url :"+ Constants.GET_ALL_NOTIFICATIONS_SERVICE_URL);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET,
                Constants.GET_ALL_NOTIFICATIONS_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response="+ response);
                Type listType = new TypeToken<List<Notification>>() {}.getType();
                mNotifications  = gson.fromJson(response, listType);
                Log.i(TAG, " Server sent notifications   ="+ mNotifications);
                if(!mNotifications.isEmpty()) {
                    if(mNoNotificationView !=null) {
                        mLayout.removeView(mNoNotificationView);
                    }
                    RadioGroup rg = createRadioButtonsForNotifications();
                    mLayout.addView(rg, 1);
                    mButton.setEnabled(true);
                    mButton.setVisibility(View.VISIBLE);
                    mButtonLoadData.setVisibility(View.INVISIBLE);
                }else{
                    mNoNotificationView = new TextView(mLayout.getContext());
                    mNoNotificationView.setText(" No new notifications...");
                    mLayout.addView(mNoNotificationView);
                    mButton.setEnabled(false);
                    mButton.setVisibility(View.GONE);
                }
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

    private void acceptNotificationToWorkOn() {
        Log.i(TAG , "********** Assigning dispatcher to notification ");
        Log.i(TAG , "******************* nNotificationId "+ nNotificationId);
        Log.i(TAG , "******************* dispatcher id ");

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        Log.i(TAG, " Sending request to url :"+ Constants.ACCEPT_AND_FIND_HELPERS_SERVICE_URL);
        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, Constants.ACCEPT_AND_FIND_HELPERS_SERVICE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i(TAG, " response from server  :"+ response);
                    if(response != null && !response.equals("null")) {
                        Type listType = new TypeToken<List<User>>() { }.getType();
                        List<User> helpers = gson.fromJson(response, listType);
                        Log.i(TAG, " found helpers  =" + helpers);
                        Log.i(TAG, " sending user to helper finder home  ");
                        startHelperFinderActivity(helpers);
                    } else{
                        Log.i(TAG, " error from server while assigning the dispatcher ");
                        showAssignFailedDialog();
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG," Error accepting the notification for dispatcher :" + error.toString());
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.N_ID_PARAM, String.valueOf(nNotificationId));
                params.put(Constants.DISPATCHER_ID_PARAM, String.valueOf(mLoggedInUser.getId()));
                Log.i(TAG, " params passed "+ params);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void showAssignFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Failed to assign the dispatcher to notificaiton")
                .setMessage(" Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}