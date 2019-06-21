package com.ssmall.cart.service;

import com.ssmall.pojogroup.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 添加商品到购物车列表
     *
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 从redis中提取购物车列表
     *
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车列表存入redis中
     *
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username, List<Cart> cartList);

    /**
     * 合并购物车
     *
     * @param cartList1
     * @param cartList2
     * @return
     */
    List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);
}
