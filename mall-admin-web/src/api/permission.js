import request from '@/utils/request'

export function create(params) {
  return request({
    url: `/permission/create`,
    method: 'post',
    data: params
  })
}

export function deletePermission(params) {
  return request({
    url: `/permission/delete`,
    method: 'post',
    params
  })
}

export function fetchList() {
  return request({
    url: `/permission/list`,
    method: 'get'
  })
}

export function fetchTreeList() {
  return request({
    url: `/permission/treeList`,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: `/permission/update/${data.id}`,
    method: 'post',
    data
  })
}
