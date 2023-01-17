package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doctogo.model.Notification;
import com.doctogo.model.User;

public class NotificationDetailsActivity extends BaseActivity {
    private static final String TAG = NotificationDetailsActivity.class.getName();
    TextView textTitle;
    ProgressBar progressBar;
    private static final String EXTRAS_NOTIFICATION = "EXTRAS_NOTIFICATION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        Intent intent = getIntent();
        Log.i(TAG, "got intent" + intent );
        for( String key : intent.getExtras().keySet()) {
            Log.i(TAG, "got intent extra key name=" +key );
            Log.i(TAG, "got intent extra value=" +intent.getExtras().get(key) );
        }
        Notification n = (Notification) intent.getExtras().get(EXTRAS_NOTIFICATION);
        Log.i(TAG, " got notification to show details for: "+ n);
        showData(n);
    }
    private void showData(Notification notification) {
        textTitle.setText(notification.getStatus().toString());
    }
    public static void startNotificationDetailsActivity(Activity activity, Notification n) {
        Log.i(TAG, " ********** putting notification in the intent "+ n);
        Intent intent = new Intent(activity, NotificationDetailsActivity.class);
        intent.putExtra(EXTRAS_NOTIFICATION, n);
        activity.startActivity(intent);
    }
}