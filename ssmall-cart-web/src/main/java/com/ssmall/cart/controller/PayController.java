package com.ssmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ssmall.pay.service.WeixinPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.IdWorker;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @RequestMapping("/createNative")
    public Map createNative() {
        IdWorker idWorker = new IdWorker();
        return weixinPayService.createNative(idWorker.nextId() + "", "1");
    }
}
