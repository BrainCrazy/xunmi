package com.ruoyi.project.system.unpackage.service.impl;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.system.StockInfoItem.domain.StockInfoItem;
import com.ruoyi.project.system.StockInfoItem.service.IStockInfoItemService;
import com.ruoyi.project.system.money.domain.HdUserMoney;
import com.ruoyi.project.system.money.mapper.HdUserMoneyMapper;
import com.ruoyi.project.system.unpackage.domain.UnpackingOrder;
import com.ruoyi.project.system.unpackage.mapper.UnpackingOrderMapper;
import com.ruoyi.project.system.unpackage.service.IUnpackingOrderService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.mapper.UserMapper;
import com.ruoyi.project.system.userpackage.domain.UserPackage;
import com.ruoyi.project.system.userpackage.mapper.UserPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 拆包订单Service业务层处理
 *
 * @author wqf
 * @date 2020-02-15
 */
@Service
public class UnpackingOrderServiceImpl implements IUnpackingOrderService {
    @Autowired
    private UnpackingOrderMapper unpackingOrderMapper;

    @Autowired
    private UserPackageMapper userPackageMapper;

    @Autowired
    private IStockInfoItemService stockInfoItemService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HdUserMoneyMapper hdUserMoneyMapper;

    @Override
    public List<UnpackingOrder> selectByShopeeOrderNos(String[] orderIds) {
        return unpackingOrderMapper.selectByShopeeOrderNos(orderIds);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean confirmOutStock(String shopeeOrderNo, MultipartFile file, String weight) throws IOException {
        UnpackingOrder unpackingOrder = unpackingOrderMapper.selectByShopeeOrderNos(new String[]{shopeeOrderNo}).get(0);
        unpackingOrder.setShopeeOrderNo(shopeeOrderNo);
        if (null != file) {
            String stockImage = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file);
            unpackingOrder.setOutstockImage(stockImage);
        }
        unpackingOrder.setPackageStatus(60);
        unpackingOrder.setOutstockTime(DateUtils.getNowDate());
        unpackingOrder.setWeight(weight);
        unpackingOrder.setExceptionFlag(10);
        unpackingOrderMapper.updateUnpackingOrderByShopeeOrderNo(unpackingOrder);
        //处理扣费逻辑
        long fee = 200;
        //查询最终的产品数量,每超过5个增加0.5元
        List<UserPackage> list = userPackageMapper.selectByShopeeOrderNoNotNull(shopeeOrderNo);
        List<StockInfoItem> items = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(shopeeOrderNo);
        long size = 0;
        if (!CollectionUtils.isEmpty(list)) {
            size += list.size();
        }
        if (!CollectionUtils.isEmpty(items)) {
            size += items.size();
        }
        if (size > 5) {
            if (size % 5 == 0) {
                fee += (size - 5) / 5 * 50;
            } else {
                fee += ((size - 5) / 5 + 1) * 50;
            }
        }
        //拍照+0.5元
        if (unpackingOrder.getPhotograph() == 20) {
            fee += 50;
        }
        //称重+0.5
        if (unpackingOrder.getWeightFlag() == 10) {
            fee += 50;
        }
        //上限为6元
        fee = Math.min(fee, 600);
        if (unpackingOrder.getFeeFlag() == 10) {
            User user = userMapper.selectUserById(unpackingOrder.getUserId());
            //新订单,执行扣费,过滤已扣费的历史订单
            //预扣增加的是3元,扣除的时候也是扣除3元
            user.setPreMoney(user.getPreMoney() - 300);
            user.setMoney(user.getMoney() - fee);
            //更新user表
            userMapper.updateUser(user);
            HdUserMoney hdUserMoney = new HdUserMoney();
            hdUserMoney.setAmount(fee);
            hdUserMoney.setCreateTime(DateUtils.getNowDate());
            hdUserMoney.setBalance(user.getMoney());
            hdUserMoney.setUserId(user.getUserId());
            hdUserMoney.setUserName(user.getUserName());
            //10-下单消费
            hdUserMoney.setChangeType(10);
            hdUserMoney.setRemark("拆包订单确认发货,扣款" + fee / 100.0 + "元");
            //入库余额变动
            hdUserMoneyMapper.insertHdUserMoney(hdUserMoney);
        }
        //库存订单确认出库
        stockInfoItemService.confirmOutStock(shopeeOrderNo);
        return true;
    }

    @Override
    public int updateStatusByShopeeOrderNo(String shopeeOrderNo, int packageStatus) {
        return unpackingOrderMapper.updateStatusByShopeeOrderNo(shopeeOrderNo, packageStatus);
    }

    /**
     * 查询拆包订单
     *
     * @param id 拆包订单ID
     * @return 拆包订单
     */
    @Override
    public UnpackingOrder selectUnpackingOrderById(Long id) {
        return unpackingOrderMapper.selectUnpackingOrderById(id);
    }

    /**
     * 查询拆包订单列表
     *
     * @param unpackingOrder 拆包订单
     * @return 拆包订单
     */
    @Override
    public List<UnpackingOrder> selectUnpackingOrderList(UnpackingOrder unpackingOrder) {
        return unpackingOrderMapper.selectUnpackingOrderList(unpackingOrder);
    }

    /**
     * 新增拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    @Override
    public int insertUnpackingOrder(UnpackingOrder unpackingOrder) {
        unpackingOrder.setCreateTime(DateUtils.getNowDate());
        return unpackingOrderMapper.insertUnpackingOrder(unpackingOrder);
    }

    /**
     * 修改拆包订单
     *
     * @param unpackingOrder 拆包订单
     * @return 结果
     */
    @Override
    public int updateUnpackingOrder(UnpackingOrder unpackingOrder) {
        unpackingOrder.setUpdateTime(DateUtils.getNowDate());
        return unpackingOrderMapper.updateUnpackingOrder(unpackingOrder);
    }

    /**
     * 删除拆包订单对象
     *
     * @param ids 需要删除的数据ID,前端控制只有一个,可以转换为long
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int deleteUnpackingOrderByIds(String ids) {
        //查询订单
        UnpackingOrder unpackingOrder = unpackingOrderMapper.selectUnpackingOrderById(Long.parseLong(ids));
        //删除快递表
        userPackageMapper.deleteUserPackageByShopeeOrderNo(unpackingOrder.getShopeeOrderNo());
        //查询库存出库记录
        List<StockInfoItem> list = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(unpackingOrder.getShopeeOrderNo());
        if (!CollectionUtils.isEmpty(list)) {
            for (StockInfoItem item : list) {
                stockInfoItemService.deleteStockInfoItemById(item.getId());
            }
        }
        User user = userMapper.selectUserById(unpackingOrder.getUserId());
        user.setPreMoney(user.getPreMoney() - 300);
        userMapper.updateUser(user);
        //删除订单表
        return unpackingOrderMapper.deleteUnpackingOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除拆包订单信息
     *
     * @param id 拆包订单ID
     * @return 结果
     */
    public int deleteUnpackingOrderById(Long id) {
        return unpackingOrderMapper.deleteUnpackingOrderById(id);
    }

    @Override
    public int queryCount(Long userId) {
        return unpackingOrderMapper.queryCount(userId);
    }

    @Override
    public void userSubmitOrder(String shopeeOrderNo) {
        //查询快递表
        List<UserPackage> list = userPackageMapper.selectByShopeeOrderNoNotNull(shopeeOrderNo);
        List<StockInfoItem> items = stockInfoItemService.selectStockInfoItemByShopeeOrderNo(shopeeOrderNo);
        if (CollectionUtils.isEmpty(list) && CollectionUtils.isEmpty(items)) {
            //库存和快递都为空,拒绝提交
            throw new BusinessException("提交失败,请配置该订单需要发送的商品");
        }

        //是否为纯库存操作标识
        boolean stockFlag = true;
        //快递是否全部已入库
        boolean hadInStock = true;
        for (UserPackage userPackage : list) {
            if (!StringUtils.isEmpty(userPackage.getPackageNo())) {
                stockFlag = false;
                if (userPackage.getStatus() != 20) {
                    hadInStock = false;
                }
                break;
            }
        }
        if (stockFlag) {
            //纯库存操作
            unpackingOrderMapper.updateStatusByShopeeOrderNo(shopeeOrderNo, 30);
        } else {
            //非纯库存操作
            //快递已全到,直接更新状态到30-已入库,否则更新为20-待入库
            if (hadInStock) {
                unpackingOrderMapper.updateStatusByShopeeOrderNo(shopeeOrderNo, 30);
            } else {
                unpackingOrderMapper.updateStatusByShopeeOrderNo(shopeeOrderNo, 20);
            }
        }
    }
}
