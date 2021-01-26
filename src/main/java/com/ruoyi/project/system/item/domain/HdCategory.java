package com.ruoyi.project.system.item.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * hd_category
 * @author 
 */
@Data
public class HdCategory implements Serializable {
    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 是否有子集0-false,1-true
     */
    private Boolean hasChildren;

    /**
     * 父类id
     */
    private Long parentId;

    /**
     * dts最小值
     */
    private Integer minLimit;

    /**
     * dts最大值
     */
    private Integer maxLimit;

    private static final long serialVersionUID = 1L;


    @Override
    public int hashCode() {
        int result = categoryId != null ? categoryId.hashCode() : 0;
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (hasChildren != null ? hasChildren.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (minLimit != null ? minLimit.hashCode() : 0);
        result = 31 * result + (maxLimit != null ? maxLimit.hashCode() : 0);
        return result;
    }
}