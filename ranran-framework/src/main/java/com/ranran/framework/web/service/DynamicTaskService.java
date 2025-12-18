package com.ranran.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ranran.common.domain.entity.SysTask;
import com.ranran.common.domain.enums.StatusEnum;
import com.ranran.common.exception.BusinessException;
import com.ranran.common.utils.CollUtil;
import com.ranran.common.utils.SpringContextUtil;
import com.ranran.persistence.service.impl.SysTaskService;
import com.ranran.persistence.task.DynamicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author ranran
 * 动态任务
 */
@Service
public class DynamicTaskService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

    public void addCronTask(Long taskId, DynamicTask task, String cronExpression) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new BusinessException("非法Cron表达式：" + cronExpression);
        }
        CronTrigger trigger = new CronTrigger(cronExpression);
        ScheduledFuture<?> future = taskScheduler.schedule(task, trigger);
        taskMap.put(taskId, future);
    }

    public void updateCronTask(Long taskId, DynamicTask task, String newCronExpression) {
        if (!CronExpression.isValidExpression(newCronExpression)) {
            throw new BusinessException("非法Cron表达式：" + newCronExpression);
        }
        removeTask(taskId);
        CronTrigger trigger = new CronTrigger(newCronExpression);
        ScheduledFuture<?> future = taskScheduler.schedule(task, trigger);
        taskMap.put(taskId, future);
    }

    public void removeTask(Long taskId) {
        ScheduledFuture<?> future = taskMap.get(taskId);
        if (future != null) {
            future.cancel(true);
            taskMap.remove(taskId);
        }
    }

    private void initTask() {
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getStatus, StatusEnum.ENABLE.getCode());
        List<SysTask> list = sysTaskService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (SysTask sysTask : list) {
            DynamicTask taskBean = SpringContextUtil.getBean(sysTask.getTaskCode());
            addCronTask(sysTask.getTaskId(), taskBean, sysTask.getCronExpression());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 避免重复执行（父子容器可能触发多次）
        if (event.getApplicationContext().getParent() == null) {
            initTask();
        }
    }

}