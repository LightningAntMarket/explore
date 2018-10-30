package com.lightningant.explorer.config;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/4/12.
 */
public class LAPStringConst {
    public static final String MINER_ADDRESS="";
    public static final String TEAM_ADDRESS="";
    public static final String OPERATION_ADDRESS="";
    public static final String BLACKHOLE_ADDRESS="";
    public static final String LAP_IN_OUT_ADDRESS="";
    public static final String LAP_20_IN_OUT_ADDRESS="";

    public static  double ERC20_BALANCE=0;


    public static String formatTosepara(double data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);
    }
}
