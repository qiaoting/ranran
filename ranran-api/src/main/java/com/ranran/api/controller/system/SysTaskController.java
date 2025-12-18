package com.ranran.api.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranran.common.domain.Result;
import com.ranran.common.domain.entity.SysTask;
import com.ranran.common.domain.enums.StatusEnum;
import com.ranran.common.domain.vo.PageVo;
import com.ranran.common.utils.SpringContextUtil;
import com.ranran.common.utils.StrUtil;
import com.ranran.framework.context.PageContext;
import com.ranran.framework.web.controller.BaseController;
import com.ranran.framework.web.service.DynamicTaskService;
import com.ranran.persistence.service.impl.SysTaskService;
import com.ranran.persistence.task.DynamicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author ranran
 * 定时任务接口
 */
@RestController
@RequestMapping("/api/task")
public class SysTaskController extends BaseController {
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private DynamicTaskService dynamicTaskService;

    @GetMapping("/page")
    @PreAuthorize("@pcs.hasPermission('system:task:index')")
    public PageVo page(SysTask sysTask) {
        Page<SysTask> page = PageContext.getPage();
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.hasText(sysTask.getTaskName())) {
            queryWrapper.like(SysTask::getTaskName, sysTask.getTaskName());
        }
        if (StrUtil.hasText(sysTask.getTaskCode())) {
            queryWrapper.like(SysTask::getTaskCode, sysTask.getTaskCode());
        }
        if (StrUtil.hasText(sysTask.getStatus())) {
            queryWrapper.like(SysTask::getStatus, sysTask.getStatus());
        }
        IPage<SysTask> announcePage = sysTaskService.page(page, queryWrapper);
        return getPageData(announcePage.getRecords(), announcePage.getTotal());
    }

    @PostMapping("/add")
    @PreAuthorize("@pcs.hasPermission('system:task:index')")
    public Result<String> add(@Validated @RequestBody SysTask sysTask) {
        sysTaskService.save(sysTask);
        dynamicTaskService.addCronTask(sysTask.getTaskId(),
                SpringContextUtil.getBean(sysTask.getTaskCode()),
                sysTask.getCronExpression());
        return Result.success("任务加入调度成功");
    }

    @PostMapping("/edit")
    @PreAuthorize("@pcs.hasPermission('system:task:index')")
    public Result<String> edit(@Validated @RequestBody SysTask sysTask) {
        sysTaskService.updateById(sysTask);
        SysTask dbTask = sysTaskService.getById(sysTask.getTaskId());
        if (dbTask.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            dynamicTaskService.updateCronTask(dbTask.getTaskId(),
                    SpringContextUtil.getBean(dbTask.getTaskCode()),
                    dbTask.getCronExpression());
        }
        return Result.success("任务修改并重新调度成功");
    }

    @GetMapping("/getById")
    @PreAuthorize("@pcs.hasPermission('system:task:index')")
    public Result<SysTask> getById(@RequestParam Long taskId) {
        SysTask announcement = sysTaskService.getById(taskId);
        return Result.success(announcement);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@pcs.hasPermission('system:task:index')")
    public Result<String> delete(@RequestParam Long taskId) {
        dynamicTaskService.removeTask(taskId);
        sysTaskService.removeById(taskId);
        return Result.success("任务调度删除成功");
    }

}
