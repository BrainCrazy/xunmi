package com.ruoyi.project.system.userpackage.mapper;

import com.ruoyi.project.system.userpackage.domain.UserPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户快递Mapper接口
 *
 * @author wqf
 * @date 2020-02-15
 */
public interface UserPackageMapper {

    int updatePackageNoById(@Param("id") long id, @Param("packageNo") String packageNo);

    int updateStockImage(@Param("id") long id, @Param("stockImage") String stockImage, @Param("status") Integer status);

    public List<UserPackage> selectByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo);

    public List<UserPackage> selectByShopeeOrderNoNotNull(@Param("shopeeOrderNo") String shopeeOrderNo);

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

    public List<UserPackage> selectUserPackageByPackageNo(@Param("packageNo") String packageNo, @Param("status") int status);

    /**
     * 新增用户快递
     *
     * @param userPackage 用户快递
     * @return 结果
     */
    public int insertUserPackage(UserPackage userPackage);

    /**
     * 修改用户快递
     *
     * @param userPackage 用户快递
     * @return 结果
     */
    public int updateUserPackage(UserPackage userPackage);

    /**
     * 删除用户快递
     *
     * @param id 用户快递ID
     * @return 结果
     */
    public int deleteUserPackageById(Long id);

    /**
     * 批量删除用户快递
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserPackageByIds(String[] ids);

    public int deleteUserPackageByShopeeOrderNo(@Param("shopeeOrderNo") String shopeeOrderNo);
}
