package com.ruoyi.project.system.item.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yibo.su
 * @version V1.0
 * @date 2020/3/19 1:37 下午
 */
public interface HdCategoryService {
    /**
     * 刷新类别信息
     *
     * @param shopId 店铺id
     */
    void refreshCategory(Long shopId);
}
