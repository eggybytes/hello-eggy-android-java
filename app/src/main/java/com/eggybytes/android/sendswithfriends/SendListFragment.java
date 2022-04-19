package com.eggybytes.android.sendswithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SendListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView sendRecyclerView;
    private SendAdapter adapter;
    private boolean subtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_list, container, false);

        this.sendRecyclerView = (RecyclerView) view.findViewById(R.id.send_recycler_view);
        this.sendRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            this.subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, this.subtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_send_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (this.subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_send:
                Send send = new Send();
                SendStash.get(getActivity()).addSend(send);
                Intent intent = SendPagerActivity
                        .newIntent(getActivity(), send.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                this.subtitleVisible = !this.subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        SendStash sendStash = SendStash.get(getActivity());
        int sendCount = sendStash.getSends().size();
        String subtitle = getString(R.string.subtitle_format, sendCount);

        if (!this.subtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        SendStash sendStash = SendStash.get(getActivity());
        List<Send> sends = sendStash.getSends();

        if (this.adapter == null) {
            this.adapter = new SendAdapter(sends);
            sendRecyclerView.setAdapter(adapter);
        } else {
            this.adapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class SendHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView;
        private TextView dateTextView;
        private ImageView sentImageView;
        private Send send;

        public SendHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_send, parent, false));
            itemView.setOnClickListener(this);

            this.titleTextView = (TextView) itemView.findViewById(R.id.send_title);
            this.dateTextView = (TextView) itemView.findViewById(R.id.send_date);
            this.sentImageView = (ImageView) itemView.findViewById(R.id.send_sent);
        }

        public void bind(Send send) {
            this.send = send;
            this.titleTextView.setText(this.send.getTitle());
            this.dateTextView.setText(this.send.getDate().toString());
            this.sentImageView.setVisibility(this.send.isSent() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = SendPagerActivity.newIntent(getActivity(), this.send.getId());
            startActivity(intent);
        }
    }

    private class SendAdapter extends RecyclerView.Adapter<SendHolder> {
        private List<Send> sends;

        public SendAdapter(List<Send> sends) {
            this.sends = sends;
        }

        @Override
        public SendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new SendHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SendHolder holder, int position) {
            Send send = this.sends.get(position);
            holder.bind(send);
        }

        @Override
        public int getItemCount() {
            return this.sends.size();
        }
    }
}
