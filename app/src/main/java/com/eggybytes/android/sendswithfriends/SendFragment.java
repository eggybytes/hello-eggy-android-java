package com.eggybytes.android.sendswithfriends;

import static android.widget.CompoundButton.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class SendFragment extends Fragment {
    private static final String ARG_SEND_ID = "send_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Send send;
    private EditText titleField;
    private Button dateButton;
    private CheckBox sentCheckBox;

    public static SendFragment newInstance(UUID sendId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEND_ID, sendId);

        SendFragment fragment = new SendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID sendId = (UUID) getArguments().getSerializable(ARG_SEND_ID);
        send = SendStash.get(getActivity()).getSend(sendId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_send, container, false);

        this.titleField = (EditText) v.findViewById(R.id.send_title);
        this.titleField.setText(this.send.getTitle());
        this.titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                send.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        this.dateButton = (Button) v.findViewById(R.id.send_date);
        updateDate();
        this.dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(send.getDate());
                dialog.setTargetFragment(SendFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        this.sentCheckBox = (CheckBox) v.findViewById(R.id.send_sent);
        this.sentCheckBox.setChecked(this.send.isSent());
        this.sentCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                send.setSent(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            this.send.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        this.dateButton.setText(this.send.getDate().toString());
    }
}
