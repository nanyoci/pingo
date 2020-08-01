package com.example.pingo;

public class ChatList {
    private int unread;
    private String friend;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public ChatList( String friend,int unread) {
        this.unread = unread;
        this.friend = friend;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "ChatList{" +
                "unread=" + unread +
                ", friend='" + friend + '\'' +
                '}';
    }
}
