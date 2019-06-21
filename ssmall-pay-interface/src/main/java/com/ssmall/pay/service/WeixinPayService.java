package com.ssmall.pay.service;

import java.util.Map;

public interface WeixinPayService {

    /**
     * 生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    Map createNative(String out_trade_no, String total_fee);
}
