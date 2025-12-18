package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.domain.entity.SysLoginLog;
import com.ranran.persistence.mapper.SysLoginLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author ranran
 * 登录日志Service
 */
@Service
public class SysLoginLogService extends ServiceImpl<SysLoginLogMapper, SysLoginLog> {
    @Async
    public void record(SysLoginLog loginLog) {
        save(loginLog);
    }
}
