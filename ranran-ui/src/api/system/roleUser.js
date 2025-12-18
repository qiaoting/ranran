import request from '@/utils/request'

// 角色用户

export const getUnallocatedPage = (data) => {
    return request({
        url: '/api/roleUser/unallocatedPage',
        method: 'get',
        data
    })
}

export const getRoleUserPage = (data) => {
    return request({
        url: '/api/roleUser/page',
        method: 'get',
        data
    })
}
export const removeRoleUser = (data) => {
    return request({
        url: '/api/roleUser/remove',
        method: 'delete',
        data
    })
}
export const getRoleUsers = (data) => {
    return request({
        url: '/api/roleUser/getRoleUsers',
        method: 'get',
        data
    })
}

export const assignRoleUsers = (data) => {
    return request({
        url: '/api/roleUser/appendUsers',
        method: 'post',
        data
    })
}