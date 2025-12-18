import request from '@/utils/request'

// 系统用户API

export const getUserPage = (data) => {
    return request({
        url: '/api/user/page',
        method: 'get',
        data
    })
}

export const getById = (data) => {
    return request({
        url: '/api/user/getById',
        method: 'get',
        data
    })
}

export const addUser = (data) => {
    return request({
        url: '/api/user/add',
        method: 'post',
        data
    })
}

export const updateUser = (data) => {
    return request({
        url: '/api/user/edit',
        method: 'post',
        data
    })
}

export const deleteUser = (data) => {
    return request({
        url: '/api/user/delete',
        method: 'delete',
        data
    })
}
