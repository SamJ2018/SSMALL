package com.ssmall.pojogroup;

import com.ssmall.pojo.TbSpecification;
import com.ssmall.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * 规格组合的实体类
 */
public class Specification implements Serializable {

    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptionList;

    public Specification() {}

    public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptionList) {
        this.specification = specification;
        this.specificationOptionList = specificationOptionList;
    }

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
