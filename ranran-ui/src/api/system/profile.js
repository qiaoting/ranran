import request from '@/utils/request'

// 个人中心API

export const getSelf = (data) => {
    return request({
        url: '/api/profile/getSelf',
        method: 'get',
        data
    })
}

export const updatePassword = (data) => {
    return request({
        url: '/api/profile/updatePassword',
        method: 'post',
        data
    })
}

export const updateSelf = (data) => {
    return request({
        url: '/api/profile/updateSelf',
        method: 'post',
        data
    })
}