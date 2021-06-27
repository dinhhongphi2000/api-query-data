package org.apiquery.shared.utils;

public class ModifiEmailForTest {
    private String subject;
    private String receiver;
    private String profile;

    public ModifiEmailForTest(String subject, String to, String profile) {
        this.subject = subject;
        this.receiver = to;
        this.profile = profile;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}