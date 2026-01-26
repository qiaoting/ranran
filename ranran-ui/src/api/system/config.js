import request from '@/utils/request'

// 公告API

export const getConfigList = (data) => {
    return request({
        url: '/api/config/list',
        method: 'get',
        data
    })
}

export const getConfigPage = (data) => {
    return request({
        url: '/api/config/page',
        method: 'get',
        data
    })
}

export const getConfigById = (data) => {
    return request({
        url: '/api/config/getById',
        method: 'get',
        data
    })
}

export const addConfig = (data) => {
    return request({
        url: '/api/config/add',
        method: 'post',
        data
    })
}

export const updateConfig = (data) => {
    return request({
        url: '/api/config/edit',
        method: 'post',
        data
    })
}

export const deleteConfig = (data) => {
    return request({
        url: '/api/config/delete',
        method: 'delete',
        data
    })
}