package com.ranran.persistence.task;

import com.ranran.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ranran
 * 健康检查任务
 */
@Component
@Slf4j
public class SysHealthTask extends DynamicTask {

    @Override
    public void execute() {
        log.info("执行健康检查任务：" + DateUtil.now());
    }

}
