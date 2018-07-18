package com.hzy.enumclass;

/**
 * Created by hzy on 2018/7/16.
 */

public enum OrderStatus {
    /**
     * 待支付
     */
    daizhifu("待支付"),
    /**
     * 待抢单
     */
    daiqiangdan("待抢单"),
    /**
     * 进行中
     */
    jinxingzhong("进行中"),
    /**
     * 异常
     */
    yichang("异常"),

    yiwancheng("已完成");

    private String state;

    private OrderStatus(String state) {
        this.state = state;
    }

    /**
     * 根据状态字符串获取token状态枚举对象
     *
     * @param
     * @return
     */
    public static OrderStatus getOrderState(int OrderS) {
        //OrderStatus
        int[] states = {0, 1, 2, 3};
        if (OrderS == 0) {
            return OrderStatus.daizhifu;
        } else if (OrderS == 1) {
            return OrderStatus.daiqiangdan;
        } else if (OrderS == 2) {
            return OrderStatus.jinxingzhong;
        } else if (OrderS == 3) {
            return OrderStatus.yiwancheng;
        } else {
            return OrderStatus.yichang;
        }

    }

    public String toString() {
        return this.state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
