package com.ranran.api.controller.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysLoginLog;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ranran
 * 登录日志接口
 */
@RestController
@RequestMapping("/api/loginlog")
@Validated
public class SysLoginLogController extends BaseController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('monitor:loginlog:index')")
    public PageVo page(SysLoginLog sysLoginLog) {
        Page<SysLoginLog> page = PageContext.getPage();
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.hasText(sysLoginLog.getUsername())) {
            queryWrapper.like(SysLoginLog::getUsername, sysLoginLog.getUsername());
        }
        if (StrUtil.hasText(sysLoginLog.getMsg())) {
            queryWrapper.like(SysLoginLog::getMsg, sysLoginLog.getMsg());
        }
        IPage<SysLoginLog> logPage = sysLoginLogService.page(page, queryWrapper);
        return getPageData(logPage.getRecords(), logPage.getTotal());
    }

    @DeleteMapping("/clear")
    @PreAuthorize("@pcs.hasPermission('monitor:loginlog:index')")
    public Result<Void> clear() {
        sysLoginLogService.deleteAll();
        return Result.success();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('monitor:loginlog:index')")
    public Result<Void> delete(@RequestParam Long logId) {
        boolean remove = sysLoginLogService.removeById(logId);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }
}