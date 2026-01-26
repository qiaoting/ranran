package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ranran.common.constant.Constant;
import com.ranran.common.domain.entity.SysConfig;
import com.ranran.common.domain.enums.ConfigTypeEnum;
import com.ranran.common.exception.BusinessException;
import com.ranran.common.utils.CollUtil;
import com.ranran.common.utils.ObjUtil;
import com.ranran.persistence.mapper.SysConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统配置表Service
 * @author ranran
 */
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> {
    @Autowired
    private RedisTemplate redisTemplate;
    @PostConstruct
    public void init() {
        writeAllCache();
    }
    public void writeAllCache() {
        List<SysConfig> list = list();
        if (CollUtil.isEmpty(list))
            return;
        list.forEach(sysConfig -> {
            redisTemplate.opsForValue().set(Constant.CONFIG_KEY_PREFIX +
                    sysConfig.getConfigKey(), sysConfig.getConfigValue());
        });
    }
    public void deleteCache(SysConfig sysConfig) {
        redisTemplate.delete(Constant.CONFIG_KEY_PREFIX +
                sysConfig.getConfigKey());
    }
    public void writeCache(SysConfig sysConfig) {
        redisTemplate.opsForValue().set(Constant.CONFIG_KEY_PREFIX +
                sysConfig.getConfigKey(), sysConfig.getConfigValue());
    }
    public void saveConfig(SysConfig sysConfig) {
        if (save(sysConfig)) {
            writeCache(sysConfig);
        }
    }
    public void updateConfig(SysConfig sysConfig) {
        if (updateById(sysConfig)) {
            deleteCache(sysConfig);
            writeCache(sysConfig);
        }
    }
    public void deleteConfigById(Long configId) {
        SysConfig sysConfig = getById(configId);
        if (ObjUtil.isNotNull(sysConfig)
                && sysConfig.getConfigType().equals(ConfigTypeEnum.SYSTEM.getCode())) {
            throw new BusinessException("系统配置禁止删除");
        }
        if (removeById(configId)) {
            deleteCache(sysConfig);
        }
    }
    public String getConfigByKey(String configKey) {
        Object value = redisTemplate.opsForValue().get(Constant.CONFIG_KEY_PREFIX +
                configKey);
        if (ObjUtil.isNull(value)) {
            LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysConfig::getConfigKey, configKey);
            SysConfig sysConfig = getOne(queryWrapper);
            if (ObjUtil.isNotNull(sysConfig)) {
                return sysConfig.getConfigValue();
            } else {
                return "";
            }
        }
        return "";
    }
}