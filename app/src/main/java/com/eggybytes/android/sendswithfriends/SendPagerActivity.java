package com.eggybytes.android.sendswithfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class SendPagerActivity extends AppCompatActivity {
    private static final String EXTRA_SEND_ID = "com.eggybytes.android.sendswithfriends.send_id";

    private ViewPager viewPager;
    private List<Send> sends;

    public static Intent newIntent(Context packageContext, UUID sendId) {
        Intent intent = new Intent(packageContext, SendPagerActivity.class);
        intent.putExtra(EXTRA_SEND_ID, sendId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pager);

        UUID sendId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_SEND_ID);

        this.viewPager = (ViewPager) findViewById(R.id.send_view_pager);

        this.sends = SendStash.get(this).getSends();
        FragmentManager fragmentManager = getSupportFragmentManager();
        this.viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Send send = sends.get(position);
                return SendFragment.newInstance(send.getId());
            }

            @Override
            public int getCount() {
                return sends.size();
            }
        });

        for (int i = 0; i < this.sends.size(); i++) {
            if (this.sends.get(i).getId().equals(sendId)) {
                this.viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
