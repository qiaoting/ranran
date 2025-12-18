import request from '@/utils/request'

// 任务API

export const getTaskPage = (data) => {
    return request({
        url: '/api/task/page',
        method: 'get',
        data
    })
}

export const getTaskById = (data) => {
    return request({
        url: '/api/task/getById',
        method: 'get',
        data
    })
}

export const addTask = (data) => {
    return request({
        url: '/api/task/add',
        method: 'post',
        data
    })
}

export const updateTask = (data) => {
    return request({
        url: '/api/task/edit',
        method: 'post',
        data
    })
}

export const deleteTask = (data) => {
    return request({
        url: '/api/task/delete',
        method: 'delete',
        data
    })
}