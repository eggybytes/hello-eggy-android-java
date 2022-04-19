package com.eggybytes.android.sendswithfriends;

import androidx.fragment.app.Fragment;

public class SendListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SendListFragment();
    }
}
