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
    public void recordLogin(SysLoginLog loginLog) {
        save(loginLog);
    }

    @Async
    public void recordLogin(String username, String msg) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUsername(username);
        loginLog.setMsg(msg);
        save(loginLog);
    }

    public void deleteAll() {
        baseMapper.deleteAll();
    }

}
