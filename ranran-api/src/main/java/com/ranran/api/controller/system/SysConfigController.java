package com.ranran.api.controller.system;

import com.ranran.common.domain.entity.SysConfig;
import com.ranran.persistence.service.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.ranran.common.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.framework.context.PageContext;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ranran.common.domain.Result;
import org.springframework.validation.annotation.Validated;
import com.ranran.framework.web.controller.BaseController;

/**
 * 系统配置表Controller
 * @author ranran
 */
@RestController
@RequestMapping("/api/config")
public class SysConfigController extends BaseController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 分页查询SysConfig数据
     */
    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:config:index')")
    public PageVo page(SysConfig sysConfig) {
        Page<SysConfig> page = PageContext.getPage();
        IPage<SysConfig> dataPage = sysConfigService.page(page);
        return getPageData(dataPage.getRecords(), dataPage.getTotal());
    }

    /**
     * 新增SysConfig数据
     */
    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:config:index')")
    public Result<Void> add(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.saveConfig(sysConfig);
        return Result.success();
    }

    /**
     * 修改SysConfig数据
     */
    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:config:index')")
    public Result<Void> edit(@Validated @RequestBody SysConfig sysConfig) {
        sysConfigService.updateConfig(sysConfig);
        return Result.success();
    }

    /**
     * 根据ID查询单个SysConfig数据
     */
    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:config:index')")
    public Result<SysConfig> getById(@RequestParam Long configId) {
        SysConfig sysConfig = sysConfigService.getById(configId);
        return Result.success(sysConfig);
    }

    /**
     * 根据ID删除SysConfig数据
     */
    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:config:index')")
    public Result<Void> delete(@RequestParam Long configId) {
        sysConfigService.deleteConfigById(configId);
        return Result.success();
    }
}
