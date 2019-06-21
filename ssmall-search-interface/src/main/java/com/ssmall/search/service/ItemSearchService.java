package com.ssmall.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索方法
     * @param searchMap
     * @return 将条件封装成Map后传过来
     */
    Map search(Map searchMap);

    /**
     * 导入列表
     * @param list
     */
    void importList(List list);

    /**
     * 删除商品列表
     * @param goodsIds （SPU ID）
     */
    void deleteByGoodsIds(List goodsIds);
}
