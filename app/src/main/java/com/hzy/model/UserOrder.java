package com.hzy.model;


public class UserOrder {


    String receive_location = "";
    String sendlocation = "";
    private String CREATETIME;
    private String UPDATETIME;
    private String username = "aaa", servicename = "bbb";
    private int orderNo, status;
    private String committime;

    public String getCommittime() {
        return committime;
    }

    public void setCommittime(String committime) {
        this.committime = committime;
    }

    public String getReceive_location() {
        return receive_location;
    }

    public void setReceive_location(String receive_location) {
        this.receive_location = receive_location;
    }

    public String getSendlocation() {
        return sendlocation;
    }

    public void setSendlocation(String sendlocation) {
        this.sendlocation = sendlocation;
    }


    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getUPDATETIME() {
        return UPDATETIME;
    }

    public void setUPDATETIME(String UPDATETIME) {
        this.UPDATETIME = UPDATETIME;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
