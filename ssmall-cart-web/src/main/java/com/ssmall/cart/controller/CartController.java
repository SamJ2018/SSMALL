package com.ssmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.ssmall.cart.service.CartService;
import com.ssmall.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private HttpServletRequest request;

    @Reference(timeout = 6000)
    private CartService cartService;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9105")
    public Result addGoodsToCartList(Long itemId, Integer num) {

        /*response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105"); //允许访问的域
        response.setHeader("Access-Control-Allow-Credentials", "true");//如果操作cookie，必须加上这句话*/

        String name = SecurityContextHolder.getContext().getAuthentication().getName();//当前登录人的账号

        try {
            //从cookie中提取购物车
            List<Cart> cartList = findCartList();
            //调用服务方法操作购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if (name.equals("anonymousUser")) {//如果未登陆
                System.out.println("从cookie中提取购物车");
                //将新的购物车存入cooke
                String cartListString = JSON.toJSONString(cartList);
                CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
            } else {//如果登陆,存入redis中
                cartService.saveCartListToRedis(name, cartList);
                System.out.println("从redis中提取购物车");
            }
            return new Result(true, "存入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "存入购物车失败");
        }


    }

    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {//还可以在前端调用
        String username = SecurityContextHolder.getContext().getAuthentication().getName();//当前登录人的账号

        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");

        if (cartListString == null || cartListString.equals("")) {
            cartListString = "[]";
        }

        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        if (username.equals("anonymousUser")) {//如果未登陆
            //从cookie中提取购物车
            System.out.println("向cookie存储购物车");
            return cartList_cookie;
        } else {//如果已登陆
            System.out.println("向redis中存储购物车");
            //获取redis购物车
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            if (cartList_cookie.size() > 0) { //判断本地购物车中存在数据
                //得到合并后的购物车
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //将合并后的购物车存入redis
                cartService.saveCartListToRedis(username, cartList);
                //本地购物车清除
                CookieUtil.deleteCookie(request, response, "cartList");
                return cartList;
            }
            return cartList_redis;
        }
    }
}
