package com.ruoyi.project.system.logistics.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 物流公司对象 hd_logistics
 * 
 * @author ruoyi
 * @date 2019-10-22
 */
public class HdLogistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 物流公司名称 */
    @Excel(name = "物流公司名称")
    private String logisticsName;

    /** 物流公司编号 */
    @Excel(name = "物流公司编号")
    private String logisticsNum;

    /** $column.columnComment */
    @Excel(name = "物流公司编号")
    private String createUser;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticsName(String logisticsName) 
    {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsName() 
    {
        return logisticsName;
    }
    public void setLogisticsNum(String logisticsNum) 
    {
        this.logisticsNum = logisticsNum;
    }

    public String getLogisticsNum() 
    {
        return logisticsNum;
    }
    public void setCreateUser(String createUser) 
    {
        this.createUser = createUser;
    }

    public String getCreateUser() 
    {
        return createUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticsName", getLogisticsName())
            .append("logisticsNum", getLogisticsNum())
            .append("createTime", getCreateTime())
            .append("createUser", getCreateUser())
            .toString();
    }
}
