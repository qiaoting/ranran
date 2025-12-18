import request from '@/utils/request'

// 菜单API

export const getAllMenuTree = (params) => {
    return request({
        url: '/api/menu/getAllMenuTree',
        method: 'get',
        params
    })
}

export const getRoutesTree = (params) => {
    return request({
        url: '/api/menu/getRoutesTree',
        method: 'get',
        params
    })
}

export const getMenuById = (data) => {
    return request({
        url: '/api/menu/getById',
        method: 'get',
        data
    })
}

export const addMenu = (data) => {
    return request({
        url: '/api/menu/add',
        method: 'post',
        data
    })
}

export const updateMenu = (data) => {
    return request({
        url: '/api/menu/edit',
        method: 'post',
        data
    })
}

export const deleteMenu = (data) => {
    return request({
        url: '/api/menu/delete',
        method: 'delete',
        data
    })
}