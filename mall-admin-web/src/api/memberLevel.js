import request from '@/utils/request'

export function fetchList() {
  return request({
    url: '/memberLevel/list',
    method: 'get'
  })
}

export function getInfo(id) {
  return request({
    url: '/memberLevel/' + id,
    method: 'get'
  })
}

export function create(data) {
  return request({
    url: '/memberLevel/create',
    method: 'post',
    data
  })
}

export function deleteBatch(params) {
  return request({
    url: '/memberLevel/delete',
    method: 'post',
    params
  })
}

export function update(data) {
  return request({
    url: '/memberLevel/update/' + data.id,
    method: 'post',
    data
  })
}
