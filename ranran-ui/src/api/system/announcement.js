import request from '@/utils/request'

// 公告API

export const getAnnouncementList = (data) => {
    return request({
        url: '/api/announcement/list',
        method: 'get',
        data
    })
}

export const getAnnouncementPage = (data) => {
    return request({
        url: '/api/announcement/page',
        method: 'get',
        data
    })
}

export const getAnnouncementById = (data) => {
    return request({
        url: '/api/announcement/getById',
        method: 'get',
        data
    })
}

export const addAnnouncement = (data) => {
    return request({
        url: '/api/announcement/add',
        method: 'post',
        data
    })
}

export const updateAnnouncement = (data) => {
    return request({
        url: '/api/announcement/edit',
        method: 'post',
        data
    })
}

export const deleteAnnouncement = (data) => {
    return request({
        url: '/api/announcement/delete',
        method: 'delete',
        data
    })
}