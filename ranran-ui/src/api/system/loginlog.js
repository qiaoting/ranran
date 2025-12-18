import request from '@/utils/request'

// 登录日志API

export const getLoginLogPage = (data) => {
    return request({
        url: '/api/loginlog/page',
        method: 'get',
        data
    })
}

export const deleteLoginLog = (data) => {
    return request({
        url: `/api/loginlog/delete`,
        method: 'delete',
        data
    })
}
