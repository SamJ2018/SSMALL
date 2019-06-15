package com.ssmall.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ssmall.mapper.TbBrandMapper;
import com.ssmall.pojo.TbBrand;
import com.ssmall.pojo.TbBrandExample;
import com.ssmall.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询所有品牌数据
     *
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 查询页面结果
     *
     * @param pageNum  当前第几页
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);/*page分页*/

        /*返回page对象*/
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);/*page分页*/

        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();

        if (brand != null) {
            if (brand.getName() != null && brand.getName().length() > 0) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
                criteria.andFirstCharLike("%" + brand.getFirstChar() + "%");
            }
        }

        /*返回page对象*/
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.insert(brand);
    }


    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand brand) {
        /*根据主键保存更新*/
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
