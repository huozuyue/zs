package com.hzy.model;

/**
 * Created by hzy on 2018/7/16.
 */

public class OrderInfo {
    int orderNo;
    String sendlocation = "";
    String detail_sendstr = "";
    String name_sendstr = "";
    String tel_sendstr = "";
    String receive_location = "";
    String receive_addressstr = "";
    String receive_namestr = "";
    String receive_phonestr = "";
    double sendlat;
    double sendlon;
    double receivelat;
    double receivelon;
    int dis;
    int dur;
    int taxiCost;
    String remarks_sendstr = "";

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getSendlocation() {
        return sendlocation;
    }

    public void setSendlocation(String sendlocation) {
        this.sendlocation = sendlocation;
    }

    public String getDetail_sendstr() {
        return detail_sendstr;
    }

    public void setDetail_sendstr(String detail_sendstr) {
        this.detail_sendstr = detail_sendstr;
    }

    public String getName_sendstr() {
        return name_sendstr;
    }

    public void setName_sendstr(String name_sendstr) {
        this.name_sendstr = name_sendstr;
    }

    public String getTel_sendstr() {
        return tel_sendstr;
    }

    public void setTel_sendstr(String tel_sendstr) {
        this.tel_sendstr = tel_sendstr;
    }

    public String getReceive_location() {
        return receive_location;
    }

    public void setReceive_location(String receive_location) {
        this.receive_location = receive_location;
    }

    public String getReceive_addressstr() {
        return receive_addressstr;
    }

    public void setReceive_addressstr(String receive_addressstr) {
        this.receive_addressstr = receive_addressstr;
    }

    public String getReceive_namestr() {
        return receive_namestr;
    }

    public void setReceive_namestr(String receive_namestr) {
        this.receive_namestr = receive_namestr;
    }

    public String getReceive_phonestr() {
        return receive_phonestr;
    }

    public void setReceive_phonestr(String receive_phonestr) {
        this.receive_phonestr = receive_phonestr;
    }

    public double getSendlat() {
        return sendlat;
    }

    public void setSendlat(double sendlat) {
        this.sendlat = sendlat;
    }

    public double getSendlon() {
        return sendlon;
    }

    public void setSendlon(double sendlon) {
        this.sendlon = sendlon;
    }

    public double getReceivelat() {
        return receivelat;
    }

    public void setReceivelat(double receivelat) {
        this.receivelat = receivelat;
    }

    public double getReceivelon() {
        return receivelon;
    }

    public void setReceivelon(double receivelon) {
        this.receivelon = receivelon;
    }

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    public int getDur() {
        return dur;
    }

    public void setDur(int dur) {
        this.dur = dur;
    }

    public int getTaxiCost() {
        return taxiCost;
    }

    public void setTaxiCost(int taxiCost) {
        this.taxiCost = taxiCost;
    }

    public String getRemarks_sendstr() {
        return remarks_sendstr;
    }

    public void setRemarks_sendstr(String remarks_sendstr) {
        this.remarks_sendstr = remarks_sendstr;
    }


}
