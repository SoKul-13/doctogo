package com.doctogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

    public class BaseActivity extends AppCompatActivity {
        private static final String TAG = BaseActivity.class.getName();
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu,menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Log.i(TAG, " ** Menu item clicked ");

            // Handle action bar actions click
            switch (item.getItemId()) {
                case R.id.menuSignOut:
                    Log.i(TAG, " *********** Logging out the user ");
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }