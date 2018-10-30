package com.lightningant.explorer.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/6/4.
 */
public class LapAddressInfo {
    private String address;
    private BigDecimal valueLap;
    private int updateTime ;
    private String transationHash;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getValueLap() {
        return valueLap;
    }

    public void setValueLap(BigDecimal valueLap) {
        this.valueLap = valueLap;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }


    public String getTransationHash() {
        return transationHash;
    }

    public void setTransationHash(String transationHash) {
        this.transationHash = transationHash;
    }
}
