package com.ruoyi.project.system.unpackage.service;

import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 拆包订单Service接口
 *
 * @author wqf
 * @date 2020-02-15
 */
public interface IUnpackingOrderService {
    List<UnpackingOrder> selectByShopeeOrderNos(String[] orderIds);

    boolean confirmOutStock(String shopeeOrderNo, MultipartFile stockImage, String weight) throws IOException;

    int updateStatusByShopeeOrderNo(String shopeeOrderNo, int packageStatus);

    /**
     * 查询拆包订单
     *
     * @param id 拆包订单ID
     * @return 拆包订单
     */
    public UnpackingOrder selectUnpackingOrderById(Long id);

    /**
     * 查询拆包订单列表
     *
     * @param unpackingOrder 拆包订单
     * @return 拆包订单集合
     */
    public List<UnpackingOrder> selectUnpackingOrderList(UnpackingOrder unpackingOrder);

    /**
     * 新增拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    public int insertUnpackingOrder(UnpackingOrder unpackingOrder);

    /**
     * 修改拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    public int updateUnpackingOrder(UnpackingOrder unpackingOrder);

    /**
     * 批量删除拆包订单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUnpackingOrderByIds(String ids);

    /**
     * 删除拆包订单信息
     *
     * @param id 拆包订单ID
     * @return 结果
     */
    public int deleteUnpackingOrderById(Long id);

    int queryCount(Long userId);

    void userSubmitOrder(String shopeeOrderNo);
}
