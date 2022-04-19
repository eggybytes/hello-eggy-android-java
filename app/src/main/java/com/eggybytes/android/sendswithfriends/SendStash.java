package com.eggybytes.android.sendswithfriends;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendStash {
    private static SendStash sendStash;
    private List<Send> sends;

    public static SendStash get(Context context) {
        if (sendStash == null) {
            sendStash = new SendStash(context);
        }

        return sendStash;
    }

    private SendStash(Context context) {
        this.sends = new ArrayList<>();
    }

    public void addSend(Send s) {
        this.sends.add(s);
    }

    public List<Send> getSends() {
        return this.sends;
    }

    public Send getSend(UUID id) {
        for (Send send : this.sends) {
            if (send.getId().equals(id)) {
                return send;
            }
        }

        return null;
    }
}
