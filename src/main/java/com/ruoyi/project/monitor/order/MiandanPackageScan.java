package com.ruoyi.project.monitor.order;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.system.miandan.mapper.MiandanOrderMapper;
import com.ruoyi.project.system.unpackage.mapper.UnpackingOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MiandanPackageScan {

    @Autowired
    private MiandanOrderMapper miandanOrderMapper;

    @Autowired
    private UnpackingOrderMapper unpackingOrderMapper;

    @Scheduled(cron = "0 55 23  * * ?")
    public void scanMiandanPackage() {
        log.info("纯贴面单异常记录扫描开始");
        Date lastDate = DateUtils.getDateOffest(-3);
        int count = miandanOrderMapper.updateExeFlag(lastDate);
        log.info("纯贴面单异常记录扫描结束,更新记录数:" + count);
        log.info("拆包异常记录扫描开始");
        count = unpackingOrderMapper.updateExeFlag(lastDate);
        log.info("拆包异常记录扫描结束,更新记录数:" + count);
    }
}
