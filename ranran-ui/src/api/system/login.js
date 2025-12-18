import request from '@/utils/request'

// 登录API

export const login = (params) => {
    return request({
        url: '/api/auth/login',
        method: 'post',
        data: params
    })
}

export const logout = () => {
    return request({
        url: '/api/auth/logout',
        method: 'get'
    })
}

export const getInfo = () => {
    return request({
        url: '/api/auth/getInfo',
        method: 'get'
    })
}

export default {
    login,
    logout,
    getInfo
}