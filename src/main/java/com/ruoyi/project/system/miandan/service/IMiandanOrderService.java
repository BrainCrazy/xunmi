package com.ruoyi.project.system.miandan.service;

import com.ruoyi.project.system.item.domain.HdItem;
import com.ruoyi.project.system.miandan.domain.MiandanOrder;

import java.util.List;

/**
 * 纯贴面单Service接口
 *
 * @author wqf
 * @date 2020-02-14
 */
public interface IMiandanOrderService {

    int updateStatusById(long id, int sourceStatus, int targetStatus);

    /**
     * 查询纯贴面单
     *
     * @param id 纯贴面单ID
     * @return 纯贴面单
     */
    public MiandanOrder selectMiandanOrderById(Long id);

    /**
     * 查询纯贴面单列表
     *
     * @param miandanOrder 纯贴面单
     * @return 纯贴面单集合
     */
    public List<MiandanOrder> selectMiandanOrderList(MiandanOrder miandanOrder);

    /**
     * 新增纯贴面单
     *
     * @param miandanOrder 纯贴面单
     * @return 结果
     */
    public int insertMiandanOrder(MiandanOrder miandanOrder);

    /**
     * 修改纯贴面单
     *
     * @param miandanOrder 纯贴面单
     * @return 结果
     */
    public int updateMiandanOrder(MiandanOrder miandanOrder, int status);

    /**
     * 批量删除纯贴面单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMiandanOrderByIds(String ids);

    /**
     * 删除纯贴面单信息
     *
     * @param id 纯贴面单ID
     * @return 结果
     */
    int deleteMiandanOrderById(Long id);

    int queryCount(Long userId);

    List<HdItem> getorderDetail(Long id);

    void confirmOutStock(Long id);
}
