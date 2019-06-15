package com.ssmall.sellergoods.service;

import com.ssmall.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {

    /**
     * 查找所有品牌
     *
     * @return 返回全部品牌
     */
    public List<TbBrand> findAll();


    /**
     * 品牌数据分类
     *
     * @param pageNum  当前第几页
     * @param pageSize 每页记录数
     * @return 品牌分页结果
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 按条件查询品牌
     *
     * @param pageNum  当前第几页
     * @param pageSize 每页记录数
     * @return 品牌分页结果
     */
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    /**
     * 品牌增加功能
     *
     * @param brand
     */
    public void add(TbBrand brand);

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
   public TbBrand findOne(Long id);


    /**
     * 从查询到的数据进行修改
     *
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);


    /**
     * 返回下拉列表数据
     * @return
     */
    public List<Map> selectOptionList();
}
