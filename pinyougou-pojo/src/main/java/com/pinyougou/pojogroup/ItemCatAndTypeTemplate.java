package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbTypeTemplate;

/**
 * 返回模板类型和分类的集合
 */
public class ItemCatAndTypeTemplate {

    private TbItemCat tbItemCat;
    private TbTypeTemplate tbTypeTemplate;

    public TbItemCat getTbItemCat() {
        return tbItemCat;
    }

    public void setTbItemCat(TbItemCat tbItemCat) {
        this.tbItemCat = tbItemCat;
    }

    public TbTypeTemplate getTbTypeTemplate() {
        return tbTypeTemplate;
    }

    public void setTbTypeTemplate(TbTypeTemplate tbTypeTemplate) {
        this.tbTypeTemplate = tbTypeTemplate;
    }
}
