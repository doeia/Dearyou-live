import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/admin/list',
    method: 'get',
    params: params
  })
}

export function info() {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

export function deleteAdmin(adminId) {
  return request({
    url: `/admin/delete/${adminId}`,
    method: 'post'
  })
}

export function updatePermission(params) {
  return request({
    url: `/admin/permission/update`,
    method: 'post',
    params
  })
}

export function getPermission(adminId) {
  return request({
    url: `/admin/permission/${adminId}`,
    method: 'get'
  })
}

export function updateRole(params) {
  return request({
    url: `/admin/role/update`,
    method: 'post',
    params
  })
}

export function getRole(adminId) {
  return request({
    url: `/admin/role/${adminId}`,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/admin/update/${data.id}`,
    method: 'post',
    data
  })
}

export function updatePassword(params) {
  return request({
    url: `/admin/updatePassword`,
    method: 'post',
    data: params
  })
}

export function getDetail(adminId) {
  return request({
    url: `/admin/${adminId}`,
    method: 'get'
  })
}

export function register(data) {
  return request({
    url: `/admin/register`,
    method: 'post',
    data
  })
}
