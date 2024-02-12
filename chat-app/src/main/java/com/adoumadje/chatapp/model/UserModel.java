package com.adoumadje.chatapp.model;

public class UserModel {
    private Long id;
    private String fullname;
    private String initial;
    private String status;
    private Boolean isActiveChat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsActiveChat() {
        return isActiveChat;
    }

    public void setIsActiveChat(Boolean activeChat) {
        isActiveChat = activeChat;
    }
}
