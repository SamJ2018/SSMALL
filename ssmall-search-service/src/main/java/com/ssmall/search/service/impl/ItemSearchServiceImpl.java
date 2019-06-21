package com.ssmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ssmall.pojo.TbItem;
import com.ssmall.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map =new HashMap();
        //空格处理
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));
        /*
        Query query=new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        map.put("rows",page.getContent());
        */

        //1、查询列表
        map.putAll(searchList(searchMap));

        //2、分组查询 商品分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);

        //3、查询品牌和规格列表
        String category = (String) searchMap.get("category");
        if(!category.equals("")){
            map.putAll(searchBrandAndSpecList(category));
        }else {
            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }

        return map;
    }

    /**
     * 查询列表方法
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){

        Map map =new HashMap();
        //高亮显示
        HighlightQuery query = new SimpleHighlightQuery();
        //构建高亮选项对象
        HighlightOptions highlightOptions=new HighlightOptions().addField("item_title");//高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>"); //前缀
        highlightOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(highlightOptions);//为查询对象设置高亮选项

        //1.1 关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2 按照商品分类过滤
        if(!"".equals(searchMap.get("category"))){//如果用户选择了分类
            FilterQuery filterquery = new SimpleFilterQuery();//过滤查询对象
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            filterquery.addCriteria(filterCriteria);
            query.addFilterQuery(filterquery);
        }

        //1.3 按品牌过滤
        if(!"".equals(searchMap.get("brand"))){//如果用户选择了品牌
            FilterQuery filterquery = new SimpleFilterQuery();//过滤查询对象
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            filterquery.addCriteria(filterCriteria);
            query.addFilterQuery(filterquery);
        }

        //1.4 按规格过滤
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                FilterQuery filterquery = new SimpleFilterQuery();//过滤查询对象
                Criteria filterCriteria=new Criteria("item_spec_"+key).is(specMap.get(key));
                filterquery.addCriteria(filterCriteria);
                query.addFilterQuery(filterquery);
            }
        }

        //1.5 按价格过滤
        if(!"".equals(searchMap.get("price"))){
            String[] prices = ((String) searchMap.get("price")).split("-");
            if(!prices[0].equals("0")){
                //如果其实价格不为0
                FilterQuery filterquery = new SimpleFilterQuery();//过滤查询对象
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(prices[0]);
                filterquery.addCriteria(filterCriteria);
                query.addFilterQuery(filterquery);
            }
            if(!prices[1].equals("*")){
                //如果其实价格不为0
                FilterQuery filterquery = new SimpleFilterQuery();//过滤查询对象
                Criteria filterCriteria=new Criteria("item_price").lessThanEqual(prices[1]);
                filterquery.addCriteria(filterCriteria);
                query.addFilterQuery(filterquery);
            }
        }

        //1.6 分页
        Integer pageNo = (Integer) searchMap.get("pageNo"); //获取页码
        if(pageNo==null)   pageNo=1;

        Integer pageSize = (Integer) searchMap.get("pageSize");
        if(pageSize==null) pageSize=20;

        query.setOffset((pageNo-1)*pageSize);//起始索引
        query.setRows(pageSize);//每页记录数


        //1.7 按价格排序
        String sortValue = (String) searchMap.get("sort"); //升序ASC 降序DESC
        String sortField= (String) searchMap.get("sortField");//排序字段
        if(sortValue!=null&&!sortValue.equals("")){
            if(sortValue.equals("ASC")){ //如果是升序，则
                Sort sort = new Sort(Sort.Direction.ASC, "item_"+sortField);
                query.addSort(sort);
            }
            if(sortValue.equals("DESC")){ //如果是升序，则
                Sort sort = new Sort(Sort.Direction.DESC, "item_"+sortField);
                query.addSort(sort);
            }
        }

        /**********************************获取高亮的结果集*******************************************/
        //返回高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口集合
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();

        for (HighlightEntry<TbItem> entry : entryList) {
            //获取高亮列表(高亮域的个数)
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
           /* for (HighlightEntry.Highlight highlight : highlights) {
                List<String> snipplets = highlight.getSnipplets(); //每个域有可能存取多个值
                System.out.println(snipplets);
            }*/
            if (highlights.size() > 0 && highlights.get(0).getSnipplets().size() > 0) {
                TbItem item = entry.getEntity();
                item.setTitle(highlights.get(0).getSnipplets().get(0));
            }
        }
        map.put("rows",page.getContent());
        map.put("totalPages",page.getTotalPages());//总页数
        map.put("total",page.getTotalElements());//总记录数
        return map;
        /**********************************获取高亮的结果集*******************************************/
    }

    /**
     * 分组查询（查询商品分类列表）
     *
     * @return
     */
    private List<String> searchCategoryList(Map searchMap) {
        List<String> list=new ArrayList();

        Query query=new SimpleQuery("*:*");
        //根据关键字查询 相当于where
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置分组选项，相当于指定了group by
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");

        query.setGroupOptions(groupOptions);

        //获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);//分组页
        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
        for (GroupEntry<TbItem> entry : entryList) {
            list.add(entry.getGroupValue());//将分组的结果添加到返回值中
        }
        return list;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询品牌和规格列表
     * @return
     * @param category 根据商品分类名称
     */
    private Map searchBrandAndSpecList(String category){
        Map map =new HashMap();
        //1、根据商品分类名称得到模板ID
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if(templateId!=null){
            //2、根据模板ID获取品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList",brandList);
            //3、根据模板ID获取规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList",specList);
        }
        return map;
    }

    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(List goodsIds) {

        Query query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_goodsid").in(goodsIds);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
