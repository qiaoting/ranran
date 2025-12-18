package com.ranran.framework.web.controller;

import com.ranran.common.domain.entity.SysLoginLog;
import com.ranran.common.domain.enums.ResultCode;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.SecurityUtil;
import com.ranran.persistence.service.impl.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author ranran
 * controller父类
 */
public class BaseController {
    @Autowired
    private SysLoginLogService sysLoginLogService;

    protected void recordLog(String msg) {
        String username = SecurityUtil.getAuthentication().getName();
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setMsg(msg);
        sysLoginLogService.record(sysLoginLog);
    }

    protected PageVo getPageData(List<?> list, long total) {
        PageVo pageVo = new PageVo();
        pageVo.setCode(ResultCode.SUCCESS.getCode());
        pageVo.setMsg("查询成功");
        pageVo.setRows(list);
        pageVo.setTotal(total);
        return pageVo;
    }

}
