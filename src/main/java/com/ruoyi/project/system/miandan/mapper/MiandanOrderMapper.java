package com.ruoyi.project.system.miandan.mapper;

import com.ruoyi.project.system.miandan.domain.MiandanOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 纯贴面单Mapper接口
 *
 * @author wqf
 * @date 2020-02-14
 */
public interface MiandanOrderMapper {
    /**
     * 更新状态
     *
     * @param id
     * @param sourceStatus
     * @param targetStatus
     * @return
     */
    int updateStatusById(@Param("id") long id, @Param("sourceStatus") int sourceStatus, @Param("targetStatus") int targetStatus);

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

    public List<MiandanOrder> selectMiandanOrderByPackageNo(@Param("packageNo") String packageNo, @Param("status") int status);

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
    public int updateMiandanOrder(MiandanOrder miandanOrder);

    /**
     * 删除纯贴面单
     *
     * @param id 纯贴面单ID
     * @return 结果
     */
    public int deleteMiandanOrderById(Long id);

    /**
     * 批量删除纯贴面单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMiandanOrderByIds(String[] ids);

    int queryCount(@Param("userId") Long userId);

    List<MiandanOrder> selectByShopeeOrderNos(String[] orderIds);

    public int updateExeFlag(@Param("updateTime") Date updateTime);
}
