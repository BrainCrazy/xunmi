package com.ruoyi.project.system.userpackage.service;

import com.ruoyi.project.system.userpackage.domain.UserPackage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用户快递Service接口
 *
 * @author wqf
 * @date 2020-02-15
 */
public interface IUserPackageService {
    public boolean updatePackageNos(Long[] id, String[] packageNo, String orderNo, String remark);

    public List<UserPackage> selectByShopeeOrderNo(String shopeeOrderNo);

    public List<UserPackage> selectByShopeeOrderNoNotNull(String shopeeOrderNo);

    /**
     * 查询用户快递
     *
     * @param id 用户快递ID
     * @return 用户快递
     */
    public UserPackage selectUserPackageById(Long id);

    /**
     * 查询用户快递列表
     *
     * @param userPackage 用户快递
     * @return 用户快递集合
     */
    public List<UserPackage> selectUserPackageList(UserPackage userPackage);

    public int insertUserPackage(UserPackage userPackage, MultipartFile goodsImage) throws IOException;

    /**
     * 修改用户快递
     *
     * @param userPackage 用户快递
     * @return 结果
     */
    public int updateUserPackage(UserPackage userPackage);

    /**
     * 批量删除用户快递
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserPackageByIds(String ids);

    /**
     * 删除用户快递信息
     *
     * @param id 用户快递ID
     * @return 结果
     */
    public int deleteUserPackageById(Long id);

    boolean saveStockImage(MultipartFile file, long id, int orderType) throws IOException;

    List<UserPackage> getUserPackageInfo(String packageNo, boolean flag);

    List<UserPackage> getUserPackageInfoByType(long id, int orderType);
}
