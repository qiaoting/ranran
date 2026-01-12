import request from '@/utils/request'

export const listTable = (data) => {
    return request({
        url: '/api/generator/listTable',
        method: 'get',
        data
    })
}

export const generateCode = (data) => {
    return request({
        url: '/api/generator/getCode',
        method: 'post',
        data
    })
}


