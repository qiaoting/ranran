package com.ranran.api.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysAnnouncement;
import com.ranran.common.domain.enums.StatusEnum;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.DateUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.persistence.service.impl.SysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ranran
 * 公告接口
 */
@RestController
@RequestMapping("/api/announcement")
public class SysAnnouncementController extends BaseController {
    @Autowired
    private SysAnnouncementService sysAnnouncementService;

    @GetMapping("/list")
    public Result<List<SysAnnouncement>> list() {
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAnnouncement::getStatus, StatusEnum.ENABLE.getCode());
        queryWrapper.orderByAsc(SysAnnouncement::getAnnouncementId);
        queryWrapper.ge(SysAnnouncement::getCreateTime, DateUtil.yesterday());
        List<SysAnnouncement> announceList = sysAnnouncementService.list(queryWrapper);
        return Result.success(announceList);
    }

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:announcement:index')")
    public PageVo page(SysAnnouncement announcement) {
        Page<SysAnnouncement> page = PageContext.getPage();
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.hasText(announcement.getTitle())) {
            queryWrapper.like(SysAnnouncement::getTitle, announcement.getTitle());
        }
        IPage<SysAnnouncement> announcePage = sysAnnouncementService.page(page, queryWrapper);
        return getPageData(announcePage.getRecords(), announcePage.getTotal());
    }

    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:announcement:index')")
    public Result<Void> add(@Validated @RequestBody SysAnnouncement announcement) {
        boolean save = sysAnnouncementService.save(announcement);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("新增失败");
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:announcement:index')")
    public Result<Void> edit(@Validated @RequestBody SysAnnouncement announcement) {
        boolean save = sysAnnouncementService.updateById(announcement);
        if (save) {
            return Result.success();
        } else {
            return Result.fail("修改失败");
        }
    }

    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:announcement:index')")
    public Result<SysAnnouncement> getById(@RequestParam Long announcementId) {
        SysAnnouncement announcement = sysAnnouncementService.getById(announcementId);
        return Result.success(announcement);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:announcement:index')")
    public Result<Void> delete(@RequestParam Long announcementId) {
        boolean remove = sysAnnouncementService.removeById(announcementId);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }

}
