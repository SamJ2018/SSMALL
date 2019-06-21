package com.ssmall.page.service.impl;

import com.ssmall.mapper.TbGoodsDescMapper;
import com.ssmall.mapper.TbGoodsMapper;
import com.ssmall.mapper.TbItemCatMapper;
import com.ssmall.mapper.TbItemMapper;
import com.ssmall.page.service.ItemPageService;
import com.ssmall.pojo.TbGoods;
import com.ssmall.pojo.TbGoodsDesc;
import com.ssmall.pojo.TbItem;
import com.ssmall.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean genItemHtml(Long goodsId) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate("item.ftl");
            //创建数据模型
            Map dataMode = new HashMap();

            //商品主表数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            dataMode.put("goods", goods);

            //商品扩展表数据
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataMode.put("goodsDesc", goodsDesc);

            //读取商品分类
            String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            dataMode.put("itemCat1", itemCat1);
            dataMode.put("itemCat2", itemCat2);
            dataMode.put("itemCat3", itemCat3);

            //4、读取SKU列表数据
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);
            criteria.andStatusEqualTo("1");//状态有效
            example.setOrderByClause("is_default desc");//按是否默认字段字形降序排序，目的是返回的结果是第一条为默认SKU
            List<TbItem> itemsList = itemMapper.selectByExample(example);
            dataMode.put("itemList", itemsList);

            //输出对象
            Writer out = new FileWriter(pagedir + goodsId + ".html");
            template.process(dataMode, out);//输出
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteItemHtml(Long[] goodsIds) {
        try {
            for (Long goodsId : goodsIds) {
                new File(pagedir + goodsId + ".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
