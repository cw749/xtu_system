import request from '@/utils/request'

export function getStudentPage(params) {
    return request({
        url: '/students',
        method: 'get',
        params
    })
}

export function getStudentDetail(id) {
    return request({
        url: `/students/${id}`,
        method: 'get'
    })
}

export function createStudent(data) {
    return request({
        url: '/students',
        method: 'post',
        data
    })
}

export function updateStudent(id, data) {
    return request({
        url: `/students/${id}`,
        method: 'put',
        data
    })
}

export function deleteStudent(id) {
    return request({
        url: `/students/${id}`,
        method: 'delete'
    })
}

export function batchDeleteStudents(ids) {
    return request({
        url: '/students',
        method: 'delete',
        params: {
            ids: ids.join(',')
        }
    })
}

export function importStudents(data) {
    return request({
        url: '/students/import',
        method: 'post',
        data
    })
}

export function exportStudents(params) {
    return request({
        url: '/students/export',
        method: 'get',
        params,
        responseType: 'blob'
    })
}

export function createStudentAccount(id, data) {
    return request({
        url: `/students/${id}/account`,
        method: 'post',
        data
    })
}

export function removeStudentAccount(id) {
    return request({
        url: `/students/${id}/account`,
        method: 'delete'
    })
}
