package com.ruoyi.project.system.item.mapper;

import com.ruoyi.project.system.item.domain.HdCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper
@Repository
public interface HdCategoryMapper {
    HdCategory selectByPrimaryKey(Long categoryId);

    /**
     * 根据id集合查询存在的id
     *
     * @param collect id集合
     * @return
     */
    List<Long> selectIdByIds(@Param("collect") Collection<Long> collect);

    /**
     * 根据id更新数据
     *
     * @param hdCategory 实体
     * @return ~
     */
    int update(HdCategory hdCategory);

    /**
     * 插入数据
     *
     * @param hdCategory 实体
     * @return ~
     */
    int insert(HdCategory hdCategory);
}