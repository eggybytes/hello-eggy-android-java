package com.eggybytes.android.sendswithfriends;

import java.util.Date;
import java.util.UUID;

public class Send {
    private UUID id;
    private String title;
    private Date date;
    private boolean sent;

    public Send() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public UUID getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSent() {
        return this.sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
