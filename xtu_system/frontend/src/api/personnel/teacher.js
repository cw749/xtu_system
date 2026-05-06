import request from '@/utils/request'

export function getTeacherPage(params) {
    return request({
        url: '/teachers',
        method: 'get',
        params
    })
}

export function getTeacherDetail(id) {
    return request({
        url: `/teachers/${id}`,
        method: 'get'
    })
}

export function createTeacher(data) {
    return request({
        url: '/teachers',
        method: 'post',
        data
    })
}

export function updateTeacher(id, data) {
    return request({
        url: `/teachers/${id}`,
        method: 'put',
        data
    })
}

export function deleteTeacher(id) {
    return request({
        url: `/teachers/${id}`,
        method: 'delete'
    })
}

export function batchDeleteTeachers(ids) {
    return request({
        url: '/teachers',
        method: 'delete',
        params: {
            ids: ids.join(',')
        }
    })
}

export function importTeachers(data) {
    return request({
        url: '/teachers/import',
        method: 'post',
        data
    })
}

export function exportTeachers(params) {
    return request({
        url: '/teachers/export',
        method: 'get',
        params,
        responseType: 'blob'
    })
}

export function createTeacherAccount(id, data) {
    return request({
        url: `/teachers/${id}/account`,
        method: 'post',
        data
    })
}

export function removeTeacherAccount(id) {
    return request({
        url: `/teachers/${id}/account`,
        method: 'delete'
    })
}
