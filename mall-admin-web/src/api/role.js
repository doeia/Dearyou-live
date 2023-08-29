import request from '@/utils/request'

export function fetchList() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function getPermission(roleId) {
  return request({
    url: `/role/permission/${roleId}`,
    method: 'get'
  })
}

export function create(params) {
  return request({
    url: `/role/create`,
    method: 'post',
    data: params
  })
}

export function deleteRole(params) {
  return request({
    url: `/role/delete`,
    method: 'post',
    params
  })
}

export function updatePermission(params) {
  return request({
    url: `/role/permission/update`,
    method: 'post',
    params
  })
}

export function update(params) {
  return request({
    url: `/role/update/${params.id}`,
    method: 'post',
    data: params
  })
}

