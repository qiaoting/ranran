import request from '@/utils/request'

// 角色API

export const getRolePage = (data) => {
    return request({
        url: '/api/role/page',
        method: 'get',
        data
    })
}

export const getRoleById = (data) => {
    return request({
        url: '/api/role/getById',
        method: 'get',
        data
    })
}

export const addRole = (data) => {
    return request({
        url: '/api/role/add',
        method: 'post',
        data
    })
}

export const updateRole = (data) => {
    return request({
        url: '/api/role/edit',
        method: 'post',
        data
    })
}

export const changeStatus = (data) => {
    return request({
        url: '/api/role/changeStatus',
        method: 'post',
        data
    })
}

export const deleteRole = (data) => {
    return request({
        url: '/api/role/delete',
        method: 'delete',
        data
    })
}