package com.eggybytes.android.sendswithfriends;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.eggybytes.android.eggy_android.EggyClient;
import com.eggybytes.android.eggy_android.EggyConfig;
import com.eggybytes.android.eggy_android.EggyDevice;

import com.google.firebase.messaging.FirebaseMessaging;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EggyConfig config = new EggyConfig("YOUR_API_TOKEN");
        EggyDevice device = new EggyDevice();
        EggyClient.start(config, device);
        registerForPushNotifications();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("eggy_message").equals("true")) {
                new Thread() {
                    @Override
                    public void run() {
                        EggyClient.sendPushNotificationWasOpenedEventWithExtras(getIntent().getExtras());
                    }
                }.start();
            }
        }

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void registerForPushNotifications() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Exception while registering FCM token with eggy", task.getException());
                return;
            }

            final String token = task.getResult();
            new Thread() {
                @Override
                public void run() {
                    EggyClient.registerWithDeviceApi("", token);
                }
            }.start();
        });
    }
}
