package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.doctogo.model.Notification;
import com.doctogo.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class NotificationsHomeActivity extends BaseActivity {
    private static final String TAG = NotificationsHomeActivity.class.getName();
    private Gson gson;
    private User mLoggedInUser;
    private NotificationListAdaptor adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaiton_home);
        Intent intent = getIntent();
        Log.i(TAG, "got intent" + intent );
        mLoggedInUser  = (User) intent.getSerializableExtra(Constants.USER_INFO);
        if(mLoggedInUser.getNotifications() != null){
            Log.i(TAG, "got notifications:" + mLoggedInUser.getNotifications() );
            for(Notification n : mLoggedInUser.getNotifications()){
                Log.i(TAG , " note ="+ n.getStatus());
            }
            //adapter = new NotificationListAdaptor();
            adapter = new NotificationListAdaptor(n ->
                    NotificationDetailsActivity.startNotificationDetailsActivity(this, n));

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            refreshLayout = findViewById(R.id.refresh);
            refreshLayout.setOnRefreshListener(this::loadData);

            loadData();
        }else{
            TextView tv = new TextView(this);
            tv.setText(" You have not created any notifications yet. ");
            ConstraintLayout mLayout =findViewById(R.id.nContainer);
            mLayout.addView(tv);
        }
    }
    private void loadData() {
        Log.i(TAG, " Load data called ");
        refreshLayout.setRefreshing(false);
        adapter.submitList(new ArrayList<Notification>(mLoggedInUser.getNotifications()));
    }

    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "Error during loading notificaiton details ", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.orange));
        snackbar.setAction("Retry", v -> {
            loadData();
            snackbar.dismiss();
        });
        snackbar.show();
    }
}