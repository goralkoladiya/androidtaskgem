package com.taskgem.Modal;

public class MyListData {
    String id,uid,rewards,reason,timestamp;
    int status;

    public MyListData() {
    }

    public MyListData(String id, String uid, String rewards, String reason, String timestamp,int status) {
        this.id = id;
        this.uid = uid;
        this.rewards = rewards;
        this.reason = reason;
        this.timestamp = timestamp;
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyListData{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", rewards='" + rewards + '\'' +
                ", reason='" + reason + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
